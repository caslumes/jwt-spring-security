package com.caslumes.securityservice.service;

import com.caslumes.securityservice.model.role.Role;
import com.caslumes.securityservice.model.user.User;
import com.caslumes.securityservice.repository.RoleRepository;
import com.caslumes.securityservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            log.error("User not found in the DB: {}.", username);
            throw new UsernameNotFoundException(String.format("User not found in the DB: %s.", username));
        }

        log.info("User found in the DB: {}.", username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} to the DB.", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()){
            log.error("User with id {} not found in the DB.", userId);
            throw new UsernameNotFoundException(String.format("User with id %d not found in the DB.", userId));
        }

        log.info("Deleting user {} from the DB.", user.get().getName());
        userRepository.deleteById(userId);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the DB.", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);

        if(role.isEmpty()){
            log.error("Role with id {} not found in the DB.", roleId);
            throw new UsernameNotFoundException(String.format("Role with id %d not found in the DB.", roleId));
        }

        log.info("Deleting role {} from the DB.", role.get().getName());
        roleRepository.deleteById(roleId);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {} in the DB.", roleName, username);
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String username) {
        log.info("Fetching user {} from the DB.", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users from the DB.");
        return userRepository.findAll();
    }
}
