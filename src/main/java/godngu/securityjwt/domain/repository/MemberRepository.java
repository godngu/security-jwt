package godngu.securityjwt.domain.repository;

import godngu.securityjwt.domain.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select distinct m from Member m join fetch m.memberRoles mr join fetch mr.role r where m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);
}
