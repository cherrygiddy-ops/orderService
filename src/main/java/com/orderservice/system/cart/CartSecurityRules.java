package com.orderservice.system.cart;

import com.orderservice.system.common.SecurityRules;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CartSecurityRules implements SecurityRules {
    @Override
    public void config(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry
                .requestMatchers("/orders/carts/**").hasRole("ADMIN") // requires ROLE_ADMIN
                .anyRequest().authenticated();
    }
}

