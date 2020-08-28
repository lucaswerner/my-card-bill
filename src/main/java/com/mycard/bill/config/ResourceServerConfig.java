package com.mycard.bill.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        final String admin = "ADMIN";
        final String system = "SYSTEM";
        final String[] adminAndSystem = {admin, system};

        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/bills/all/**").hasAnyRole(adminAndSystem)
                .antMatchers(HttpMethod.GET).hasAuthority("READ_BILL")
                .antMatchers(HttpMethod.POST).hasAuthority("WRITE_BILL");
    }

}
