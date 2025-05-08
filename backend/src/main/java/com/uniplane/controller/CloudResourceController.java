package com.uniplane.controller;

import com.uniplane.model.CloudResource;
import com.uniplane.model.Project;
import com.uniplane.model.Tenant;
import com.uniplane.repository.CloudResourceRepository;
import com.uniplane.repository.ProjectRepository;
import com.uniplane.repository.TenantRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class CloudResourceController {

    @Autowired
    private CloudResourceRepository resourceRepo;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private TenantRepository tenantRepository;

    @GetMapping
    public List<CloudResource> getAll(HttpServletRequest request) {
        Long tenantId = (Long) request.getAttribute("tenantId");
        return resourceRepo.findByProject_Tenant_Id(tenantId);
    }

    @PostMapping("/{projectId}")
    public CloudResource create(HttpServletRequest request, @PathVariable Long projectId, @RequestBody CloudResource resource) {
        Long tenantId = (Long) request.getAttribute("tenantId");
        
        Project project = projectRepo.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));
            
        if (!project.getTenant().getId().equals(tenantId)) {
            throw new RuntimeException("Project does not belong to tenant");
        }
        
        resource.setProject(project);
        return resourceRepo.save(resource);
    }
}
