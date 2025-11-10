package com.example.paintlab.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        System.out.println("🎯 SECURITY CONFIG CARREGADA - /api/ia/** PERMITIDO!");
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/user/profile").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/user/password").authenticated()
                        .requestMatchers(HttpMethod.POST, "/user/logout").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/user/account").authenticated()
                        .requestMatchers(HttpMethod.POST, "/user/create-admin").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/colors").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/colors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/colors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/colors/**/compositions").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/colors/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/pigments").permitAll()
                        .requestMatchers(HttpMethod.POST, "/pigments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/pigments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/pigments/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/saved-colors").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/saved-colors").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/saved-colors/search").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/saved-colors/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/history").authenticated()
                        .requestMatchers(HttpMethod.GET, "/history/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/history/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/ia/**").permitAll()
                        .requestMatchers("/api/history/**").permitAll()
                        .requestMatchers("/api/ia/**").permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
