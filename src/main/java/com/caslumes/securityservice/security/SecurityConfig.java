package com.caslumes.securityservice.security;

import com.caslumes.securityservice.filter.CustomAuthenticationFilter;
import com.caslumes.securityservice.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.caslumes.securityservice.model.role.RoleName.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UrlBasedCorsConfigurationSource corsConfigurationSource;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
        return authBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authManager);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests((authz) -> authz
                                .requestMatchers("/api/login/**", "/api/token/refresh/**")
                                .permitAll()

                                .requestMatchers("/error")
                                .permitAll()

                        .requestMatchers(DELETE, "/api/users/**").permitAll()

                                .requestMatchers(GET, "/api/users/**")
                                .hasAnyAuthority(
                                        ROLE_SUPER_ADMIN.getRoleName(),
                                        ROLE_USER.getRoleName(),
                                        ROLE_MANAGER.getRoleName(),
                                        ROLE_ADMIN.getRoleName()
                                )

                                .requestMatchers(POST, "/api/users/save/**")
                                .hasAnyAuthority(
                                        ROLE_MANAGER.getRoleName(),
                                        ROLE_ADMIN.getRoleName(),
                                        ROLE_SUPER_ADMIN.getRoleName()
                                )

                                .requestMatchers(POST, "/api/role/save/**")
                                .hasAnyAuthority(
                                        ROLE_MANAGER.getRoleName(),
                                        ROLE_ADMIN.getRoleName(),
                                        ROLE_SUPER_ADMIN.getRoleName()
                                )

                                .requestMatchers(POST, "/api/role/addtouser/**")
                                .hasAnyAuthority(
                                        ROLE_MANAGER.getRoleName(),
                                        ROLE_ADMIN.getRoleName(),
                                        ROLE_SUPER_ADMIN.getRoleName()
                                )

                                .anyRequest()
                                .authenticated()
                )
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
