package godngu.securityjwt.security.access;

import godngu.securityjwt.security.common.SecurityConstants;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JwtAuthenticationFilter(RequestMatcher matcher) {
        super(matcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {

        String token = extractToken(request);
        return this.getAuthenticationManager().authenticate(
            new JwtAuthenticationToken(token));
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(authorizationHeader)) {
            throw new AuthenticationServiceException("Authorization header cannot be empty");
        }

        String tokenPrefix = SecurityConstants.tokenPrefix;
        if (!authorizationHeader.startsWith(tokenPrefix)) {
            throw new AuthenticationServiceException(
                String.format("Authorization header must be start with %s", tokenPrefix));
        }

        return authorizationHeader.replace(tokenPrefix, "");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
        Authentication authResult) throws IOException, ServletException {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }
}
