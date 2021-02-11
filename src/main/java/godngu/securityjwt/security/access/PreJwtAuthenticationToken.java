package godngu.securityjwt.security.access;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class PreJwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private PreJwtAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public static PreJwtAuthenticationToken create(String token) {
        return new PreJwtAuthenticationToken(null, token);
    }

    public String getToken() {
        return (String) getCredentials();
    }
}
