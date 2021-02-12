package godngu.securityjwt.security.login;

import godngu.securityjwt.security.common.SecurityMemberContext;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class LoginPostAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private LoginPostAuthenticationToken(Object principal, Object credentials,
        Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static LoginPostAuthenticationToken create(SecurityMemberContext context) {
        return new LoginPostAuthenticationToken(context, null, context.getAuthorities());
    }

    public SecurityMemberContext getSecurityMemberContext() {
        return (SecurityMemberContext) getPrincipal();
    }
}
