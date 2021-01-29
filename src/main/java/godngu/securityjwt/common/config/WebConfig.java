package godngu.securityjwt.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/swagger-ui.html");
        // TODO: swagger url을 "/docs" 로 변경한다.
        registry.addRedirectViewController("/docs", "/swagger-ui.html");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            // TODO 웹이나, 서버는 설정이 가능하겠는데 모바일은 설정하기 어렵지 않을까? 그래서 "*"로 사용?
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "HEAD", "OPTIONS", "DELETE")
            .maxAge(3600);// 3600초 동안 preflight 결과를 캐시에 저장O
    }
}
