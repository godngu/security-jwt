package godngu.securityjwt.security.jwt;

import static java.nio.charset.StandardCharsets.UTF_8;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.http.HttpHeaders;

@ConfigurationProperties(prefix = "application.jwt")
@ConstructorBinding
@RequiredArgsConstructor
public class JwtConfig {

    private final String secretKey;
//    @Getter
//    private final String tokenPrefix;
    @Getter
    private final Integer tokenExpirationAfterMinutes;
    @Getter
    private final Integer refreshTokenExpirationAfterDays;
    @Getter
    private final String issuer;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public Date getTokenExpiration() {
        return toDate(LocalDateTime.now().plusMinutes(tokenExpirationAfterMinutes));
    }

    public Date getRefreshTokenExpiration() {
        return toDate(LocalDateTime.now().plusDays(refreshTokenExpirationAfterDays));
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public SecretKey getSecretKey() {
        return generateSecretKey(this.secretKey);
    }

    private SecretKey generateSecretKey(String key) {
        return Keys.hmacShaKeyFor(key.getBytes(UTF_8));
    }

//    public String makeBearerToken(String token) {
//        return this.tokenPrefix + token;
//    }
}
