package com.epam.esm.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

public interface AuthorizationComponent {
    boolean userHasAccess(@Nonnull UserDetails principal, @Nonnull Long id);

}
