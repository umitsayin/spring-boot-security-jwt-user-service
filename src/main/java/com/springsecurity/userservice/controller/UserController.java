package com.springsecurity.userservice.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.springsecurity.userservice.service.UserService;
import com.springsecurity.userservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        return ResponseEntity.ok().body(this.userService.saveUser(user));
    }

    @PostMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestBody User user, HttpServletRequest request){
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok().body(this.userService.updateUser(user,username));
    }
}
