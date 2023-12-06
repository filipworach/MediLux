package com.MediLux.MediLux.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.MediLux.MediLux.Config.JwtKeyProvider.secretKey;

public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private String secret; // Klucz prywatny używany do podpisywania JWT


    private Long expiration; // Czas wygaśnięcia tokena w milisekundach

    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Pobierz token z nagłówka HTTP
        String token = extractToken(request);

        // Sprawdź, czy żądanie dotyczy logowania
        if (isLoginRequest(request) || isRegisterRequest(request)) {
            // Jeśli tak, przepuść żądanie bez sprawdzania tokenu
            filterChain.doFilter(request, response);
            return;
        }

        // Zweryfikuj token
        if (token != null && validateToken(token)) {
            // Uwierzytelnij użytkownika
            Authentication authentication = getAuthentication(token, request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "User not in database or user is both student and employee!");
        }
    }

    private boolean isRegisterRequest(HttpServletRequest request) {
        return "/patient/register".equalsIgnoreCase(request.getRequestURI()); //|| "/employee/register".equalsIgnoreCase(request.getRequestURI());
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return "/api/auth/login".equalsIgnoreCase(request.getRequestURI());
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Authentication getAuthentication(String token, HttpServletRequest request) {
        String username = getUsernameFromToken(token);
        List<SimpleGrantedAuthority> authorities = getAuthoritiesFromToken(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Przypisanie ról do użytkownika
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return authenticationToken;
    }

    private String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    private List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        // Retrieve the "roles" claim from the token
        Object rolesObject = claims.get("roles");

        // Manually convert the claim value to List<String>
        List<String> roles;
        if (rolesObject instanceof List) {
            roles = (List<String>) rolesObject;
        } else if (rolesObject instanceof String) {
            roles = Arrays.asList((String) rolesObject);
        } else {
            roles = Collections.emptyList(); // or handle the unsupported type accordingly
        }

        // Convert roles to SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return authorities;
    }
}
