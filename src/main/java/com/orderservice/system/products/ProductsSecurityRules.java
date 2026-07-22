package com.orderservice.system.products;

import com.orderservice.system.common.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class ProductsSecurityRules implements SecurityRules {
    @Override
    public void config(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.GET,"/products/**").permitAll();
        registry.requestMatchers(HttpMethod.GET,"/products/{productId}").permitAll();
        registry.requestMatchers(HttpMethod.GET,"/products/pages").permitAll();

    }
}
