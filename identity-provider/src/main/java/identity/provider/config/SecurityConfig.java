package identity.provider.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    public HttpSessionRequestCache httpSessionRequestCache() {
//        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
//        requestCache.setMatchingRequestParameterName("continue"); // Установите параметр для matching
//        requestCache.setCreateSessionAllowed(true); // Разрешите создание сессии
//        return requestCache;
//    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionRequestCache httpSessionRequestCache() {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache() {
            @Override
            public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
                // Не сохраняем запросы для favicon.ico
                if (!request.getRequestURI().equals("/favicon.ico")) {
                    super.saveRequest(request, response);
                }
            }
        };
        requestCache.setMatchingRequestParameterName("continue");
        return requestCache;
    }


    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            RegisteredClientRepository registeredClientRepository,
            OAuth2AuthorizationService authorizationService,
            OAuth2AuthorizationConsentService authorizationConsentService) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        // ВАЖНО: Добавьте OIDC конфигурацию
        authorizationServerConfigurer
                .oidc(oidc -> oidc
                        .providerConfigurationEndpoint(provider -> provider
                                .providerConfigurationCustomizer(config -> config
                                        .subjectType("public")
                                        .idTokenSigningAlgorithm("RS256")
                                )
                        )
                        .clientRegistrationEndpoint(Customizer.withDefaults())
                );


        http
                .securityMatcher(
                        "/api/v1/authorize",
                        "/api/v1/token",
                        "/.well-known/jwks.json",
                        "/.well-known/openid-configuration",
                        "/userinfo" // Добавляем userinfo endpoint

                )
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Разрешить OPTIONS запросы
                                .anyRequest().authenticated())

                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        "/api/v1/token",
                        "/.well-known/jwks.json",
                        "/.well-known/openid-configuration",
                        "/userinfo"

                ))
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Добавьте CORS здесь
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                )
                .apply(authorizationServerConfigurer);



        // Configure OAuth2 Authorization Service
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .authorizationService(authorizationService)
                .authorizationConsentService(authorizationConsentService);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http,
            HttpSessionRequestCache requestCache) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/users/register",
                                "/login",           // Разрешите доступ к логину
                                "/error",
                                "/css/**",
                                "/js/**",
                                "/.well-known/openid-configuration",
                                "/.well-known/jwks.json"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .requestCache(cache -> cache.requestCache(requestCache))
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/")
                        // .successHandler(authenticationSuccessHandler(requestCache))
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));


        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
        RegisteredClient uiClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("ui-client")
                .clientSecret(passwordEncoder.encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:4200/callback")
                .redirectUri("http://localhost:4200/callback")
                // ВАЖНО: Добавьте OIDC scopes
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.EMAIL)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(1))
                        .refreshTokenTimeToLive(Duration.ofDays(7))
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(uiClient);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:9000")
                .authorizationEndpoint("/api/v1/authorize")
                .tokenEndpoint("/api/v1/token")
                .jwkSetEndpoint("/.well-known/jwks.json")
                .oidcUserInfoEndpoint("/userinfo") // Добавьте OIDC endpoints
                .oidcClientRegistrationEndpoint("/connect/register")
                .build();
    }

//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler(HttpSessionRequestCache requestCache) {
//        return (request, response, authentication) -> {
//            SavedRequest savedRequest = requestCache.getRequest(request, response);
//            if (savedRequest != null && savedRequest.getRedirectUrl() != null) {
//                // Перенаправляем на оригинальный OAuth запрос
//                response.sendRedirect(savedRequest.getRedirectUrl());
//            } else {
//                response.sendRedirect("/");
//            }
//        };
//    }

//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler(HttpSessionRequestCache requestCache) {
//        return (request, response, authentication) -> {
//            SavedRequest savedRequest = requestCache.getRequest(request, response);
//
//            System.out.println("Authentication successful");
//            System.out.println("Saved request: " + (savedRequest != null ? savedRequest.getRedirectUrl() : "null"));
//            System.out.println("Principal: " + authentication.getPrincipal());
//
//            // Spring Security автоматически обработает OAuth2 flow
//            if (savedRequest != null && savedRequest.getRedirectUrl() != null &&
//                    savedRequest.getRedirectUrl().contains("/api/v1/authorize")) {
//                System.out.println("OAuth2 flow detected, letting Spring handle it");
//            } else {
//                System.out.println("Regular login, redirecting to home");
//                assert savedRequest != null;
//                response.sendRedirect(savedRequest.getRedirectUrl());
//            }
//        };
//    }

//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler(HttpSessionRequestCache requestCache) {
//        return (request, response, authentication) -> {
//            SavedRequest savedRequest = requestCache.getRequest(request, response);
//
//            // Если это OAuth flow, перенаправляем обратно на authorize endpoint
//            if (savedRequest != null && savedRequest.getRedirectUrl() != null &&
//                    savedRequest.getRedirectUrl().contains("/api/v1/authorize")) {
//
//                // Для OAuth flow - редирект обратно на authorize endpoint
//                String redirectUrl = savedRequest.getRedirectUrl();
//                response.sendRedirect(redirectUrl);
//
//            } else {
//                // Обычный логин - перенаправляем на главную
//                response.sendRedirect("/");
//            }
//        };
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://127.0.0.1:4200"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        config.setExposedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


}
