package godngu.securityjwt.security.access;

import godngu.securityjwt.security.common.SecurityMemberContext;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtPostAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private JwtPostAuthenticationToken(Object principal, Object credentials,
        Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static JwtPostAuthenticationToken create(SecurityMemberContext context) {
        return new JwtPostAuthenticationToken(context, null, context.getAuthorities());
    }

    public SecurityMemberContext getSecurityMemberContext() {
        return (SecurityMemberContext) getPrincipal();
    }
}
