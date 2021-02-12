package godngu.securityjwt.domain.entity;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resource_role")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ResourceRole {

    @Id
    @GeneratedValue
    @Column(name = "resource_role_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    private ResourceRole(Role role, Resource resource) {
        this.role = role;
        this.resource = resource;
    }

    public static ResourceRole create(Resource resource, Role role) {
        return new ResourceRole(role, resource);
    }
}
