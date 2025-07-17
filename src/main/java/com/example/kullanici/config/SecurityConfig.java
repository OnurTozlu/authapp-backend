package com.example.kullanici.config;

import com.example.kullanici.security.JwtAuthenticationFilter;
import com.example.kullanici.security.JwtUtil;
import com.example.kullanici.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            HandlerMappingIntrospector mvcHandlerMappingIntrospector
    ) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})  // Cors ayarları bu satırla aktif ama ayar eklenmedi
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new MvcRequestMatcher(mvcHandlerMappingIntrospector, "/api/kullanici/kayit")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(mvcHandlerMappingIntrospector, "/api/kullanici/giris")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(mvcHandlerMappingIntrospector, "/auth/**")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(mvcHandlerMappingIntrospector, "/uploads/**")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(mvcHandlerMappingIntrospector, "/api/kullanici/me")).authenticated()
                        .requestMatchers(new MvcRequestMatcher(mvcHandlerMappingIntrospector, "/api/kullanici/*/uploadProfilFoto")).authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
