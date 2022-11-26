package com.springsecurity.userservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.springsecurity.userservice.model.Role;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class JwtTokenUtil {
    final Algorithm algorithm = Algorithm.HMAC256("UMIT_SAYIN".getBytes());

    public String generateAccessToken(final String username, final List<?> roles){

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withClaim("roles",roles)
                .sign(algorithm);
    }

    public String generateRefreshToken(final String username){

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .sign(algorithm);
    }
}
