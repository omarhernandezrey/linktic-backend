package com.linktic.products.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${api.key:}")
    private String expectedApiKey;

    private static final String HEADER = "X-API-KEY";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/actuator")
                || path.startsWith("/error")
                || "/".equals(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (expectedApiKey == null || expectedApiKey.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }
        String provided = request.getHeader(HEADER);
        if (expectedApiKey.equals(provided)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/vnd.api+json");
            var error = Map.of("errors", new Object[]{Map.of(
                    "status", "401",
                    "title", "Unauthorized",
                    "detail", "Invalid or missing API key")});
            new ObjectMapper().writeValue(response.getWriter(), error);
        }
    }
}
