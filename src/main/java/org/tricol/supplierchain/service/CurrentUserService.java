package org.tricol.supplierchain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tricol.supplierchain.entity.UserApp;
import org.tricol.supplierchain.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrentUserService {

    private final UserRepository userRepository;

    @Transactional
    public UserApp getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }


        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String keycloakUserId = jwt.getSubject();
            
            return userRepository.findByKeycloakUserId(keycloakUserId)
                    .orElseGet(() -> createKeycloakUser(jwt));
        }
        

        String username = authentication.getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    private UserApp createKeycloakUser(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");

        UserApp user = UserApp.builder()
                .keycloakUserId(keycloakUserId)
                .username(username != null ? username : keycloakUserId)
                .email(email != null ? email : keycloakUserId + "@keycloak.local")
                .password(null)
                .enabled(true)
                .role(null)
                .build();

        UserApp savedUser = userRepository.save(user);
        log.info("Auto-created Keycloak user: {} (ID: {})", username, keycloakUserId);
        
        return savedUser;
    }
}
