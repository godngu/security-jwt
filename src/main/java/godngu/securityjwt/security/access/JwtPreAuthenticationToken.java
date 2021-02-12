package godngu.securityjwt.security.access;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtPreAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private JwtPreAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public static JwtPreAuthenticationToken create(String token) {
        return new JwtPreAuthenticationToken(null, token);
    }

    public String getToken() {
        return (String) getCredentials();
    }
}
