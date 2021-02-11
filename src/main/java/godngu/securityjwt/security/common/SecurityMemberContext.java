package godngu.securityjwt.security.common;

import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public class SecurityMemberContext {

    private final Long memberId;
    private final String email;
    private final Collection<GrantedAuthority> authorities;

    public static SecurityMemberContext create(Long memberId, String email, Collection<GrantedAuthority> authorities) {
        return new SecurityMemberContext(memberId, email, authorities);
    }

}
