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
    public SecurityFilterChain volunteerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Dezactivează CORS
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/volunteers/home#").hasAnyRole("VOLUNTEER_USER", "ADMIN")
                        .requestMatchers("/volunteers/profile").permitAll() // Acces doar pentru utilizatorii autentificați
//                        .requestMatchers("/admin").hasRole("ADMIN") // Acces doar pentru utilizatorii cu rolul ADMIN
                        .anyRequest().permitAll() // Permită accesul la toate celelalte pagini
                )
                .formLogin(login -> login
                        .loginPage("/volunteers/login") // Specifică pagina de login pentru utilizatorii voluntari
                        .loginProcessingUrl("/login") // Specifică URL-ul pentru procesarea formularului de login pentru voluntari
                        .permitAll()
                        .defaultSuccessUrl("/volunteers/home", true) // Setează pagina implicită de succes după logare pentru voluntari
                )
                .headers(headers -> headers.contentTypeOptions().disable()); // Dezactivează X-Content-Type-Option

        return http.build();
    }

    @Bean
    public SecurityFilterChain erasmusSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Dezactivează CORS
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/erasmus/home").hasAnyRole("ERASMUS_USER", "ADMIN")
                        .requestMatchers("/erasmus/profile").permitAll() // Acces doar pentru utilizatorii autentificați
                        .anyRequest().permitAll() // Permită accesul la toate celelalte pagini
                )
                .formLogin(login -> login
                        .loginPage("/erasmus/login_erasmus") // Specifică pagina de login pentru utilizatorii Erasmus
                        .loginProcessingUrl("/login_erasmus") // Specifică URL-ul pentru procesarea formularului de login pentru Erasmus
                        .permitAll()
                        .defaultSuccessUrl("/erasmus/home", true) // Setează pagina implicită de succes după logare pentru Erasmus
                )
                .headers(headers -> headers.contentTypeOptions().disable()); // Dezactivează X-Content-Type-Option

        return http.build();
    }


//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return web -> web.ignoring()
//                .requestMatchers("/static/css/**", "/js/**");
//    }

//    @Bean
//    public UserDetailsService userDetailsService(VolunteerUserService volunteerUserService, AdminUserService adminUserService) {
//        return username -> {
//            VolunteerUser volunteerUser = volunteerUserService.getVolunteerUserByUsername(username);
//            if (volunteerUser != null) {
//                return new MyUserPrincipal(volunteerUser);
//            }
//            AdminUser adminUser = adminUserService.getAdminUserByUsername(username);
//            if (adminUser != null) {
//                return new MyUserPrincipal(adminUser);
//            }
//            throw new UsernameNotFoundException("User not found: " + username);
//        };
//    }


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
