package godngu.securityjwt.security.login;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

//    private final ObjectMapper objectMapper;
//
//    protected LoginAuthFilter(String defaultFilterProcessesUrl,
//        ObjectMapper objectMapper) {
//        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
//        this.objectMapper = objectMapper;
//    }

    @Autowired
    private ObjectMapper objectMapper;

    public LoginAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {

        LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
        verify(loginRequest);

        return this.getAuthenticationManager().authenticate(
            PreLoginAuthenticationToken.create(loginRequest)
        );
    }

    private void verify(LoginRequest loginRequest) {
        if (isEmpty(loginRequest.getEmail()) || isEmpty(loginRequest.getPassword())) {
            throw new IllegalArgumentException("Email or Password is empty");
        }
    }
}
