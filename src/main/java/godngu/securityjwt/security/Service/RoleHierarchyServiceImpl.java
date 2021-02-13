package godngu.securityjwt.security.Service;

import godngu.securityjwt.domain.repository.RoleHierarchyRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleHierarchyServiceImpl implements RoleHierarchyService {

    private final RoleHierarchyRepository roleHierarchyRepository;

    /**
     * 문자열 포맷
     * {부모 권한} > {자식권한} \n {자식 권한} > {손자 권한}
     * @return
     */
    @Override
    public String findAllHierarchyString() {
        return roleHierarchyRepository.findAll().stream()
            .filter(rh -> rh.getParent() != null)
            .map(rh -> roleHierarchyFormat(rh.getParent().getRoleType().name(), rh.getRoleType().name()))
            .collect(Collectors.joining("\n"));
    }

    private String roleHierarchyFormat(String parentRoleName, String chileRoleName) {
        return String.format("%s > %s", parentRoleName, chileRoleName);
    }
}
