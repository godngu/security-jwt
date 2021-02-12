package godngu.securityjwt.security.common;

import godngu.securityjwt.security.Service.SecurityResourceService;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

@RequiredArgsConstructor
public class UrlResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private final SecurityResourceService securityResourceService;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourceMap;

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {
        if (MapUtils.isEmpty(resourceMap)) {
            init();
        }
        return resourceMap;
    }

    private void init() {
        this.resourceMap = securityResourceService.getResourceList();
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
