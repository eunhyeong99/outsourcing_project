package com.team24.outsourcing_project.aop;

import com.team24.outsourcing_project.domain.order.entity.Order;
import com.team24.outsourcing_project.domain.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Autowired
    private OrderRepository orderRepository;

    @Around(
            "execution(* com.team24.outsourcing_project.domain.order.service.OrderService.createOrder(..)) || "
                    + "execution(* com.team24.outsourcing_project.domain.order.service.OrderService.acceptOrder(..)) || "
                    + "execution(* com.team24.outsourcing_project.domain.order.service.OrderService.statusOrder(..))")
    public Object logOrderAction(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed(); // 메서드 실행

        Object[] args = joinPoint.getArgs();
        Long orderId = null;
        Long storeId = null;

        // 주문 생성 시
        if (joinPoint.getSignature().getName().equals("createOrder")) {
            storeId = (Long) args[1]; // storeId
        } else {
            orderId = (Long) args[0]; // orderId

            // Order 객체를 통해 Store ID 조회
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order != null) {
                storeId = order.getStore().getId(); // Order에서 Store ID 가져오기
            }
        }

        logger.info("요청 시각: {}, 가게 ID: {}, 주문 ID: {}",
                LocalDateTime.now(), storeId, orderId);

        return result; // 메서드 결과 반환
    }
}