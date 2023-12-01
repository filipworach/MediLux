package com.MediLux.MediLux.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter{


//    @Bean
//    protected SecurityFilterChain configure(HttpSecurity security) throws Exception
//    {
//        security.httpBasic().disable();
//        security.cors().and().csrf().disable();
//        security.authorizeRequests().anyRequest().permitAll();
//        return security.build();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Wyłącz domyślne zabezpieczenia Spring Security
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable();
    }
}
