package com.imooc.aspet;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

@Component
@Aspect
public class ServiceLogAspect {
    public static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);
    /**
     * AOP通知
     * 1. 前置通知：调用方法之前执行；
     * 2. 后置通知：调用方法之后执行；
     * 3. 环绕通知：调用方法之前和之后，都分别执行可执行的通知；
     * 4. 异常通知：如果在调用方法发生异常，则通知；
     * 5. 最终通知：调用方法之后，无论是否发生异常都执行通知
     */

    /**
     * 切面表达式
     * execution 代表所要执行的表达式主体
     * 第一处*代表方法返回类型，*代表所有
     * 第二处 包名，代表AOP所要监控的类所在的包
     * 第三处 ..代表该包及其子包下所有类的方法
     * 第四处 代表类名，*代表所有类
     * 第五处 *（..） *代表所有方法，(..)代表所有参赛类型
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */

    @Around("execution(* com.imooc.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("========== 开始执行 {},{} ========",joinPoint.getTarget().getClass(),joinPoint.getSignature().getName());

        //记录开始时间
        long begin = System.currentTimeMillis();

        Object res = joinPoint.proceed();

        //记录结束时间
        long end = System.currentTimeMillis();

        long takeTime = end - begin;

        if (takeTime > 3000) {
            logger.error("=========执行结束，耗时 {} 毫秒===========",takeTime);
        } else if (takeTime > 2000) {
            logger.warn("=========执行结束，耗时 {} 毫秒===========",takeTime);
        } else {
            logger.info("=========执行结束，耗时 {} 毫秒===========",takeTime);
        }

        return res;
    }
}
