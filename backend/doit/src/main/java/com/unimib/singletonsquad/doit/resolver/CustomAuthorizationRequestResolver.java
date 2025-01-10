package com.unimib.singletonsquad.doit.resolver;
import java.util.Base64;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import jakarta.servlet.http.HttpServletRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
        return customizeAuthorizationRequest(authorizationRequest, request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
        return customizeAuthorizationRequest(authorizationRequest, request);
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest,
            HttpServletRequest request) {
        if (authorizationRequest == null) {
            return null;
        }
        String role = (String) request.getSession().getAttribute("role");
        if (role == null) {
            role = "default";
        }
        String uuid = (String) request.getSession().getAttribute("uuid");
        if (uuid == null) {
                uuid = "default-uuid"; // Valore di default se il UUID non è presente
        }

// Codifica entrambi i parametri in Base64
        String encodedRole = Base64.getEncoder().encodeToString(role.getBytes());
        String encodedUuid = Base64.getEncoder().encodeToString(uuid.getBytes());

// Concatenazione dei parametri nello stato
        String customState = "role=" + encodedRole + "&uuid=" + encodedUuid;

        return OAuth2AuthorizationRequest.from(authorizationRequest)
                    .state(customState)
                    .build();
        }
    }

