package com.ESN_Poliapp.Proiect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CORS
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/volunteers/home#").hasAnyRole("VOLUNTEER_USER", "ADMIN")
                        .requestMatchers("/volunteers/profile").permitAll() // Acces doar pentru utilizatorii autentificați
                        .requestMatchers("/volunteers/admin").hasRole("ADMIN") // Acces doar pentru utilizatorii cu rolul ADMIN
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/volunteers/login")
                        .loginProcessingUrl("/login")
                        .permitAll()
                        .defaultSuccessUrl("/volunteers/home", true) // Setăm pagina implicită de succes după logare
                )
                .headers(headers -> headers.contentTypeOptions().disable() // Dezactivează X-Content-Type-Option
                );

        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return web -> web.ignoring()
//                .requestMatchers("/static/css/**", "/js/**");
//    }

    @Bean
    public UserDetailsService userDetailsService(VolunteerUserService volunteerUserService, AdminUserService adminUserService) {
        return username -> {
            VolunteerUser volunteerUser = volunteerUserService.getVolunteerUserByUsername(username);
            if (volunteerUser != null) {
                return new MyUserPrincipal(volunteerUser);
            }
            AdminUser adminUser = adminUserService.getAdminUserByUsername(username);
            if (adminUser != null) {
                return new MyUserPrincipal(adminUser);
            }
            throw new UsernameNotFoundException("User not found: " + username);
        };
    }


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
