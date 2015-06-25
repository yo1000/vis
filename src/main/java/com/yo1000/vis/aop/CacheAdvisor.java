package com.yo1000.vis.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Created by yoichi.kikuchi on 2015/06/25.
 */
@Aspect
@Component
public class CacheAdvisor {
    private Cache.Store cacheStore = new Cache.Store();

    @Around("execution(* com.yo1000.vis.model.service.ChartService.*(..)) " +
            "&& @annotation(Cache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
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

    protected Cache.Store getCacheStore() {
        return cacheStore;
    }
}
