package com.team24.outsourcing_project.domain.order.entity;

import com.team24.outsourcing_project.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
