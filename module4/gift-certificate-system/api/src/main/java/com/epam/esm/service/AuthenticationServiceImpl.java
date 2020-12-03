package com.epam.esm.service;

import com.epam.esm.dto.AuthenticationRequest;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthenticationServiceImpl implements AuthenticationService{
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String generateToken(AuthenticationRequest authenticationRequest) {
        authenticate(authenticationRequest);
        String username = authenticationRequest.getUsername();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenProvider.generateToken(userDetails);
    }

    private void authenticate(AuthenticationRequest authenticationRequest) {
        try {
            String username = authenticationRequest.getUsername();
            String password = authenticationRequest.getPassword();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("Incorrect username or password", e);
        }
    }

}
