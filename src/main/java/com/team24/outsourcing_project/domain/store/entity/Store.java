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
@Getter
public class Store extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int minOrderPrice;

    @Column(nullable = false)
    private boolean isOwner;

    @Enumerated(EnumType.STRING)
    private StoreStatus role;

    @Column(nullable = false)
    private Time openTime;

    @Column(nullable = false)
    private Time closeTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "store")
    private List<Order> orderList;

    @Getter
    @OneToMany(mappedBy = "store")
    private List<Menu> menuList;


    private Store(final String name, final int minOrderPrice, final boolean isOwner, final StoreStatus role, final Time openTime, final Time closeTime,
                  final User user)
    {
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.isOwner = isOwner;
        this.role = role;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.user = user;

    }

    public static Store createStore(final String name, final int minOrderPrice, final boolean isOwner, final StoreStatus role, final Time openTime, final Time closeTime,
                                     final User user)
    {
        return new Store(name,minOrderPrice,isOwner,role,openTime,closeTime,user);
    }
    public void update(String name, int minOrderPrice,Time openTime, Time closeTime)
    {
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;

    }
    public void changeStatus(final StoreStatus newStatus)
    {
        this.role = newStatus;
    }
}
