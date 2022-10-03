package com.example.checkcheck.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
/**
 * API 모든 컨트롤러의 실행 속도를 측정할 수 있는 클래스*
 */
public class ExecutionCapacityTimer {

    //Controller로 성능 테스트
    @Around("bean(*Controller)")
    public Object AssumeExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object proceed = joinPoint.proceed(); // 조인포인트의 메서드 실행
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.info("실행 메서드: {}, 실행시간 = {}ms", methodName, totalTimeMillis);
        if(totalTimeMillis > 500) {
            log.info("많은 성능 개선이 필요함 : {}", methodName);
        }else if(499 > totalTimeMillis && totalTimeMillis > 200){
            log.info("조금 성능 개선이 필요함 : {}", methodName);
        }else
        log.info("만족할 성능을 가지고 있음 : {}", methodName);

        return proceed;
    }
}
