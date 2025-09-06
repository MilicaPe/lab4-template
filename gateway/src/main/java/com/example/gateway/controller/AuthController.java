package com.example.gateway.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class AuthController {

    @GetMapping("/api/userinfo")
    public Map<String, Object> userInfo(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "sub", jwt.getSubject(),
                "name", jwt.getClaim("name"),
                "email", jwt.getClaim("email")
        );
    }

    @GetMapping("/api/public/initiate-login")
    public void initiateLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/idp");
    }
}


//package com.example.gateway.controller;
//
//import com.example.gateway.dto.PaginationResponseDTO;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/**")
//public class GatewayController {
//
//    @Value("${identity.service.url}")
//    private String basicUrl;
//
//    public String login() throws URISyntaxException {
//        System.out.println(" Stigao na gateway");
//        URI uri = new URI(this.basicUrl.toString() + "");
//        System.out.println(uri.toString());
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity entity = new HttpEntity(headers);
//        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, PaginationResponseDTO.class);
//        System.out.println(result.getBody());
//        System.out.println(result);
//        return result.getBody();
//    }
//
//}