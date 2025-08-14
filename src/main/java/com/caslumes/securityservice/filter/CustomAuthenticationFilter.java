package com.caslumes.securityservice.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.caslumes.securityservice.jwt.JWTGenerator;
import com.caslumes.securityservice.service.UserService;
import com.caslumes.securityservice.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        log.info("Username: {}", username);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User user = (User) authResult.getPrincipal();
        Algorithm algorithm = Utils.getJwtAlgorithm();

        String access_token = JWTGenerator.generateAccessToken(request, user, algorithm, Utils.getAccessTokenExpirationSpan());

        String refresh_token = JWTGenerator.generateRefreshToken(request, user, algorithm, Utils.getRefreshTokenExpirationSpan());

        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("access_token", access_token);
        responseBody.put("user", userService.getUser(user.getUsername()));

        long refreshTokenExpirationSpanMillis = Utils.getRefreshTokenExpirationSpan().getTime() - new Date(System.currentTimeMillis()).getTime();
        int maxAge = (int) refreshTokenExpirationSpanMillis/1000;

        ResponseCookie resCookie = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true)
                .sameSite("None")
                .domain("localhost")
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .build();

        response.addHeader("Set-Cookie", resCookie.toString());

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }
}
