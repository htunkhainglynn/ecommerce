package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
