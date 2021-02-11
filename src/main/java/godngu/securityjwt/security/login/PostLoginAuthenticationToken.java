package godngu.securityjwt.security.login;

import godngu.securityjwt.security.common.SecurityMemberContext;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class PostLoginAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private PostLoginAuthenticationToken(Object principal, Object credentials,
        Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static PostLoginAuthenticationToken create(SecurityMemberContext context) {
        return new PostLoginAuthenticationToken(context, null, context.getAuthorities());
    }

    public SecurityMemberContext getSecurityMemberContext() {
        return (SecurityMemberContext) getPrincipal();
    }
}
