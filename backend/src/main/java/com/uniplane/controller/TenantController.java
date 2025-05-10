package com.uniplane.controller;

import com.uniplane.model.Tenant;
import com.uniplane.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    @Autowired
    private TenantRepository tenantRepository;

    public TenantController() {
        System.out.println("✅ TenantController initialized");
    }

    @GetMapping
    public List<Tenant> getAll() {
        System.out.println("📥 Received GET /api/tenants");
        return tenantRepository.findAll();
    }

    @PostMapping
    public Tenant create(@RequestBody Tenant tenant) {
        return tenantRepository.save(tenant);
    }
}
