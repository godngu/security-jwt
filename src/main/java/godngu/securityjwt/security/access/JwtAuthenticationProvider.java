package godngu.securityjwt.security.access;

import static godngu.securityjwt.security.common.SecurityConstants.AUTHORITIES;
import static godngu.securityjwt.security.common.SecurityConstants.AUTHORITY;

import godngu.securityjwt.security.common.SecurityMemberContext;
import godngu.securityjwt.security.exception.JwtTokenExpiredException;
import godngu.securityjwt.security.jwt.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtConfig jwtConfig;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        JwtPreAuthenticationToken authenticationToken = (JwtPreAuthenticationToken) authentication;
        String token = authenticationToken.getToken();
        
        // 토큰 검증
        Claims claims = parse(token);
        String email = claims.getSubject();
        Long memberId = getMemberId(claims);
        List<GrantedAuthority> authorities = getAuthorities(claims);

        // ROLE 검증?
        SecurityMemberContext securityMemberContext = SecurityMemberContext.create(memberId, email, authorities);
        return JwtPostAuthenticationToken.create(securityMemberContext);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtPreAuthenticationToken.class);
    }

    private Claims parse(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("Jwt token is expired.");
        } catch (SignatureException e) {
            throw new BadCredentialsException(String.format("Invalid jwt token: %s", token));
        }
    }

    private List<GrantedAuthority> getAuthorities(Claims claims) {
        var authorities = (List<Map<String, String>>) claims.get(AUTHORITIES);
        return authorities.stream()
            .map(m -> new SimpleGrantedAuthority(m.get(AUTHORITY)))
            .collect(Collectors.toList());
    }

    private Long getMemberId(Claims claims) {
        return claims.get("memberId", Long.class);
    }
}
