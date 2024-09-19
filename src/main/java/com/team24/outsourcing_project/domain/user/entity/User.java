package com.team24.outsourcing_project.domain.user.entity;

import com.team24.outsourcing_project.domain.common.entity.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
