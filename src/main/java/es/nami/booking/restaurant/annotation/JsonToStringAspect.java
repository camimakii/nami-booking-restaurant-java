package es.nami.booking.restaurant.annotation;

import es.nami.booking.restaurant.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class JsonToStringAspect {

    // This Aspect is more for learning purpose than be really useful
    // It overrides the toString method of annotated classes to print it in a pretty Json
    @Around("execution(String toString()) && @within(JsonToString)")
    public Object jsonToStringAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return JsonUtil.toJson(joinPoint.getTarget());
        } catch (Exception ex) {
            log.error("Error when toString of class {}", joinPoint.getTarget().getClass().getSimpleName());
            return joinPoint.toString();
        }
    }
}
