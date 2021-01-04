package com.epam.esm.service;

import com.epam.esm.security.UserDetailsEntity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication;
        authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        String userName = ((UserDetailsEntity) authentication.getPrincipal()).getUser().getUsername();

        return Optional.of(userName);
    }
}
