package fr.esgi.avis.aop;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@AllArgsConstructor
@Log4j2
@Component
public class LoggingAspect {

    // Toutes les méthodes de la classe HelloController (EditeurController)
    @Pointcut("within(fr.esgi.avis.controller.HelloController)")
    public void editeurControllerPointcut() {
    }

    @Around("editeurControllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            Object result = joinPoint.proceed();
            log.info("Invocation {} avec arguments {} : ", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
            System.out.println("Invocation : " + joinPoint.getSignature().getName() + "() argument[s] = "
                    + Arrays.toString(joinPoint.getArgs())
                    + " resultat = " + result);
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Exception levée : {} dans {}()", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().getName());
            System.err.println("Exception levée : " + e.getMessage());
            throw e;
        }
    }

}
