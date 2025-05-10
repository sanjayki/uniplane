package com.uniplane.controller;

import com.uniplane.model.Project;
import com.uniplane.model.Tenant;
import com.uniplane.repository.ProjectRepository;
import com.uniplane.repository.TenantRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @GetMapping
    public List<Project> getAll(HttpServletRequest request) {
        Long tenantId = (Long) request.getAttribute("tenantId");
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow();
        return projectRepository.findByTenant(tenant);
    }

    @PostMapping
    public Project createProject(HttpServletRequest request, @RequestBody Project project) {
        Long tenantId = (Long) request.getAttribute("tenantId");
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow();
        project.setTenant(tenant);
        return projectRepository.save(project);
    }
}
