package com.epam.esm.filter;

import com.epam.esm.exception.JwtAuthenticationException;
import com.epam.esm.security.JwtTokenProvider;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Order(value = HIGHEST_PRECEDENCE + 1)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JwtValidationFilter extends OncePerRequestFilter {

    public static final String USERNAME_REQUEST_ATTRIBUTE = "username";
    public static final String JWT_REQUEST_ATTRIBUTE = "jwt";
    public static final int BEARER_HEADER_OFFSET = 7;
    public static final String BEARER = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    private void validateJwt(HttpServletRequest request) {

        final String requestTokenHeader = request.getHeader(AUTHORIZATION);

        if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER)) {
            String jwtToken = requestTokenHeader.substring(BEARER_HEADER_OFFSET);
            try {
                String username = jwtTokenProvider.getUsernameFromToken(jwtToken);
                request.setAttribute(USERNAME_REQUEST_ATTRIBUTE, username);
                request.setAttribute(JWT_REQUEST_ATTRIBUTE, jwtToken);
            } catch (SignatureException e) {
                throw new JwtAuthenticationException("Calculating JWT signature failed: JWT signature does not match locally computed signature.");
            } catch (IllegalArgumentException e) {
                throw new JwtAuthenticationException("Unable to get JWT Token");
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        validateJwt(request);
        filterChain.doFilter(request, response);
    }
}
