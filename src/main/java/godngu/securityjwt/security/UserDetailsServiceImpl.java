package godngu.securityjwt.security;

import godngu.securityjwt.domain.entity.Member;
import godngu.securityjwt.domain.repository.MemberRepository;
import godngu.securityjwt.security.login.MemberContext;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("UsernameNotFoundException"));
        return createMemberContext(member);
    }

    private MemberContext createMemberContext(Member member) {
        List<SimpleGrantedAuthority> authorities = member.getMemberRoles().stream()
            .map(memberRole -> memberRole.getRole().getRoleType().name())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
        return new MemberContext(member, authorities);
    }
}
