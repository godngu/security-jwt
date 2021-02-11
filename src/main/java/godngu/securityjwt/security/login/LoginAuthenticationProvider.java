package godngu.securityjwt.security.login;

import static godngu.securityjwt.security.login.PostLoginAuthenticationToken.create;

import godngu.securityjwt.domain.entity.Member;
import godngu.securityjwt.domain.exception.EntityNotFoundException;
import godngu.securityjwt.domain.repository.MemberRepository;
import godngu.securityjwt.security.common.SecurityMemberContext;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class LoginAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");

        PreLoginAuthenticationToken authenticationToken = (PreLoginAuthenticationToken) authentication;

        String email = authenticationToken.getEmail();
        String password = authenticationToken.getPassword();

        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BadCredentialsException("BadCredentialsException");
        }

        if (CollectionUtils.isEmpty(member.getMemberRoles())) {
            throw new InsufficientAuthenticationException("Member has no roles assigned");
        }

        return create(createSecurityMemberContext(member));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreLoginAuthenticationToken.class);
    }

    private SecurityMemberContext createSecurityMemberContext(Member member) {
        Collection<GrantedAuthority> authorities = member.getMemberRoles().stream()
            .map(memberRole -> memberRole.getRole().getRoleType().name())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

        return new SecurityMemberContext(member.getId(), member.getEmail(), authorities);
    }
}
