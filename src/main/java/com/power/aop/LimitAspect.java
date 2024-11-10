package com.power.aop;


import com.power.annotation.IPLimit;
import com.power.domain.vo.Result;
import com.power.limit.SlidingWindowRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class LimitAspect {
    @Autowired
    private SlidingWindowRateLimiter slidingWindowRateLimiter;

    @Value("${me.whiteIPList}")
    private List<String> whiteIPList;

    @Pointcut("@annotation(com.power.annotation.IPLimit)")
    public void pt() {}

    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        log.info("限流开始!");
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        IPLimit classIPLimit = getClassMyLimit(joinPoint);
        IPLimit methodIPLimit = getMethodMyLimit(joinPoint);

        String remoteHost = request.getRemoteHost().replaceAll(":", ".");

        if (!whiteIPList.contains(remoteHost)) {
            String key = (Objects.nonNull(classIPLimit) ? classIPLimit.prefix() + ":" : "") + methodIPLimit.prefix() + ":" + remoteHost;
            int limit = methodIPLimit.limit();
            int windowSize = methodIPLimit.windowSize();
            Boolean access = slidingWindowRateLimiter.tryAcquire(key, limit, windowSize);
            if (!access) {
                log.info("限流未通过!");
                log.info("限流key=>{}", key);
                return Result.error("501", "操作次数太多了，请稍后重试！");
            }
        }

//        log.info("限流通过!");
        return joinPoint.proceed();
    }


    private IPLimit getMethodMyLimit(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        IPLimit annotation = methodSignature.getMethod().getAnnotation(IPLimit.class);
        return annotation;
    }

    private IPLimit getClassMyLimit(ProceedingJoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getAnnotation(IPLimit.class);
    }
}
