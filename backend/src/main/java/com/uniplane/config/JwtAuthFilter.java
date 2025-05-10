package com.uniplane.config;

import com.uniplane.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
        throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwtUtil.validateToken(jwt)) {
                Claims claims = jwtUtil.extractClaims(jwt);
                String username = claims.getSubject();
                String role = (String) claims.get("role");

                // Create Spring Security authentication object
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singleton(authority)
                );

                SecurityContextHolder.getContext().setAuthentication(auth);

                // Also pass tenantId and role in request attributes
                request.setAttribute("tenantId", claims.get("tenantId"));
                request.setAttribute("role", role);
                request.setAttribute("username", username);
                request.setAttribute("projectId", claims.get("projectId"));
            }
        }

        chain.doFilter(request, response);
    }
}
