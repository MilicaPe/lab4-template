package identity.provider.config;

import identity.provider.model.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

//@Component
//public class OidcUserInfoMapper implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {
//
//    @Override
//    public OidcUserInfo apply(OidcUserInfoAuthenticationContext context) {
//        OAuth2AuthenticationToken authentication = context.getAuthentication();
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//
//        return OidcUserInfo.builder()
//                .subject(userDetails.getUsername())
//                .name(userDetails.getUsername())
//                .email(userDetails.getEmail())
//                .claim("roles", userDetails.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.toList()))
//                .build();
//    }
//}
