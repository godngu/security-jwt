package godngu.securityjwt.security.config;

import static godngu.securityjwt.domain.entity.RoleType.ROLE_ADMIN;
import static godngu.securityjwt.domain.entity.RoleType.ROLE_MANAGER;
import static godngu.securityjwt.domain.entity.RoleType.ROLE_USER;

import godngu.securityjwt.domain.entity.Member;
import godngu.securityjwt.domain.entity.Role;
import godngu.securityjwt.domain.entity.RoleType;
import godngu.securityjwt.domain.repository.MemberRepository;
import godngu.securityjwt.domain.repository.MemberRoleRepository;
import godngu.securityjwt.domain.repository.RoleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (this.alreadySetup) {
            return;
        }

        init();

        this.alreadySetup = true;
    }

    private void init() {
        Role roleAdmin = createRoleIfNotFound(ROLE_ADMIN);
        Role roleManager = createRoleIfNotFound(ROLE_MANAGER);
        Role roleUser = createRoleIfNotFound(ROLE_USER);

        createMemberIfNotFound("admin@test.com", "1111", 40, roleAdmin);
        createMemberIfNotFound("manager@test.com", "1111", 30, roleManager);
        createMemberIfNotFound("user@test.com", "1111", 20, roleUser);
    }

    private Member createMemberIfNotFound(String email, String password, int age, Role role) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            return member.get();
        }
        return memberRepository.save(Member.createMember(email, password, age, role));
    }

    private Role createRoleIfNotFound(RoleType roleType) {
        Optional<Role> role = roleRepository.findByRoleType(roleType);
        if (role.isPresent()) {
            return role.get();
        }
        return roleRepository.save(new Role(roleType));
    }
}
