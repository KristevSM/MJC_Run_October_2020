package com.epam.esm.security;

import com.epam.esm.dto.RoleDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.exception.UserAccessDeniedException;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthorizationComponentImpl implements AuthorizationComponent{

    private final UserService userService;
    @Override
    public boolean userHasAccess(@Nonnull UserDetails principal, @Nonnull Long id) {
        if (id == null) {
            throw new InvalidInputDataException("The given id must not be null!");
        }
        UserDTO user = userService.getUserById(id);
        Collection<? extends GrantedAuthority> roles = principal.getAuthorities();
        if (roles.stream().anyMatch(r-> r.getAuthority().contains("ROLE_ADMIN"))) {
            return true;
        } else if (principal.getUsername().equals(user.getUsername())) {
            return true;
        } else {
            throw new UserAccessDeniedException("You don't have access to view the resource!");
        }
    }

    @Override
    public boolean isUsersOrder(@Nonnull UserDetails principal, @Nonnull Long id) {
        User user = userService.findByUsername(principal.getUsername());
        Collection<? extends GrantedAuthority> roles = principal.getAuthorities();
        if (roles.stream().anyMatch(r-> r.getAuthority().contains("ROLE_ADMIN"))) {
            return true;
        } else if (user.getOrders().stream().anyMatch(o -> o.getId().equals(id))) {
            return true;
        } else {
            throw new UserAccessDeniedException("You don't have access to view the resource");
        }    }
}
