package godngu.securityjwt.security.access;

import godngu.securityjwt.security.common.SecurityMemberContext;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class PostJwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private PostJwtAuthenticationToken(Object principal, Object credentials,
        Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static PostJwtAuthenticationToken create(SecurityMemberContext context) {
        return new PostJwtAuthenticationToken(context, null, context.getAuthorities());
    }

    public SecurityMemberContext getSecurityMemberContext() {
        return (SecurityMemberContext) getPrincipal();
    }
}
