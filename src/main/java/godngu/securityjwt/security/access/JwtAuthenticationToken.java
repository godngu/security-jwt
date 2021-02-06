package godngu.securityjwt.security.access;

import godngu.securityjwt.security.common.SecurityMemberContext;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private String token;
    private SecurityMemberContext securityMemberContext;

    /**
     * 인증받기 전 사용자가 입력하는 정보를 담는 생성자
     */
    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        this.setAuthenticated(false);
    }

    /**
     * 인증 후 인증에 성공한 결과를 담는 생성자
     */
    public JwtAuthenticationToken(SecurityMemberContext securityMemberContext, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.securityMemberContext = securityMemberContext;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return this.securityMemberContext;
    }

    public void setAuthenticated(boolean isAuthenticated) {
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.token = null;
    }
}
