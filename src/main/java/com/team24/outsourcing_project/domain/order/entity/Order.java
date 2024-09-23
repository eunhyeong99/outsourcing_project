package com.team24.outsourcing_project.domain.order.entity;

import com.team24.outsourcing_project.domain.common.entity.Timestamped;
import com.team24.outsourcing_project.domain.menu.entity.Menu;
import com.team24.outsourcing_project.domain.store.entity.Store;
import com.team24.outsourcing_project.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Getter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Order(final User user, final Store store, final Menu menu, final OrderStatus status) {
        this.user = user;
        this.store = store;
        this.menu = menu;
        this.status = status;
    }

    public static Order create(final User user, final Store store, final Menu menu,
            final OrderStatus status) {
        return new Order(user, store, menu, status);
    }

    public void statusDelivering() {
        this.status = OrderStatus.DELIVERING;
    }

    public void statusCompleted() {
        this.status = OrderStatus.COMPLETED;
    }

    public void accept() {
        this.status = OrderStatus.ACCEPTED;
    }

    public boolean isSameStatus(final OrderStatus status) {
        return this.status == status;
    }
}
