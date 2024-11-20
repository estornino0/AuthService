package com.app.authservice.security;

import com.app.authservice.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailServiceImpl userDetailsServiceImpl;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private JWTAuthFilter filter;

    @Bean
    public AuthenticationManager authenticationManager() {
        ProviderManager authenticationManager = new ProviderManager(Arrays.asList(daoAuthenticationProvider()));
        authenticationManager.setAuthenticationEventPublisher(authenticationEventPublisher());
        return authenticationManager;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userDetailsServiceImpl);
        return provider;
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

//                .securityMatcher("/protected")
//                .authorizeHttpRequests(auth -> auth.requestMatchers(AntPathRequestMatcher.antMatcher("/protected")).hasRole("USER"))
//                .addFilterBefore(new JWTAuthFilter(), UsernamePasswordAuthenticationFilter.class)

//                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/login", "/register", "/api/auth/login", "/api/auth/register").permitAll()
//                        // Si no pongo los paths aca no ejecuta el filtro, o directamente no los deja acceder de entrada.
//                           Ver que garnacho hace mentira, era lo de abajo
                // Verr patron builderrr
//                        .anyRequest().authenticated()
//                );
//                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
                .csrf((csrf) -> csrf
                    .ignoringRequestMatchers("/login", "/register", "/api/auth/login", "/api/auth/register")
                );
        return http.build();
    }


}


