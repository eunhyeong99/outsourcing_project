package com.team24.outsourcing_project.domain.store.repository;

import com.team24.outsourcing_project.domain.store.entity.Store;
import com.team24.outsourcing_project.domain.store.entity.StoreStatus;
import com.team24.outsourcing_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Store findByIdAndRole(Long id, StoreStatus role);

    List<Store> findAllByUserIdAndRole(Long userId, StoreStatus status);

    List<Store> findAllByRole(StoreStatus status);

}
