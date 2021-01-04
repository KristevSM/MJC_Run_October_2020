package com.epam.esm.service;

import com.epam.esm.dto.AuthenticationRequest;

public interface AuthenticationService {

    String generateToken(AuthenticationRequest authenticationRequest);

}
