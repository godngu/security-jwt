package godngu.securityjwt.domain.entity;

import static godngu.securityjwt.domain.entity.MemberRole.createMemberRole;
import static javax.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "member")
@Getter
@ToString(of = {"id", "email", "age"})
@NoArgsConstructor(access = PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", length = 50, unique = true)
    @NotEmpty
    private String email;

    @JsonIgnore
    @Column(name = "password")
    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    @Column(name = "age")
    @Positive
    private int age;

    @Column(name = "refresh_jti")
    private String jti;

    @OneToMany(mappedBy = "member", cascade = ALL)
    @NotNull
    private Set<MemberRole> memberRoles = new HashSet<>();

    private Member(String email, String password, int age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public static Member createMember(String email, String password, int age, Role... roles) {
        // TODO: validation 필요
        Member member = new Member(email, password, age);
        for (Role role : roles) {
            MemberRole memberRole = createMemberRole(member, role);
            member.getMemberRoles().add(memberRole);
        }
        return member;
    }

    public void login(String jti) {
        this.jti = jti;
    }

    public void logout() {
        this.jti = null;
    }
}
