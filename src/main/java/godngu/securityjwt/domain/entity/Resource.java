package godngu.securityjwt.domain.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

@Entity
@Table(name = "resource")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Resource {

    @Id
    @GeneratedValue
    @Column(name = "resource_id")
    private Long id;

    @Column(name = "resource_url")
    private String resourceUrl;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "http_method")
    @Enumerated(value = STRING)
    private HttpMethod httpMethod;

    @OneToMany(mappedBy = "resource", cascade = ALL, orphanRemoval = true)
    private Set<ResourceRole> resourceRoles = new HashSet<>();

    private Resource(String resourceUrl, String resourceName, HttpMethod httpMethod, Role... roles) {
        this.resourceUrl = resourceUrl;
        this.resourceName = resourceName;
        this.httpMethod = httpMethod;
        for (Role role : roles) {
            resourceRoles.add(ResourceRole.create(this, role));
        }
    }

    public static Resource create(String resourceUrl, String resourceName, HttpMethod httpMethod, Role... roles) {
        return new Resource(resourceUrl, resourceName, httpMethod, roles);
    }
}
