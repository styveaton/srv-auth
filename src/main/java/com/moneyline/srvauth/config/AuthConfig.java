package com.moneyline.srvauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class AuthConfig {

    private List<String> permitAllEndpoints = Stream.of(EndpointsAuthenticated.values())
            .map(EndpointsAuthenticated::getValue)
            .collect(Collectors.toList());

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        String[] authenticateEndpoints = new String[permitAllEndpoints.size()];
        authenticateEndpoints = permitAllEndpoints.toArray(authenticateEndpoints);

         http
                .cors().and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(authenticateEndpoints
//                        EndpointsAuthenticated.ENDPOINT_FETCH_COUNTRY.getValue(),
//                        EndpointsAuthenticated. ENDPOINT_UPDATE_USER.getValue(),
//                        EndpointsAuthenticated. ENDPOINT_REGISTER_USER.getValue(),
//                        EndpointsAuthenticated.ENDPOINT_LOGIN_USER.getValue(),
//                        EndpointsAuthenticated.ENDPOINT_SEND_CODE.getValue(),
//                        EndpointsAuthenticated.ENDPOINT_ACTIVATE_ACCOUNT.getValue(),
//                        EndpointsAuthenticated.ENDPOINT_LOGIN_USER.getValue(),
//                        EndpointsAuthenticated.ENDPOINT_CHANGEPIN.getValue(),
//
//                        EndpointsAuthenticated.ENDPOINT_FETCH_USER.getValue()
//                        EndpointsAuthenticated.ENDPOINT_VALIDATE_TOKEN.getValue()
                )
                .authenticated().anyRequest().permitAll();

        return http.build();

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
}
