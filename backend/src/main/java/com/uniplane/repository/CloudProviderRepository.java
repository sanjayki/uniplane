package com.uniplane.repository;

import com.uniplane.model.CloudProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.uniplane.model.Tenant;

public interface CloudProviderRepository extends JpaRepository<CloudProvider, Long> {
    List<CloudProvider> findByTenant(Tenant tenant);
}
