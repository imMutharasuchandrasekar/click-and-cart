package com.project.sb_ecommerce.service.impl;

import com.project.sb_ecommerce.DTOs.Requests.AddressDTO;
import com.project.sb_ecommerce.Utilities.AuthUtil;
import com.project.sb_ecommerce.exceptions.APIException;
import com.project.sb_ecommerce.exceptions.ResourceNotFoundException;
import com.project.sb_ecommerce.model.Address;
import com.project.sb_ecommerce.repository.AddressRepository;
import com.project.sb_ecommerce.repository.UserRepository;
import com.project.sb_ecommerce.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.sb_ecommerce.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService
{
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Override
    public AddressDTO createAddressForUser( AddressDTO addressDTO )
    {
        User user = authUtil.loggedInUser();

        Address newaddress = modelMapper.map( addressDTO, Address.class );
        newaddress.setUser( user );

        List<Address> addressesList = user.getAddresses();
        addressesList.add( newaddress );
        user.setAddresses(addressesList);

        Address savedAddress = addressRepository.save( newaddress );
        return modelMapper.map( savedAddress, AddressDTO.class );
    }

    @Override
    public List<AddressDTO> getAddressByUser()
    {
        User user = authUtil.loggedInUser();

        List<Address> addresses = addressRepository.getAddressesByUser( user.getUserId() );
        if( addresses == null )
        {
            throw new APIException( "No Address Found for this User !" );
        }
        List<AddressDTO> addressDTOList = addresses.stream().map( a -> {
                AddressDTO dto = modelMapper.map( a, AddressDTO.class );
                return dto;
        }).toList();

        return addressDTOList;
    }

    @Override
    public AddressDTO updateAddress( Long addressId, AddressDTO addressDTO )
    {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        addressFromDatabase.setCity( addressDTO.getCity() );
        addressFromDatabase.setPincode( addressDTO.getPincode() );
        addressFromDatabase.setState( addressDTO.getState() );
        addressFromDatabase.setCountry( addressDTO.getCountry() );
        addressFromDatabase.setStreet( addressDTO.getStreet() );
        addressFromDatabase.setBuilding( addressDTO.getBuilding() );

        Address updatedAddress = addressRepository.save( addressFromDatabase );

        User user = addressFromDatabase.getUser();
        user.getAddresses().removeIf( address -> address.getAddressId().equals(addressId) );
        user.getAddresses().add( updatedAddress );
        userRepository.save( user );

        return modelMapper.map( updatedAddress, AddressDTO.class );
    }

    @Override
    public String deleteAddress( Long addressId )
    {
        Address addressFromDatabase = addressRepository.findById( addressId )
                .orElseThrow( () -> new ResourceNotFoundException( "Address", "addressId", addressId ) );

        User user = addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals( addressId ) );
        userRepository.save( user );

        addressRepository.delete( addressFromDatabase );

        return "Address deleted successfully with addressId: " + addressId;
    }
}
