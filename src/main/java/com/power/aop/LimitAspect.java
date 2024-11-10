package com.power.aop;


import com.power.annotation.MyLimit;
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

    @Pointcut("@annotation(com.power.annotation.MyLimit)")
    public void pt() {}

    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        log.info("限流开始!");
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        MyLimit classMyLimit = getClassMyLimit(joinPoint);
        MyLimit methodMyLimit = getMethodMyLimit(joinPoint);

        String remoteHost = request.getRemoteHost().replaceAll(":", ".");

        if (!whiteIPList.contains(remoteHost)) {
            String key = (Objects.nonNull(classMyLimit) ? classMyLimit.prefix() + ":" : "") + methodMyLimit.prefix() + ":" + remoteHost;
            int limit = methodMyLimit.limit();
            int windowSize = methodMyLimit.windowSize();
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


    private MyLimit getMethodMyLimit(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        MyLimit annotation = methodSignature.getMethod().getAnnotation(MyLimit.class);
        return annotation;
    }

    private MyLimit getClassMyLimit(ProceedingJoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getAnnotation(MyLimit.class);
    }
}
