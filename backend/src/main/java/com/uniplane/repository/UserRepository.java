package com.uniplane.repository;

import com.uniplane.model.User;
import com.uniplane.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByTenant(Tenant tenant);
}
