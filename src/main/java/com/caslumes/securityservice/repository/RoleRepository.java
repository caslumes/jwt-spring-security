package com.caslumes.securityservice.repository;

import com.caslumes.securityservice.model.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
