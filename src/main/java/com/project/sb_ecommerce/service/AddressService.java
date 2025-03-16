package com.project.sb_ecommerce.service;

import com.project.sb_ecommerce.DTOs.Requests.AddressDTO;
import com.project.sb_ecommerce.model.Address;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AddressService
{
    AddressDTO createAddressForUser( AddressDTO addressDto );

    List<AddressDTO> getAddressByUser();

    AddressDTO updateAddress( Long addressId, AddressDTO addressDTO );

    String deleteAddress(Long addressId);
}
