package com.team24.outsourcing_project.domain.review.repository;

import com.team24.outsourcing_project.domain.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review,Long>{

    List<Review> findAllByOrderId(final long orderId);

    boolean existsByUserIdAndOrderId(final long userId, final long orderId);

    @Query("SELECT r " +
            "FROM Review r " +
            "LEFT JOIN r.order o " +
            "WHERE o.store.id = :storeId AND r.score BETWEEN :lowerBound AND :upperBound " +
            "ORDER BY r.createdAt DESC")
    List<Review> findAllByStoreId(
            @Param("storeId") final long storeId,
            @Param("lowerBound") final int lowerBound,
            @Param("upperBound") final int upperBound);
}
