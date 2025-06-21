package site.javatech.cim.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import site.javatech.cim.core.service.UserServiceImpl;

import java.util.Arrays;

/**
 * Конфигурация безопасности приложения.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserServiceImpl userService;

    /**
     * Конструктор для внедрения зависимостей.
     * @param userService Сервис пользователей
     */
    public SecurityConfig(UserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * Настройка цепочки фильтров безопасности.
     * @param http Объект HttpSecurity
     * @return Настроенная цепочка фильтров
     * @throws Exception при ошибке конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").authenticated()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.realmName("CIM"))
                .userDetailsService(userService);

        return http.build();
    }

    /**
     * Настройка CORS.
     * @return Источник конфигурации CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Настройка кодировщика паролей.
     * @return Кодировщик паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}