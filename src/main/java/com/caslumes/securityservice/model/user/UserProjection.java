package com.caslumes.securityservice.model.user;

import com.caslumes.securityservice.model.role.Role;

import java.util.Collection;

public interface UserProjection {
    Long getId();
    String getName();
    String getUsername();
    Collection<Role> getRoles();
}
