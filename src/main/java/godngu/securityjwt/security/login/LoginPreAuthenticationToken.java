package godngu.securityjwt.security.login;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginPreAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private LoginPreAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public static LoginPreAuthenticationToken create(String email, String password) {
        return new LoginPreAuthenticationToken(email, password);
    }

    public static LoginPreAuthenticationToken create(LoginRequest loginRequest) {
        return new LoginPreAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
    }

    public String getEmail() {
        return (String) getPrincipal();
    }

    public String getPassword() {
        return (String) getCredentials();
    }
}
