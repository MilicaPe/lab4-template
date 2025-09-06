package identity.provider.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import identity.provider.model.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        return new InMemoryOAuth2AuthorizationConsentService();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private static RSAKey generateRsa() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private static KeyPair generateRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
//        return context -> {
//            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
//                Authentication principal = context.getPrincipal();
//
//                // Добавляем роли
//                List<String> authorities = principal.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.toList());
//                context.getClaims().claim("roles", authorities);
//
//                // Добавляем стандартные OIDC claims
//                if (principal.getPrincipal() instanceof CustomUserDetails) {
//                    CustomUserDetails userDetails = (CustomUserDetails) principal.getPrincipal();
//                    context.getClaims().claim("email", userDetails.getEmail());
//                    context.getClaims().claim("preferred_username", userDetails.getUsername());
//                }
//            }
//        };
//    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                Authentication principal = context.getPrincipal();

                // Добавляем роли
                List<String> authorities = principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
                context.getClaims().claim("roles", authorities);

                // Добавляем стандартные OIDC claims
                if (principal.getPrincipal() instanceof CustomUserDetails) {
                    CustomUserDetails userDetails = (CustomUserDetails) principal.getPrincipal();
                    context.getClaims().claim("email", userDetails.getEmail());
                    context.getClaims().claim("preferred_username", userDetails.getUsername());
                }
            }

            // Для ID token тоже добавляем claims
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                Authentication principal = context.getPrincipal();
                if (principal.getPrincipal() instanceof CustomUserDetails) {
                    CustomUserDetails userDetails = (CustomUserDetails) principal.getPrincipal();
                    context.getClaims().claim("email", userDetails.getEmail());
                    context.getClaims().claim("preferred_username", userDetails.getUsername());

                    List<String> authorities = principal.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList());
                    context.getClaims().claim("roles", authorities);
                }
            }
        };
    }
}

//
//@Configuration(proxyBeanMethods = false)
//public class AuthorizationServerConfig {
//
//    @Bean
//    public OAuth2AuthorizationService authorizationService() {
//        return new InMemoryOAuth2AuthorizationService();
//    }
//
//    @Bean
//    public OAuth2AuthorizationConsentService authorizationConsentService() {
//        return new InMemoryOAuth2AuthorizationConsentService();
//    }
//
//    @Bean
//    public JWKSource<SecurityContext> jwkSource() {
//        RSAKey rsaKey = generateRsa();
//        JWKSet jwkSet = new JWKSet(rsaKey);
//        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
//    }
//
//    private static RSAKey generateRsa() {
//        KeyPair keyPair = generateRsaKey();
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        return new RSAKey.Builder(publicKey)
//                .privateKey(privateKey)
//                .keyID(UUID.randomUUID().toString())
//                .build();
//    }
//
//    private static KeyPair generateRsaKey() {
//        try {
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//            keyPairGenerator.initialize(2048);
//            return keyPairGenerator.generateKeyPair();
//        } catch (Exception ex) {
//            throw new IllegalStateException(ex);
//        }
//    }
//
//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
//        return context -> {
//            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
//                Authentication principal = context.getPrincipal();
//
//                // Добавляем роли
//                List<String> authorities = principal.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.toList());
//                context.getClaims().claim("roles", authorities);
//
//                // Добавляем стандартные OIDC claims
//                if (principal.getPrincipal() instanceof CustomUserDetails) {
//                    CustomUserDetails userDetails = (CustomUserDetails) principal.getPrincipal();
//                    context.getClaims().claim("email", userDetails.getEmail());
//                    context.getClaims().claim("preferred_username", userDetails.getUsername());
//                }
//            }
//        };
//    }
//}


//
//@Configuration(proxyBeanMethods = false)
//public class AuthorizationServerConfig {
//
//
//    @Bean
//    public JWKSource<SecurityContext> jwkSource() {
//        RSAKey rsaKey = generateRsa();
//        JWKSet jwkSet = new JWKSet(rsaKey);
//        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
//    }
//
//    private static RSAKey generateRsa() {
//        KeyPair keyPair = generateRsaKey();
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        return new RSAKey.Builder(publicKey)
//                .privateKey(privateKey)
//                .keyID(UUID.randomUUID().toString())
//                .build();
//    }
//
//    private static KeyPair generateRsaKey() {
//        try {
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//            keyPairGenerator.initialize(2048);
//            return keyPairGenerator.generateKeyPair();
//        } catch (Exception ex) {
//            throw new IllegalStateException(ex);
//        }
//    }
//
//
//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
//        return context -> {
//            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
//
//                Authentication principal = context.getPrincipal();
//
//                // Добавляем роли
//                List<String> authorities = principal.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.toList());
//
//                context.getClaims().claim("roles", authorities);
//
//                // Добавляем стандартные OIDC claims
//                if (context.getPrincipal() instanceof UserDetails) {
//                    UserDetails userDetails = (UserDetails) principal.getPrincipal();
//                    // Для email и profile нужно использовать кастомный UserDetails
//                    if (userDetails instanceof CustomUserDetails) {
//                        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
//                        context.getClaims().claim("email", customUserDetails.getEmail());
//                        context.getClaims().claim("profile", Map.of(
//                                "name", customUserDetails.getUsername(),
//                                "email", customUserDetails.getEmail()
//                        ));
//                    }
//                }
//            }
//        };
//    }
//}
