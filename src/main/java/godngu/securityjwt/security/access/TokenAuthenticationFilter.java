package godngu.securityjwt.security.access;

import godngu.securityjwt.security.common.SecurityConstants;
import godngu.securityjwt.security.jwt.JwtConfig;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public TokenAuthenticationFilter(RequestMatcher matcher) {
        super(matcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {

        String token = extractToken(request);
        return this.getAuthenticationManager().authenticate(
            new UsernamePasswordAuthenticationToken(token, null));
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

}
