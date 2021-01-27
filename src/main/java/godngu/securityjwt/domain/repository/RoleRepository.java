package godngu.securityjwt.domain.repository;

import godngu.securityjwt.domain.entity.Role;
import godngu.securityjwt.domain.entity.RoleType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleType(RoleType roleType);
}
