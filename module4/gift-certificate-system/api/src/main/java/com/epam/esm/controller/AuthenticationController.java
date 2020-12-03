package com.epam.esm.controller;

import com.epam.esm.dto.AuthenticationRequest;
import com.epam.esm.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity generate(@RequestBody AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String token = authenticationService.generateToken(authenticationRequest);
        Map<Object, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}
