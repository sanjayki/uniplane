package com.uniplane.controller;

import com.uniplane.model.StackDeployment;
import com.uniplane.repository.StackDeploymentRepository;
import com.uniplane.service.StackDeploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stack")
public class StackDeploymentController {

    @Autowired
    private StackDeploymentService deploymentService;

    @Autowired
    private StackDeploymentRepository repository;

    @PostMapping("/deploy")
    public ResponseEntity<Map<String, Object>> deployStack(
            @RequestAttribute("tenantId") Long tenantId,
            @RequestAttribute(name = "projectId", required = false) Long projectId,
            @RequestBody Map<String, Object> request) {
        String stackName = (String) request.get("stackName");
        Map<String, String> values = (Map<String, String>) request.get("values");
        StackDeployment deployment = new StackDeployment();
        deployment.setName(stackName);
        deployment.setStatus("CREATING");
        deployment.setTenantId(tenantId.toString());
        deployment.setProjectId((projectId != null) ? projectId.toString() : "default-project");
        deployment.setCreatedAt(Instant.now());
        deployment.setUpdatedAt(Instant.now());
        repository.save(deployment);

        deploymentService.deployStackAsync(deployment.getId(), values, stackName);

        Map<String, Object> response = new HashMap<>();
        response.put("id", deployment.getId());
        response.put("status", "CREATING");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> getStatus(
            @PathVariable Long id,
            @RequestAttribute("tenantId") Long tenantId) {

        StackDeployment deployment = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Deployment not found"));

        if (!deployment.getTenantId().equals(tenantId.toString())) {
            throw new RuntimeException("Deployment does not belong to tenant");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("status", deployment.getStatus());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteStack(@PathVariable Long id) {
        deploymentService.deleteStackAsync(id);
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("status", "DELETING");
        return ResponseEntity.ok(response);
    }
}
