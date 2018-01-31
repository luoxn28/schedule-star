package com.schedule.star.admin.aspect;

import com.schedule.star.admin.util.WebResultUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author xiangnan
 * @date 2018/1/31 22:39
 */
@Aspect
@Component
@Order(1)
public class WebResultAspect {

    @Around("execution(public * com.schedule.star.admin.controller.ExecutorController.*(..))")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            return WebResultUtil.success(result);
        } catch (Exception e) {
            // 返回异常信息
            return WebResultUtil.error(-1, e.getMessage());
        }
    }

}
