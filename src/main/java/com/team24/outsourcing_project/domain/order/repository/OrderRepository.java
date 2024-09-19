package com.team24.outsourcing_project.domain.order.repository;

import com.team24.outsourcing_project.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Long, Order> {
}
