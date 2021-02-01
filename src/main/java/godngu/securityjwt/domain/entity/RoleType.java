package godngu.securityjwt.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ROLE_ADMIN("관리자"),
    ROLE_MANAGER("매니저"),
    ROLE_USER("사용자"),
    ROLE_REFRESH("토큰재발급"),
    UNKNOWN("알수없음")
    ;

    private final String description;
}
