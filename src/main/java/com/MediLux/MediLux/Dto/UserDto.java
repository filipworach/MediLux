package com.MediLux.MediLux.Dto;

import lombok.Data;

@Data
public class UserDto {
     private String email;
     private String password;
     private String token;
     private String role;
     private String newPassword;

}
