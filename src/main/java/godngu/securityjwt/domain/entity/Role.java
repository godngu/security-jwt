package godngu.securityjwt.domain.entity;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_type")
    @Enumerated(value = STRING)
    private RoleType roleType;

    @Column(name = "role_desc")
    private String roleDescription;

    @Builder
    public Role(RoleType roleType) {
        this.roleType= roleType;
        this.roleDescription = roleType.getDescription();
    }
}
