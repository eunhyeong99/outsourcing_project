package com.team24.outsourcing_project.domain.menu.entity;

import com.team24.outsourcing_project.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menus")
@NoArgsConstructor
public class Menu extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
