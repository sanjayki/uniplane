package com.uniplane.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cloud_resources")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CloudResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // e.g., "ec2", "rds", "s3", "vpc"

    private String status;

    @Column(columnDefinition = "TEXT")
    private String configYaml; // Crossplane YAML config
}
