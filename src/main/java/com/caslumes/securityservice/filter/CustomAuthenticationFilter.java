package com.caslumes.securityservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.caslumes.securityservice.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    private Date accessTokenExpirationSpan() {
        return new Date(System.currentTimeMillis() + 10 * 60 * 1000);
    };

    private Date refreshTokenExpirationSpan(){
        return new Date(System.currentTimeMillis() + 30 * 60 * 1000);
    };

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        log.info("Username: {}", username);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        Algorithm algorithm = Utils.getJwtAlgorithm();

        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(accessTokenExpirationSpan())
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(refreshTokenExpirationSpan())
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();

        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);

//        Cookie jwtCookie = new Cookie("jwtToken", access_token);
//        jwtCookie.setHttpOnly(true);
//        jwtCookie.setSecure(true);
//        jwtCookie.setPath("/");
//        int maxAge = (int) Duration.between(LocalDate.now(), LocalDate.from(Instant.ofEpochMilli(refreshTokenExpirationSpan.getTime()))).getSeconds();
//        jwtCookie.setMaxAge(maxAge);
//
//        response.addCookie(jwtCookie);

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
