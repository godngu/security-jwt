package godngu.securityjwt.security.login;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class PreLoginAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private PreLoginAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public static PreLoginAuthenticationToken create(String email, String password) {
        return new PreLoginAuthenticationToken(email, password);
    }

    public static PreLoginAuthenticationToken create(LoginRequest loginRequest) {
        return new PreLoginAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
    }

    public String getEmail() {
        return (String) getPrincipal();
    }

    public String getPassword() {
        return (String) getCredentials();
    }
}
