package godngu.securityjwt.domain.repository;

import godngu.securityjwt.domain.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Resource findByResourceUrl(String resourceUrl);
}
