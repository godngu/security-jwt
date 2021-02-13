package godngu.securityjwt.domain.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role_hierarchy")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class RoleHierarchy {

    @Id
    @GeneratedValue
    @Column(name = "role_hierarchy_id")
    private Long id;

    @Column(name = "role_type")
    @Enumerated(value = STRING)
    private RoleType roleType;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "parent_id")
    private RoleHierarchy parent;

    @OneToMany(mappedBy = "parent", cascade = ALL)
    private Set<RoleHierarchy> children = new HashSet<>();

    public RoleHierarchy(RoleType roleType) {
        this.roleType = roleType;
    }

    public void setParent(RoleHierarchy parent) {
        this.parent = parent;
    }
}
