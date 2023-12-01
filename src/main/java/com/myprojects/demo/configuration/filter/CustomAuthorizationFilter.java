package com.myprojects.demo.configuration.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myprojects.demo.dto.DecodedData;
import com.myprojects.demo.utilities.JwtUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login") ||
            request.getServletPath().equals("/user/token/refresh") ||
            request.getServletPath().equals("/user/save") ||
            request.getServletPath().equals("/movie/page")) {
            filterChain.doFilter(request, response);
            return;
        }


        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String access_token = authorizationHeader.substring("Bearer ".length());
                DecodedData data = JwtUtilities.tryToDecode(access_token, false);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(data.getUsername(), null, data.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                log.error("Error logging in: {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
//                response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}