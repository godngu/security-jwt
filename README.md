
# 테스트 계정 (id/pw)
- admin@test.com / 1111
- manager@test.com / 1111
- user@test.com / 1111

# Resources
### permitAll
- POST /login
- GET /hello

### 인증 필요
- GET /api/hello

# classes
| class type | Login 처리 | Token 인증 |
|---|---|---|
| `filter` | LoginAuthenticationFilter | JwtAuthenticationFilter |
| `provider` | LoginAuthenticationProvider | JwtAuthenticationProvider |
| `authentication` | UsernamePasswordAuthenticationToken | JwtAuthenticationToken |
| `공통` | SecurityMemberContext ||


# 주의할점?
## 진입될 Filter와 처리할 Provider 결정하기
- filter는 SecurityConfig 에서 설정되며, RequestMatcher에 의해 url 패턴을 결정한다.
- provider의 supports 메소드에 정의된 token class를 기준으로 사용될 provider가 정해진다.
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
- 로그인 처리 완료, 토큰인증 처리 완료시 공통으로 사용한다.
- 이렇게 처리하는 이유는 SecurityContext에서 동일한 타입의 객체를 꺼내기 위함이다.
```java
SecurityMemberContext memberContext = (SecurityMemberContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
```
* 이렇게 설정을 하면 JPA에 의한 자동 audit 처리시 동일한 로직으로 memberId를 설정할 수 있다.  
> 자동 audit 처리란, insert/update 시 등록자/수정자에 대한 회원 id를 자동 세팅하는 기능이다.