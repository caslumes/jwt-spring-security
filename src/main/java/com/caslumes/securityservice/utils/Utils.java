package com.caslumes.securityservice.utils;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;

public class Utils {
    @Getter
    private final static Algorithm jwtAlgorithm = Algorithm.HMAC256("secret".getBytes());

}
