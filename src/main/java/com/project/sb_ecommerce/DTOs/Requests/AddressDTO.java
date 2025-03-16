package com.project.sb_ecommerce.DTOs.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO
{
    private String building;
    private String Street;
    private String city;
    private String state;
    private String country;
    private String pincode;
}
