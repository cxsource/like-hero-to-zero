package com.likeherotozero.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security Konfiguration.
 *
 * Diese Klasse definiert die Sicherheitsrichtlinien der Anwendung:
 * - Oeffentliche Zugriffe auf /emissions, /login, CSS/JS und H2-Konsole
 * - Geschuetzte /backend/** Pfade mit SCIENTIST-Rolle
 * - Form-basiertes Login mit Umleitungen
 * - Logout-Funkionalitaet
 *
 * Benutzer:
 * - Benutzername: scientist
 * - Passwort: password123
 * - Rolle: SCIENTIST
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/emissions", "/emissions/**", "/css/**", "/js/**", "/h2-console/**").permitAll()
                .requestMatchers("/backend/**").hasRole("SCIENTIST")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/backend", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/emissions")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        var scientist = User.builder()
            .username("scientist")
            .password(encoder.encode("password123"))
            .roles("SCIENTIST")
            .build();
        return new InMemoryUserDetailsManager(scientist);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
