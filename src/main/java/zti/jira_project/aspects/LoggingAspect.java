package zti.jira_project.aspects;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* zti.jira_project.controllers.*.*(..)) || execution(* zti.jira_project.services.*.*(..))")
    public void loggingPointcut() {}

    @AfterReturning(pointcut = "loggingPointcut()", returning = "result")
    public void logAfterReturning(Object result) {
        logger.info("Method executed successfully with result: " + result);
    }
}
