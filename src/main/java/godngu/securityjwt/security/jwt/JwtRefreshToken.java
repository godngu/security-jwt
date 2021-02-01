package godngu.securityjwt.security.jwt;

import godngu.securityjwt.security.common.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.security.core.GrantedAuthority;

public class JwtRefreshToken implements JwtToken {

    private Claims claims;
    private String token;

    public JwtRefreshToken(Claims claims, String token) {
        this.claims = claims;
        this.token = token;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    private JwtRefreshToken generateToken(String email, Collection<GrantedAuthority> authorities, String issuer, Date expiration,
        SecretKey secretKey) {

        Claims claims = Jwts.claims().setSubject(email);
        claims.put(SecurityConstants.AUTHORITIES, authorities);

        String token = Jwts.builder()
            .setClaims(claims)
            .setId(UUID.randomUUID().toString())
            .setIssuer(issuer)
            .setIssuedAt(new Date())
            .setExpiration(expiration)
            .signWith(secretKey)
            .compact();

        return new JwtRefreshToken(claims, token);
    }
}
