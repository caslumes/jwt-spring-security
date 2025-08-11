package com.caslumes.securityservice.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.caslumes.securityservice.model.role.Role;
import com.caslumes.securityservice.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Date;
import java.util.stream.Collectors;

public class JWTGenerator {
    public static String generateAccessToken(HttpServletRequest request, User user, Algorithm algorithm, Date expirationSpan) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expirationSpan)
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(algorithm);
    }

    public static String refreshAccessToken(HttpServletRequest request, com.caslumes.securityservice.model.user.User user, Algorithm algorithm, Date expirationSpan) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expirationSpan)
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()))
                .sign(algorithm);
    }

    public static String generateRefreshToken(HttpServletRequest request, User user, Algorithm algorithm, Date expirationSpan) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expirationSpan)
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }
}
