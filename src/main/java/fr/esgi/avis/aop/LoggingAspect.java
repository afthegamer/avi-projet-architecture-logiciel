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

    // Pointcut = zone observée : ici toutes les méthodes du HelloController.
    // Cela veut dire : chaque appel d’une méthode du HelloController passera
    // par le code ci-dessous (logAround) pour qu’on puisse écrire un log.
    @Pointcut("within(fr.esgi.avis.controller.HelloController)")
    public void editeurControllerPointcut() {
    }

    // L’annotation @Around indique : "avant et après chaque méthode du pointcut,
    // exécute ce code pour tracer les appels".
    @Around("editeurControllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            Object result = joinPoint.proceed();
            // On écrit un log avec le nom de la méthode, ses arguments et le résultat retourné.
            log.info("Invocation {} avec arguments {} : ", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
            System.out.println("Invocation : " + joinPoint.getSignature().getName() + "() argument[s] = "
                    + Arrays.toString(joinPoint.getArgs())
                    + " resultat = " + result);
            return result;
        } catch (IllegalArgumentException e) {
            // Si une IllegalArgumentException est levée, on la logge aussi.
            log.error("Exception levée : {} dans {}()", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().getName());
            System.err.println("Exception levée : " + e.getMessage());
            throw e;
        }
    }

}
