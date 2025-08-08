package com.caslumes.securityservice.repository;

import com.caslumes.securityservice.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
