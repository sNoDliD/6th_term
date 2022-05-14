package com.example.lab2.web;

import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Set;

public interface UserInformationSecurityContextHolder {

    default Set<String> getRoles() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((SimpleKeycloakAccount) authentication.getDetails()).getRoles();
    }

    default AccessToken getAccessToken(Principal principal) {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        return keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();
    }
}
