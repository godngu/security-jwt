package godngu.securityjwt.domain.repository;

import godngu.securityjwt.domain.entity.RoleHierarchy;
import godngu.securityjwt.domain.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHierarchyRepository extends JpaRepository<RoleHierarchy, Long> {

    RoleHierarchy findByRoleType(RoleType roleType);
}
