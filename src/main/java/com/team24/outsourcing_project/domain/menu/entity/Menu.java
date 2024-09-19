package com.team24.outsourcing_project.domain.menu.entity;

import com.team24.outsourcing_project.domain.common.entity.Timestamped;
import com.team24.outsourcing_project.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private Menu(final String menuName, final int menuPrice, final Store store) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.store = store;
    }

    public static Menu create(final String menuName, final int menuPrice, final Store store) {
        return new Menu(menuName, menuPrice, store);
    }
}
