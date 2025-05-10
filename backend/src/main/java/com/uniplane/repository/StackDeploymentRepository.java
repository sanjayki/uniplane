package com.uniplane.repository;

import java.util.List;

import com.uniplane.model.StackDeployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StackDeploymentRepository extends JpaRepository<StackDeployment, Long> {
    List<StackDeployment> findByTenantId(String tenantId);
}

