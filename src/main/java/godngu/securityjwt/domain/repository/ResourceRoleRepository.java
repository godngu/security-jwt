package godngu.securityjwt.domain.repository;

import godngu.securityjwt.domain.entity.ResourceRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResourceRoleRepository extends JpaRepository<ResourceRole, Long> {

    @Query("select r from ResourceRole r "
        + "join fetch r.resource resource "
        + "join fetch r.role role "
        + "order by resource.resourceUrl")
    List<ResourceRole> findAllResourceRole();
}
