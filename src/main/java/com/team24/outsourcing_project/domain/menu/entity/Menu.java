package com.team24.outsourcing_project.domain.menu.entity;

import com.team24.outsourcing_project.domain.common.entity.Timestamped;
import com.team24.outsourcing_project.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menus")
@NoArgsConstructor
public class Menu extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String menuName;

    @Column(nullable = false)
    private int menuPrice;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

}
