package godngu.securityjwt.security.access;

import io.jsonwebtoken.lang.Assert;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class SkipPathRequestMatcher implements RequestMatcher {

    private OrRequestMatcher skipMatchers;
    private RequestMatcher processingMatcher;

    public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {
        Assert.notNull(pathsToSkip);
        List<RequestMatcher> requestMatchers = pathsToSkip.stream()
            .map(path -> new AntPathRequestMatcher(path))
            .collect(Collectors.toList());
        skipMatchers = new OrRequestMatcher(requestMatchers);
        processingMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (skipMatchers.matches(request)) {
            return false;
        }
        return processingMatcher.matches(request);
    }
}
