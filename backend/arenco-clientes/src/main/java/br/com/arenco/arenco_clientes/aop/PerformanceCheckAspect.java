package br.com.arenco.arenco_clientes.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class PerformanceCheckAspect {
    private static final int LOG_LEVEL_THRESHOLD = 2000;

    @Pointcut("execution(public * br.com.arenco.arenco_clientes.controllers..*(..))")
    public void aroundAllControllerMethodsPointCut() {}

    @Pointcut("execution(public * br.com.arenco.arenco_clientes.repositories.*.*(..))")
    public void aroundAllRepositoriesPointCut() {}

    @Around("aroundAllControllerMethodsPointCut() || aroundAllRepositoriesPointCut()")
    public Object logApiPerformance(final ProceedingJoinPoint pjp) throws Throwable {
        final var methodName = getMethodName(pjp);
        final long startTime = System.currentTimeMillis();

        try {
            return pjp.proceed();
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            if (duration > LOG_LEVEL_THRESHOLD) {
                log.warn("Finishing {} - Duration: {} ms", methodName, duration);
            } else {
                log.debug("Finishing {} - Duration: {} ms", methodName, duration);
            }
        }
    }

    private String getMethodName(final ProceedingJoinPoint pjp) {
        return pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
    }
}
