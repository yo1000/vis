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
    private Cache.Store cacheStore;

    @Around("execution(* com.yo1000.vis.controller.api.v1.*.*(..)) " +
            "&& @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    protected Object aroundRestController(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object[] signatureSources = new Object[args.length + 1];

        signatureSources[0] = joinPoint.getSignature().toLongString();
        System.arraycopy(args, 0, signatureSources, 1, args.length);

        Cache.Signature signature = new Cache.Signature(signatureSources);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

        this.getRequestHistoryService().setHistory(request.getRequestURL().toString());

        String cacheParameter = request.getParameter("cache");
        if (cacheParameter != null && cacheParameter.equals("clear")) {
            this.getCacheStore().remove(signature);
        }

        return joinPoint.proceed();
    }

    @Around("execution(* com.yo1000.vis.model.service.ChartService.*(..)) " +
            "&& @annotation(Cache)")
    protected Object aroundChartService(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object[] signatureSources = new Object[args.length + 1];

        signatureSources[0] = joinPoint.getSignature().toLongString();
        System.arraycopy(args, 0, signatureSources, 1, args.length);

        Cache.Signature signature = new Cache.Signature(signatureSources);

        if (this.getCacheStore().exists(signature)) {
            return this.getCacheStore().get(signature);
        }

        Object returnValue = joinPoint.proceed();
        this.getCacheStore().set(signature, returnValue);

        return returnValue;
    }

    protected RequestHistoryService getRequestHistoryService() {
        return requestHistoryService;
    }

    protected Cache.Store getCacheStore() {
        return cacheStore;
    }
}
