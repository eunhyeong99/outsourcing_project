package com.team24.outsourcing_project.domain.store.entity;

import com.team24.outsourcing_project.domain.common.entity.Timestamped;
import jakarta.persistence.*;


@Table(name = "stores")
@Entity
public class Store extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
