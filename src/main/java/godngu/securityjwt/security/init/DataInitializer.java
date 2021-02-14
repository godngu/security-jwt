package godngu.securityjwt.security.init;

import static godngu.securityjwt.domain.entity.RoleType.ROLE_ADMIN;
import static godngu.securityjwt.domain.entity.RoleType.ROLE_MANAGER;
import static godngu.securityjwt.domain.entity.RoleType.ROLE_USER;
import static org.springframework.http.HttpMethod.GET;

import godngu.securityjwt.domain.entity.Member;
import godngu.securityjwt.domain.entity.Resource;
import godngu.securityjwt.domain.entity.Role;
import godngu.securityjwt.domain.entity.RoleHierarchy;
import godngu.securityjwt.domain.entity.RoleType;
import godngu.securityjwt.domain.repository.MemberRepository;
import godngu.securityjwt.domain.repository.MemberRoleRepository;
import godngu.securityjwt.domain.repository.ResourceRepository;
import godngu.securityjwt.domain.repository.ResourceRoleRepository;
import godngu.securityjwt.domain.repository.RoleHierarchyRepository;
import godngu.securityjwt.domain.repository.RoleRepository;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final ResourceRepository resourceRepository;
    private final RoleHierarchyRepository roleHierarchyRepository;

    @PersistenceContext
    private EntityManager em;

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

        createResourceIfNotFound("/api/hello/admin", "테스트 관리자 페이지", GET, roleAdmin);
        createResourceIfNotFound("/api/hello/manager", "테스트 매니저 페이지", GET, roleManager);
        createResourceIfNotFound("/api/hello/user", "테스트 회원 페이지", GET, roleUser);

        createRoleHierarchyIfNotFound(roleManager, roleAdmin);
        createRoleHierarchyIfNotFound(roleUser, roleManager);
    }

    public void createRoleHierarchyIfNotFound(Role currentRole, Role parentRole) {
        consoleMessage("RoleHierarchy", parentRole.getRoleDescription() +" > " + currentRole.getRoleDescription());
        RoleHierarchy roleHierarchy = roleHierarchyRepository.findByRoleType(parentRole.getRoleType());
        if (roleHierarchy == null) {
            roleHierarchy = new RoleHierarchy(parentRole.getRoleType());
        }
        RoleHierarchy parentRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);

        roleHierarchy = roleHierarchyRepository.findByRoleType(currentRole.getRoleType());
        if (roleHierarchy == null) {
            roleHierarchy = new RoleHierarchy(currentRole.getRoleType());
        }

        RoleHierarchy childRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);
        childRoleHierarchy.setParent(parentRoleHierarchy);

        em.flush();
    }

    private Resource createResourceIfNotFound(String resourceUrl, String resourceName, HttpMethod httpMethod, Role role) {
        consoleMessage("Resource", resourceUrl);
        Resource resource = resourceRepository.findByResourceUrl(resourceUrl);

        if (resource == null) {
            resource = Resource.create(resourceUrl, resourceName, httpMethod, role);
        }

        Resource savedResource = resourceRepository.save(resource);
        em.flush();
        return savedResource;
    }

    private Member createMemberIfNotFound(String email, String password, int age, Role... role) {
        consoleMessage("Member", email);
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            return member.get();
        }
        Member savedMember = memberRepository.save(Member.createMember(email, passwordEncoder.encode(password), age, role));
        em.flush();
        return savedMember;
    }

    private Role createRoleIfNotFound(RoleType roleType) {
        consoleMessage("Role", roleType.name());
        Optional<Role> role = roleRepository.findByRoleType(roleType);
        if (role.isPresent()) {
            return role.get();
        }
        Role savedRole = roleRepository.save(new Role(roleType));
        em.flush();
        return savedRole;
    }

    private void consoleMessage(String target, String message) {
        System.out.println("\n\n########################################");
        System.out.println(String.format("## [%s]의 초기화, 값: %s", target, message));
        System.out.println("########################################\n\n");
    }
}
