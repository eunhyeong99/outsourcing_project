package com.team24.outsourcing_project.domain.menu.entity;

import com.team24.outsourcing_project.domain.common.entity.Timestamped;
import com.team24.outsourcing_project.domain.store.entity.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "menus")
@NoArgsConstructor
@Getter
@Setter
public class Menu extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String menuName;

    @Column(nullable = false)
    private int menuPrice;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",nullable = false)
    private Store store;


    public void createMenu(Store store,String menuName, int menuPrice) {
        this.store = store;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }


    public void updateMenu(String menuName, int menuPrice) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }
}
