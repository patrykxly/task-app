package zti.jira_project.aspects;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution of service and controller methods.
 */
@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Pointcut that matches all methods in the controllers and services packages.
     */
    @Pointcut("execution(* zti.jira_project.controllers.*.*(..)) || execution(* zti.jira_project.services.*.*(..))")
    public void loggingPointcut() {}

    /**
     * Advice that logs the result of method execution after the method successfully returns.
     *
     * @param result the result returned by the method
     */
    @AfterReturning(pointcut = "loggingPointcut()", returning = "result")
    public void logAfterReturning(Object result) {
        logger.info("Method executed successfully with result: " + result);
    }
}
