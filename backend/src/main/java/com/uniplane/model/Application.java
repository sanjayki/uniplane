package com.uniplane.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "applications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(nullable = false)
    private String name;

    private String type; // "java", "python", "agent", etc.

    private String status;

    @Column(columnDefinition = "TEXT")
    private String deployYaml; // Ansible playbook

    @ManyToMany
    @JoinTable(
        name = "application_resources",
        joinColumns = @JoinColumn(name = "application_id"),
        inverseJoinColumns = @JoinColumn(name = "cloud_resource_id")
    )
    private Set<CloudResource> linkedResources;
}
