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

    // Pointcut = zone observée : ici toutes les méthodes de l'app (package fr.esgi.avis..)
    // dont le nom commence par "i" (ex : init(), invokeSomething()).
    // On limite à notre code pour éviter de proxyfier des classes Spring internes.
    @Pointcut("execution(* fr.esgi.avis..i*(..))")
    public void methodsStartingWithI() {
    }

    // L’annotation @Around indique : "avant et après chaque méthode du pointcut,
    // exécute ce code pour tracer les appels".
    @Around("methodsStartingWithI()")
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
