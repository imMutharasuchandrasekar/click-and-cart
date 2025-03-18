package com.project.sb_ecommerce.controller;

import com.project.sb_ecommerce.DTOs.Requests.AddressDTO;
import com.project.sb_ecommerce.model.Address;
import com.project.sb_ecommerce.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController
{
    @Autowired
    AddressService addressService;

    @PostMapping("/add-address")
    public ResponseEntity<AddressDTO> createAddress( @Valid @RequestBody AddressDTO addressDto )
    {
        AddressDTO savedAddress = addressService.createAddressForUser( addressDto );
        return new ResponseEntity<>( savedAddress, HttpStatus.CREATED );
    }

    @GetMapping("/user/address")
    public ResponseEntity<?> getAddressByUser()
    {
        try{
            List<AddressDTO> addressDTOList = addressService.getAddressByUser();
            return new ResponseEntity<>( addressDTOList, HttpStatus.OK );
        }
        catch ( RuntimeException e )
        {
            return new ResponseEntity<>( e.getMessage(), HttpStatus.NOT_FOUND );
        }
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress( @PathVariable Long addressId
            , @RequestBody AddressDTO addressDTO )
    {
        AddressDTO updatedAddress = addressService.updateAddress( addressId, addressDTO );
        return new ResponseEntity<>( updatedAddress, HttpStatus.OK );
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId)
    {
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>( status, HttpStatus.OK );
    }


}
