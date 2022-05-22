package cn.imokkkk.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author ImOkkkk
 * @date 2022/5/22 22:44
 * @since 1.0
 */
@Aspect
@Component
@Slf4j
public class PerformanceAspect {

  @Around("repositoryOps()")
//  @Around("execution(* cn.imokkkk.repository..*(..))")
  public Object logPerformance(ProceedingJoinPoint pjp) throws Throwable {
    long startTime = System.currentTimeMillis();
    String name = "-";
    String result = "Y";
    try {
      name = pjp.getSignature().toShortString();
      return pjp.proceed();
    } catch (Throwable e) {
      result = "N";
      throw e;
    } finally {
      long endTime = System.currentTimeMillis();
      log.info("{};{};{}ms", name, result, endTime - startTime);
    }
  }

  @Pointcut("execution(* cn.imokkkk.repository..*(..))")
  private void repositoryOps() {}
}
