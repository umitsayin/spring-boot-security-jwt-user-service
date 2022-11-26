package com.springsecurity.userservice.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsecurity.userservice.service.UserService;
import com.springsecurity.userservice.model.Role;
import com.springsecurity.userservice.model.User;
import com.springsecurity.userservice.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationTokenController {
    private final UserService userService;
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getHeader(AUTHORIZATION) != null && request.getHeader(AUTHORIZATION).contains("Bearer ")){
            try{
                final String token = request.getHeader(AUTHORIZATION).substring("Bearer ".length());
                final Algorithm algorithm = Algorithm.HMAC256("UMIT_SAYIN".getBytes());
                final JWTVerifier verifier = JWT.require(algorithm).build();
                final DecodedJWT decodedJWT = verifier.verify(token);
                final String username = decodedJWT.getSubject();
                final User user = this.userService.getUser(username);
                final String access_token = JwtTokenUtil.generateAccessToken(user.getUsername(), user.getRoles());
                final String refresh_token = JwtTokenUtil.generateRefreshToken(username);
                final Map<String,String> tokens = new HashMap<>();

                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception e){
                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                response.setContentType(APPLICATION_JSON_VALUE);

                final Map<String,String> error = new HashMap<>();

                error.put("error",e.getMessage());

                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }
    }
}
