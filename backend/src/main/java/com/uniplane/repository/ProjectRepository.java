package com.uniplane.repository;

import com.uniplane.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.uniplane.model.Tenant;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByTenant(Tenant tenant);
}
