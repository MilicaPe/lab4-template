package identity.provider.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OidcConfigurationController {

    @GetMapping("/.well-known/openid-configuration")
    public Map<String, Object> getOpenIdConfiguration() {
        Map<String, Object> config = new HashMap<>();
        config.put("issuer", "http://localhost:9000");
        config.put("authorization_endpoint", "http://localhost:9000/api/v1/authorize");
        config.put("token_endpoint", "http://localhost:9000/api/v1/token");
        config.put("userinfo_endpoint", "http://localhost:9000/userinfo");
        config.put("jwks_uri", "http://localhost:9000/.well-known/jwks.json");
        config.put("scopes_supported", List.of("openid", "profile", "email"));
        config.put("response_types_supported", List.of("code"));
        config.put("grant_types_supported", List.of("authorization_code", "refresh_token"));
        config.put("subject_types_supported", List.of("public"));
        config.put("id_token_signing_alg_values_supported", List.of("RS256"));
        config.put("token_endpoint_auth_methods_supported", List.of("client_secret_basic"));
        config.put("claims_supported", List.of("sub", "email", "preferred_username", "roles"));

        return config;
    }
}