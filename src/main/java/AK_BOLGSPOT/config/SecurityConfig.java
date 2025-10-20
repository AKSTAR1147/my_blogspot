package AK_BOLGSPOT.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// ADDED: This import is needed for the new filter placement
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig  {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    // ADDED: Inject your new custom filter
    @Autowired
    private CustomRedirectUriFilter customRedirectUriFilter;

    // This beam for your local login info remains unchanged
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        //this  will take info from the DB tables
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ADDED: This line registers your new filter to run before the OAuth2 process starts
                .addFilterBefore(customRedirectUriFilter, OAuth2AuthorizationRequestRedirectFilter.class)

                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**"
                                ).permitAll()
                                .requestMatchers("/addPost").hasRole("ADMIN")
                                .requestMatchers("/post/*/comment").authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(form ->
                        form
                                .loginPage("/loginDB")
                                .loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/addPost",true)
                                .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(customAuthenticationSuccessHandler)
                )
                .logout(logout ->
                        logout.permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                );
        return http.build();
    }
}
