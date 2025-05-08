package com.uniplane.repository;

import com.uniplane.model.CloudResource;
import com.uniplane.model.Project;
import com.uniplane.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CloudResourceRepository extends JpaRepository<CloudResource, Long> {
    List<CloudResource> findByProject(Project project);
    List<CloudResource> findByProject_Tenant(Tenant tenant);
    List<CloudResource> findByProject_Tenant_Id(Long tenantId);
}
