package com.team24.outsourcing_project.domain.user.repository;

import com.team24.outsourcing_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
