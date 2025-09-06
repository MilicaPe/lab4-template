package identity.provider.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userinfo")
public class UserInfoController {

    @GetMapping
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("sub", jwt.getSubject());
        userInfo.put("preferred_username", jwt.getClaim("preferred_username"));
        userInfo.put("email", jwt.getClaim("email"));
        userInfo.put("roles", jwt.getClaim("roles"));
        return userInfo;
    }
}

//@RequestMapping("/userinfo")
//public class UserInfoController {
//
//    @GetMapping
//    public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
//        return Map.of(
//                "sub", jwt.getSubject(),
//                "email", jwt.getClaim("email"),
//                "name", jwt.getClaim("name"),
//                "preferred_username", jwt.getClaim("preferred_username")
//        );
//    }
//}