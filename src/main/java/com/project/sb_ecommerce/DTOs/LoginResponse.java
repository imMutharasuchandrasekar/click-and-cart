package com.project.sb_ecommerce.DTOs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse
{
    private String username;
    private String jwtToken;
    private List<String> roles;
}