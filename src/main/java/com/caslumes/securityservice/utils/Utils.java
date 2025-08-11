package com.caslumes.securityservice.utils;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class Utils {
    private static final String secret = "secret";

    @Getter
    private static final Algorithm jwtAlgorithm = Algorithm.HMAC256(secret.getBytes());

    public static Date getAccessTokenExpirationSpan(){return new Date(System.currentTimeMillis() + 10 * 60 * 1000);};

    public static Date getRefreshTokenExpirationSpan(){return new Date(System.currentTimeMillis() + 30 * 60 * 1000);};
}
