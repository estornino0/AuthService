package com.app.authservice.security;

import com.app.authservice.service.UserDetailServiceImpl;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter{

    @Autowired private UserDetailServiceImpl userDetailsService;
    @Autowired private JWTUtil JWTUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            // No Bearer Header
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } else {
            String jwt = authHeader.substring(7);
            if(jwt.isBlank()){
                // No JWT Token in Bearer Header
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else {
                try{
                    String email = JWTUtil.validateTokenAndRetrieveEmail(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());
                    if(SecurityContextHolder.getContext().getAuthentication() == null){
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch(JWTVerificationException | UsernameNotFoundException exc){
                    // Invalid JWT Token
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private final RequestMatcher uriMatcher =
            new AntPathRequestMatcher("/api/auth/{login|register}", HttpMethod.POST.name());

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.matches("/api/auth/(login|register)") ||
                path.matches("/(login|register)");
    }
}
