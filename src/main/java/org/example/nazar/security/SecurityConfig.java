package org.example.nazar.security;

import org.example.nazar.security.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthFilter jwtAuthFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity req) throws Exception {
        return req.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/registration/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().permitAll())
                .userDetailsService(customUserDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)


                .build();
    }

    //    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user1 = User.withUsername("user")
//                .password("$2a$12$lz1phDOTa/NK656Ewj97Duv7mJOtPcdODzavBa.Q0nyZXFS67GNHy")
//                .roles("ADMIN")
//                .build();
//        UserDetails user2 = User.withUsername("admin")
//                .password("$2a$12$lz1phDOTa/NK656Ewj97Duv7mJOtPcdODzavBa.Q0nyZXFS67GNHy")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user1, user2);
//    }
    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;

    }
}
