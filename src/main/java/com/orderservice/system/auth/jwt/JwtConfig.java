package com.orderservice.system.auth.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@Data
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtConfig {
    private String secretKey ="Z9ANoDmEsyk0hMF6hTBTDZoEuZlzqohFlcnxroo5XTSur7kDgq3Tmn8J+s/CfnCDERvRPIdXOtFxyRcdqcLQsQ==";
    private int refreshExpiration=604800000;
    private int accessExpiration=86400000;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
