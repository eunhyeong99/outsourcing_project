package com.team24.outsourcing_project.domain.review.entity;

import com.team24.outsourcing_project.domain.common.entity.Timestamped;
import com.team24.outsourcing_project.domain.order.entity.Order;
import com.team24.outsourcing_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private int score;

    private Review(final Order order, final User user, final String content, final int score) {
        this.order = order;
        this.user = user;
        this.content = content;
        this.score = score;
    }

    public static Review create(final Order order, final User user, final String content, final int score) {
        return new Review(order, user, content, score);
    }
}
