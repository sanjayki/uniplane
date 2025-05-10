package com.uniplane.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "stack_deployments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StackDeployment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String status; // CREATING, READY, FAILED

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String projectId;

    @Column(length = 4096)
    private String renderedYamlPaths;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    // Getters and Setters
}

