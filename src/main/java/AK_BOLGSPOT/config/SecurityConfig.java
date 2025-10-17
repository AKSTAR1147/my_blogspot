package AK_BOLGSPOT.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig  {

    //beam for my local login info
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        //this  will take info from the DB tables
        return new JdbcUserDetailsManager(dataSource);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**"
                                ).permitAll()
                                .requestMatchers("/addPost").hasRole("ADMIN")   // 1. Only users with ROLE_OWNER can access /addPost

                                .requestMatchers("/post/*/comment").authenticated() // 2. ANY authenticated user (DB or Google) can comment

                                .anyRequest().permitAll()       // 3. All other requests are permitted
                )
                .formLogin(form ->
                        form
                                .loginPage("/loginDB")                                    //our custom login page endpoint
                                .loginProcessingUrl("/authenticateTheUser")                     //the user login info is sent at this and spring will take care of it
                                .defaultSuccessUrl("/addPost",true)     //redirects here after login
                                .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2 // Configuration for Google users
                        .defaultSuccessUrl("/",true)
                )
                .logout(logout ->
                                logout.permitAll()                  //this adds logout support
                        //for logout form button should submit at /logout
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                );
        return http.build();
    }

}



