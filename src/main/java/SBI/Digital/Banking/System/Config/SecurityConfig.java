package SBI.Digital.Banking.System.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    /**
     * Configure BCrypt password encoder
     * BCrypt is a strong password hashing algorithm
     * Strength 10 means 2^10 iterations (recommended for security)
     * 
     * @return BCryptPasswordEncoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
    
    /**
     * Configure security filter chain
     * Allow public access to H2 console and all banking endpoints
     * 
     * @param http the HttpSecurity object
     * @return SecurityFilterChain bean
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
            .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
        return http.build();
    }
}
