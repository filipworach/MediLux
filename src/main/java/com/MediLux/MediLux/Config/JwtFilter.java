package com.MediLux.MediLux.Config;


import com.MediLux.MediLux.Exceptions.HeaderException;
import com.MediLux.MediLux.Exceptions.WrongKeyException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Key;

public class JwtFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String header = httpServletRequest.getHeader("authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new HeaderException();
        } else {
            try {
                String token = header.substring(7);
                Claims claims = Jwts.parser()
                        .setSigningKey(JwtKeyProvider.secretKey)
                        .parseClaimsJws(token)
                        .getBody();
                servletRequest.setAttribute("claims", claims);
            } catch (Exception e) {
                throw new WrongKeyException();
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
