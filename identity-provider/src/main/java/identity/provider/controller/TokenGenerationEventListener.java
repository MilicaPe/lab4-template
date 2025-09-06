//package identity.provider.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
//import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenGeneratedEvent;
//import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIssuedEvent;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TokenGenerationEventListener {
//    private static final Logger logger = LoggerFactory.getLogger(TokenGenerationEventListener.class);
//
//    @EventListener
//    public void onTokenGenerated(OAuth2AccessTokenGeneratedEvent event) {
//        OAuth2AccessToken accessToken = event.getAccessToken();
//        OAuth2Authorization authorization = event.getAuthorization();
//
//        logger.info("Access token generated for client: {}",
//                authorization.getRegisteredClientId()); // Исправлено на getRegisteredClientId()
//        logger.info("Token scopes: {}", accessToken.getScopes());
//        logger.info("Token expires at: {}", accessToken.getExpiresAt());
//    }
//
//    @EventListener
//    public void onTokenIssued(OAuth2TokenIssuedEvent event) {
//        OAuth2Authorization authorization = event.getAuthorization();
//        logger.info("Token issued for principal: {}",
//                authorization.getPrincipalName());
//    }
//}