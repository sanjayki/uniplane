package com.uniplane.repository;

import com.uniplane.model.Application;
import com.uniplane.model.Project;
import com.uniplane.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByProject(Project project);
    List<Application> findByProject_Tenant(Tenant tenant);
    List<Application> findByProject_Tenant_Id(Long tenantId);
}
