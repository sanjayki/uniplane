package com.uniplane.controller;

import com.uniplane.model.User;
import com.uniplane.model.Tenant;
import com.uniplane.repository.TenantRepository;
import com.uniplane.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @GetMapping
    public List<User> getAll(HttpServletRequest request) {
        Long tenantId = (Long) request.getAttribute("tenantId");
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow();
        return userRepository.findByTenant(tenant);
    }

    @PostMapping
    public User createUser(HttpServletRequest request, @RequestBody User user) {
        Long tenantId = (Long) request.getAttribute("tenantId");
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow();
        user.setTenant(tenant);
        return userRepository.save(user);
    }
}
