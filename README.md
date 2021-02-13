
# 테스트 계정 (id/pw)
- admin@test.com / 1111
- manager@test.com / 1111
- user@test.com / 1111

# 테스트 주의사항
- 테스트데이터 초기화 로직
    - `DataInitializer.java`
- 어플리케이션이 시작할 때 리소스권한 정보를 가져옵니다.
    - 만약 application.yml 에서 `ddl_auto: create`로 되어 있다면 DB 테이블이 비어있어서 url 권한검사가 동작하지 않습니다.
    - 이 경우 `ddl_auto: update`로 설정한 후 최초 1회 서버를 기동하여 DB 데이터 초기화를 한 후에 테스트 해야 합니다.

# Resources
### permitAll
- GET /
- GET /hello
- POST /login

### 인증 필요
| Method | Resource | Role |
| --- | --- | --- |
| GET | /api/hello/admin | ROLE_ADMIN |
| GET | /api/hello/manager | ROLE_MANAGER |
| GET | /api/hello/user | ROLE_USER |


# 주요 인증 classes
| class type | Login 처리 | Token 인증 |
|---|---|---|
| `filter` | LoginAuthenticationFilter | JwtAuthenticationFilter |
| `provider` | LoginAuthenticationProvider | JwtAuthenticationProvider |
| `authentication` | UsernamePasswordAuthenticationToken | JwtAuthenticationToken |
| `공통` | SecurityMemberContext ||


# 주의할점?
## 진입될 Filter와 처리할 Provider 결정하기
- filter는 SecurityConfig 에서 설정되며, RequestMatcher에 의해 url 패턴을 결정합니다.
- provider의 supports 메소드에 정의된 token class를 기준으로 사용될 provider가 정해집니다.
```java
// JwtAuthenticationFilter 에서 JwtAuthenticationToken 설정
@Override
public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException, IOException, ServletException {

    String token = extractToken(request);
    return this.getAuthenticationManager().authenticate(
        new JwtAuthenticationToken(token));
}
```

```java
// JwtAuthenticationProvider 에서 JwtAuthenticationToken.class 설정
@Override
public boolean supports(Class<?> authentication) {
    return authentication.equals(JwtAuthenticationToken.class);
}
```

## SecurityMemberContext 공통 사용
- 로그인 처리 완료, 토큰인증 처리 완료시 공통으로 사용합니다.
- 이렇게 처리하는 이유는 SecurityContext에서 동일한 타입의 객체를 꺼내기 위함입니다.
```java
SecurityMemberContext memberContext = (SecurityMemberContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
```
* 이렇게 설정을 하면 JPA에 의한 자동 audit 처리시 동일한 로직으로 memberId를 설정할 수 있습니다.  
> 자동 audit 처리란, insert/update 시 등록자/수정자에 대한 회원 id를 자동 세팅하는 기능입니다.


# DB를 통한 URL 기반의 권한관리
- `FilterInvocationSecurityMetadataSource` 를 구현합니다.

## 기본 설정

### `SecurityResourceService`
- `ResourceRole` 테이블에서 리소스권한을 조회하여 `LinkedHashMap<RequestMatcher, List<ConfigAttribute>>`으로 리턴합니다.

### `UrlResourcesMapFactoryBean`
- 어플리케이션이 기동될 때 DB로부터 리소스권한 정보(RESOURCE_ROLE)를 조회하여 `LinkedHashMap`에 담아둡니다.

### `UrlFilterInvocationSecurityMetadataSource`
- 리소스권한 정보가 조회된 `LinkedHashMap`을 대상으로 resource 요청이 있을 경우 권한 검사를 수행합니다.
- 자원 접근에 대한 판단은 `AccessDecisionManager`의 구현체인 `AffirmativeBased`를 사용합니다.

### 문제점
- Application 로딩시 DB로 부터 리소스와 권한 관계를 조회하여 `LinkedHashMap`에 담아두게 됩니다.
    - 만약 DB 정보가 변경 된다면 `Map`에 있는 데이터는 어떻게 변경해야 하는가?
        - 대안1. `reload` 메소드를 구현하고, DB값 변경시 데이터를 재적재 합니다.
            - 서버가 이중화 되어 있다면 어떻게 전파시키지?
        - 대안2. 모든 url 호출시 DB를 조회합니다.
            - 성능에 얼마나 영향을 줄까?
        - 대안3. 적절할 `cache`의 활용

## 계층 권한 적용
###  권한 계층
- ADMIN > MANAGER > USER
- MANAGER 로그인
    - USER 리소스: O
    - ADMIN 리소스: X (403 Forbidden)

### `RoleHierarchyServiceImpl`
- 반드시 아래와 같은 포맷의 문자열로 반환 되어야 한다.
```text
{부모 권한명} > {자식 권한명}  
{자식 권한명} > {손자 권한명}  
ROLE_ADMIN > ROLE_MANAGER  
\nROLE_MANAGER > ROLE_USER
```
### `SecurityInitializer`
- 계층 권한이 적용되는 시점을 `ApplicationRunner`에 설정한다.
