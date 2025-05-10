package com.uniplane.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cloud_providers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CloudProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(nullable = false)
    private String name; // e.g., "AWS Prod", "OCI Test"

    @Column(nullable = false)
    private String providerType; // "aws", "gcp", "oci", "azure"

    @Column(columnDefinition = "TEXT")
    private String credentialsJson; // Encrypted, if needed
}
