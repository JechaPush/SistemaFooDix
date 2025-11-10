package com.example.SistemaDePromociones.config;

import com.example.SistemaDePromociones.security.RoleBasedAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad de Spring Security
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private RoleBasedAuthenticationSuccessHandler successHandler;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas - accesibles sin autenticación
                .requestMatchers(
                    "/",
                    "/login",
                    "/registro",
                    "/registro-repartidor",
                    "/registro-restaurante",
                    "/verificacion",
                    "/auth/send-code",
                    "/auth/verify-code",
                    "/contacto",
                    "/tutorial",
                    "/recuperar-password",
                    "/api/provincias/**",
                    "/api/distritos/**",
                    "/api/validation/**",
                    "/menuAdministrador/test-password",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/images/**"
                ).permitAll()
                
                // Rutas protegidas por rol
                .requestMatchers("/menuAdministrador/**").hasRole("ADMIN")
                .requestMatchers("/menuRestaurante/**").hasRole("RESTAURANT")
                .requestMatchers("/menuDelivery/**").hasRole("DELIVERY")
                .requestMatchers("/menuUsuario/**").hasRole("CUSTOMER")
                .requestMatchers("/uploads/**").hasAnyRole("ADMIN", "RESTAURANT", "DELIVERY")
                
                // Todas las demás rutas requieren autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(successHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/auth/send-code",
                    "/auth/verify-code",
                    "/api/**"
                )
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            );
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
