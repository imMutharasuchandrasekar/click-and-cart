package com.project.sb_ecommerce.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    private String building;

    @NotBlank
    private String Street;

    @NotBlank
    @Size(min = 2, message = "City name should be atleast two letters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name should be atleast two letters")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name should be atleast two letters")
    private String country;

    @NotBlank
    @Size(min = 3,  message = "Pincode should have atleast 3 digits")
    private String pincode;

    public Address(String city, String building, String street, String state, String country, String pincode) {
        this.city = city;
        this.building = building;
        Street = street;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }

    @ManyToMany(mappedBy = "addresses")
    private List<User> users;
}
