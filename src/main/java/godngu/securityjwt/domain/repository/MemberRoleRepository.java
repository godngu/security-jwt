package godngu.securityjwt.domain.repository;

import godngu.securityjwt.domain.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

}
