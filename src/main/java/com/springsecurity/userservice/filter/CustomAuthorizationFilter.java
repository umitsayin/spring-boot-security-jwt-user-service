package com.springsecurity.userservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh")){
            filterChain.doFilter(request,response);
        }else{
            if(request.getHeader(AUTHORIZATION) != null && request.getHeader(AUTHORIZATION).contains("Bearer ")){
                try {
                    final String token = request.getHeader("AUTHORIZATION").substring("Bearer ".length());
                    final Algorithm algorithm = Algorithm.HMAC256("UMIT_SAYIN".getBytes());
                    final JWTVerifier verifier = JWT.require(algorithm).build();
                    final DecodedJWT decodedJWT = verifier.verify(token);
                    final String username = decodedJWT.getSubject();
                    final String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

                    final Collection<SimpleGrantedAuthority> authorities = stream(roles)
                            .map(role->new SimpleGrantedAuthority(role))
                            .collect(Collectors.toList());


                    final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null,authorities);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,response);
                }catch (Exception e){
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setStatus(FORBIDDEN.value());
                    response.setHeader("error",e.getMessage());

                    final Map<String,String> error = new HashMap<>();
                    error.put("error",e.getMessage());

                    new ObjectMapper().writeValue(response.getOutputStream(),error);
                }
            }else{
                filterChain.doFilter(request,response);
            }
        }
    }
}
