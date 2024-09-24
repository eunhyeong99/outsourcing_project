package com.team24.outsourcing_project.aop;

import com.team24.outsourcing_project.domain.order.entity.Order;
import com.team24.outsourcing_project.domain.order.repository.OrderRepository;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private OrderRepository orderRepository;

    @Around(
            "execution(* com.team24.outsourcing_project.domain.order.service.OrderService.createOrder(..)) || "
                    + "execution(* com.team24.outsourcing_project.domain.order.service.OrderService.acceptOrder(..)) || "
                    + "execution(* com.team24.outsourcing_project.domain.order.service.OrderService.statusOrder(..))")
    public Object logOrderAction(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Long orderId = null;
        Long storeId = null;

        Object[] args = joinPoint.getArgs();

        // 메서드 실행 전
        if (joinPoint.getSignature().getName().equals("createOrder")) {
            Long userId = (Long) args[0];
            storeId = (Long) args[1];
            Long menuId = (Long) args[2];

            // 메서드 실행
            result = joinPoint.proceed();

            // 생성된 주문 ID 가져오기
            orderId = ((Order) result).getId(); // 생성된 주문 객체에서 ID 추출
        } else {
            // 수락 및 상태 변경 처리
            orderId = (Long) args[0];
            storeId = orderRepository.findById(orderId)
                    .map(order -> order.getStore().getId())
                    .orElse(null);

            // 메서드 실행
            result = joinPoint.proceed();
        }

        // 로그 출력
        log.info("요청 시각: {}, 가게 ID: {}, 주문 ID: {}",
                LocalDateTime.now(), storeId, orderId);

        return result; // 메서드 결과 반환
    }
}