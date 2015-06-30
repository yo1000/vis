package com.yo1000.vis.component.aop;

import com.yo1000.vis.model.service.RequestHistoryService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yoichi.kikuchi on 2015/06/25.
 */
@Aspect
@Component
public class CacheAdvisor {
    @Autowired
    private RequestHistoryService requestHistoryService;

    @Autowired
    private Cache.Store<String> cacheStore;

    @Around("execution(* com.yo1000.vis.controller.api.v1.*.*(..)) " +
            "&& @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    protected Object aroundRestController(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        String url = request.getRequestURL().toString();

        this.getRequestHistoryService().setHistory(url);

        String cacheParameter = request.getParameter("cache");
        if (cacheParameter != null && cacheParameter.equals("clear")) {
            this.getCacheStore().remove(url);
        }

        if (this.getCacheStore().exists(url)) {
            return this.getCacheStore().get(url);
        }

        Object returnValue = joinPoint.proceed();
        this.getCacheStore().set(url, returnValue);

        return returnValue;
    }

    protected RequestHistoryService getRequestHistoryService() {
        return requestHistoryService;
    }

    protected Cache.Store getCacheStore() {
        return cacheStore;
    }
}
