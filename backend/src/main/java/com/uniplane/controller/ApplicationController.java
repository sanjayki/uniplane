package com.uniplane.controller;

import com.uniplane.model.Application;
import com.uniplane.model.Project;
import com.uniplane.model.Tenant;
import com.uniplane.repository.ApplicationRepository;
import com.uniplane.repository.ProjectRepository;
import com.uniplane.repository.TenantRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apps")
public class ApplicationController {

    @Autowired
    private ApplicationRepository appRepo;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private TenantRepository tenantRepository;

    @GetMapping
    public List<Application> getAll(HttpServletRequest request) {
        Long tenantId = (Long) request.getAttribute("tenantId");
        return appRepo.findByProject_Tenant_Id(tenantId);
    }

    @PostMapping("/{projectId}")
    public Application create(HttpServletRequest request, @PathVariable Long projectId, @RequestBody Application app) {
        Long tenantId = (Long) request.getAttribute("tenantId");
        
        Project project = projectRepo.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));
            
        if (!project.getTenant().getId().equals(tenantId)) {
            throw new RuntimeException("Project does not belong to tenant");
        }
        
        app.setProject(project);
        return appRepo.save(app);
    }
}
