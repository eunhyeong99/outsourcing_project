package com.team24.outsourcing_project.domain.store.entity;

import com.team24.outsourcing_project.domain.common.entity.Timestamped;
import com.team24.outsourcing_project.domain.menu.entity.Menu;
import com.team24.outsourcing_project.domain.order.entity.Order;
import com.team24.outsourcing_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;


@Table(name = "stores")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int minOrderPrice;

    @Enumerated(EnumType.STRING)
    private StoreRoleEnum role;

    @Column(nullable = false)
    private Time openTime;

    @Column(nullable = false)
    private Time closeTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false)
    private User user;

    @Getter
    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Menu> menus;

    private Store(final String name, final int minOrderPrice, final StoreRoleEnum role, final Time openTime, final Time closeTime, final User user)
    {
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.role = role;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.user = user;
    }
    private static Store createStore(final String name, final int minOrderPrice, final StoreRoleEnum role, final Time openTime, final Time closeTime, final User user)
    {
        return new Store(name,minOrderPrice,role,openTime,closeTime,user);
    }


}
