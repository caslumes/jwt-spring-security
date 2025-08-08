package com.caslumes.securityservice.model.role;

import lombok.Getter;

@Getter
public enum RoleName {
    ROLE_USER("ROLE_USER"),
    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN");

    private final String roleName;

    RoleName(String roleName) {
        this.roleName = roleName;
    }

}
