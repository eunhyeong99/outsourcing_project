package com.team24.outsourcing_project.domain.order.entity;

import com.team24.outsourcing_project.domain.common.entity.Timestamped;
import com.team24.outsourcing_project.domain.menu.entity.Menu;
import com.team24.outsourcing_project.domain.store.entity.Store;
import com.team24.outsourcing_project.domain.user.entity.User;
import com.team24.outsourcing_project.domain.user.entity.UserRoleEnum;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    private Order(final User user, final Store store, final Menu menu, final OrderStatusEnum status) {
        this.user = user;
        this.store = store;
        this.menu = menu;
        this.status = status;
    }

    public static Order create(final User user, final Store store, final Menu menu, final OrderStatusEnum status) {
        return new Order(user, store, menu, status);
    }
}
