package com.caslumes.securityservice.api;

import lombok.Data;

@Data
class RoleToUserForm {
    private String username;
    private String rolename;
}
