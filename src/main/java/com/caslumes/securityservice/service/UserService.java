package com.caslumes.securityservice.service;

import com.caslumes.securityservice.model.role.Role;
import com.caslumes.securityservice.model.user.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    void deleteUser(Long userId);
    Role saveRole(Role role);
    void deleteRole(Long roleId);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();
}
