package godngu.securityjwt.security.access;

import static godngu.securityjwt.security.common.SecurityConstants.AUTHORITIES;
import static godngu.securityjwt.security.common.SecurityConstants.AUTHORITY;

import godngu.securityjwt.security.jwt.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final JwtConfig jwtConfig;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = (String) authentication.getPrincipal();
        
        // 토큰 검증
        Claims claims = parse(token);
        String email = claims.getSubject();
        List<GrantedAuthority> authorities = getAuthorities(claims);

        // ROLE 검증?

        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private Claims parse(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(jwtConfig.getSecretKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private List<GrantedAuthority> getAuthorities(Claims claims) {
        var authorities = (List<Map<String, String>>) claims.get(AUTHORITIES);
        return authorities.stream()
            .map(m -> new SimpleGrantedAuthority(m.get(AUTHORITY)))
            .collect(Collectors.toList());
    }
}
