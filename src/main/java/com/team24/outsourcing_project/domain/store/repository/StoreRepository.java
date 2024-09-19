package com.team24.outsourcing_project.domain.store.repository;

import com.team24.outsourcing_project.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store,Long> {
}
