package godngu.securityjwt.security.config;

import static org.springframework.security.config.BeanIds.AUTHENTICATION_MANAGER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import godngu.securityjwt.security.Service.SecurityResourceService;
import godngu.securityjwt.security.access.JwtAccessDeniedHandler;
import godngu.securityjwt.security.access.JwtAuthenticationEntryPoint;
import godngu.securityjwt.security.access.SkipPathRequestMatcher;
import godngu.securityjwt.security.access.JwtAuthenticationFilter;
import godngu.securityjwt.security.access.JwtAuthenticationProvider;
import godngu.securityjwt.security.common.UrlFilterInvocationSecurityMetadataSource;
import godngu.securityjwt.security.common.UrlResourcesMapFactoryBean;
import godngu.securityjwt.security.jwt.JwtConfig;
import godngu.securityjwt.security.login.LoginFailureHandler;
import godngu.securityjwt.security.login.LoginSuccessHandler;
import godngu.securityjwt.security.login.LoginAuthenticationProvider;
import godngu.securityjwt.security.login.LoginAuthenticationFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PERMIT_ALL_RESOURCES = {"/", "/login*", "/hello"};
    public static final String API_ROOT_URL = "/api/**";

    private final JwtConfig jwtConfig;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final SecurityResourceService securityResourceService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .csrf().disable()
            .httpBasic().disable() // 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
            .formLogin().disable()
            .sessionManagement()
            .sessionCreationPolicy(STATELESS);
        http
            .authorizeRequests()
            .antMatchers(PERMIT_ALL_RESOURCES).permitAll();
        http
            .authorizeRequests()
            .antMatchers(API_ROOT_URL).authenticated();
        http
            .addFilterBefore(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);

        http
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler);
    }

    @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());
        return filterSecurityInterceptor;
    }

    @Bean
    public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() throws Exception {
        return new UrlFilterInvocationSecurityMetadataSource(
            urlResourcesMapFactoryBean().getObject(), securityResourceService);
    }

    private UrlResourcesMapFactoryBean urlResourcesMapFactoryBean() {
        return new UrlResourcesMapFactoryBean(securityResourceService);
    }

    @Bean
    public AccessDecisionManager affirmativeBased() {
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(roleVoter());
        return accessDecisionVoters;
    }

    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        return new RoleHierarchyImpl();
    }

    protected JwtAuthenticationFilter tokenAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(
            new SkipPathRequestMatcher(Arrays.asList(PERMIT_ALL_RESOURCES), API_ROOT_URL));
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginAuthProvider());
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    public LoginAuthenticationProvider loginAuthProvider() {
        return new LoginAuthenticationProvider();
    }

    @Bean
    public LoginAuthenticationFilter loginAuthenticationFilter() throws Exception {
        LoginAuthenticationFilter filter = new LoginAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean(AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
