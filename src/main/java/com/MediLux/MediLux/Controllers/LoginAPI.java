package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Dto.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class LoginAPI {

//    @PostMapping("/login")
//    public String login(@RequestBody UserDto userDto)
//    {
//
//        long currentTimeMillis = System.currentTimeMillis();
//        return Jwts.builder()
//                .setSubject(userDto.getEmail())
//                .claim("roles", "user")
//                .setIssuedAt(new Date(currentTimeMillis))
//                .setExpiration(new Date(currentTimeMillis))
//                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512))
//                .compact();
//    }
}