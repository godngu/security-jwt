package godngu.securityjwt.security.Service;

import godngu.securityjwt.domain.entity.ResourceRole;
import godngu.securityjwt.domain.repository.ResourceRoleRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityResourceService {

    private final ResourceRoleRepository resourceRoleRepository;

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();

        List<ResourceRole> resourceRoles = resourceRoleRepository.findAllResourceRole();
        resourceRoles.forEach(resourceRole -> {
            List<ConfigAttribute> configAttributeList = new ArrayList<>();
            configAttributeList.add(new SecurityConfig(resourceRole.getRole().getRoleType().name()));
            result.put(new AntPathRequestMatcher(resourceRole.getResource().getResourceUrl()), configAttributeList);
        });
        return result;
    }

}
