package godngu.securityjwt.security.login;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import godngu.securityjwt.domain.exception.EntityNotFoundException;
import godngu.securityjwt.domain.repository.MemberRepository;
import godngu.securityjwt.security.jwt.JwtTokenFactory;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtTokenFactory tokenFactory;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        String email = (String) authentication.getPrincipal();
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();

        UUID uuid = UUID.randomUUID();
        String accessToken = tokenFactory.generateAccessToken(email, authorities);
        String refreshToken = tokenFactory.generateRefreshToken(email, uuid);

        memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new)
            .login(uuid.toString());

        sendResponse(response, new LoginResponse(accessToken, refreshToken));
    }

    private void sendResponse(HttpServletResponse response, LoginResponse loginResponse) throws IOException {
        response.setStatus(OK.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), loginResponse);
    }
}
