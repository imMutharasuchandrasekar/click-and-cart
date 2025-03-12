package com.project.sb_ecommerce.DTOs.Responses;
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

    private List<String> roles;

    // As cookie based auth. is implemented removing the token from response.
    //  private String jwtToken;
}