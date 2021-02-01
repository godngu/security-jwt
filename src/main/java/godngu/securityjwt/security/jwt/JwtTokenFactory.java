package godngu.securityjwt.security.jwt;

import static godngu.securityjwt.domain.entity.RoleType.ROLE_REFRESH;
import static godngu.securityjwt.security.common.SecurityConstants.AUTHORITIES;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import godngu.securityjwt.domain.entity.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenFactory {

    private final JwtConfig jwtConfig;

    public String generateAccessToken(String email, Collection<GrantedAuthority> authorities) {
        return generateToken(email, authorities, UUID.randomUUID());
    }

    public String generateRefreshToken(String email, UUID uuid) {
        return generateAccessToken(email, Arrays.asList(new SimpleGrantedAuthority(ROLE_REFRESH.name())));
    }

    public String generateToken(String email, Collection<GrantedAuthority> authorities, UUID uuid) {

        verifyEmailAndAuthorities(email, authorities);

        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "jwt");
        header.put("alg", algorithm.getValue());

        Claims payload = Jwts.claims().setSubject(email);
        payload.put(AUTHORITIES, authorities);

        return Jwts.builder()
            .setHeader(header)
            .setClaims(payload)
            .setId(uuid.toString())
            .setIssuer(jwtConfig.getIssuer())
            .setIssuedAt(new Date())
            .setExpiration(jwtConfig.getTokenExpiration())
            .signWith(jwtConfig.getSecretKey(), algorithm)
            .compact();
    }

    private void verifyEmailAndAuthorities(String email, Collection<GrantedAuthority> authorities) {
        if (isEmpty(email)) {
            throw new IllegalArgumentException("Cannot create JWT Token without email.");
        }
        if (CollectionUtils.isEmpty(authorities)) {
            throw new IllegalArgumentException("User doesn't have any privileges.");
        }
    }
}
