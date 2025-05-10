package com.uniplane.controller;

import com.uniplane.model.CloudProvider;
import com.uniplane.model.Tenant;
import com.uniplane.repository.CloudProviderRepository;
import com.uniplane.repository.TenantRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cloud-providers")
public class CloudProviderController {

    @Autowired
    private CloudProviderRepository cloudProviderRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @GetMapping
    public List<CloudProvider> getAll(HttpServletRequest request) {
        Long tenantId = (Long) request.getAttribute("tenantId");
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow();
        return cloudProviderRepository.findByTenant(tenant);
    }

    @PostMapping
    public CloudProvider createProvider(HttpServletRequest request, @RequestBody CloudProvider provider) {
        Long tenantId = (Long) request.getAttribute("tenantId");
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow();
        provider.setTenant(tenant);
        return cloudProviderRepository.save(provider);
    }
}
