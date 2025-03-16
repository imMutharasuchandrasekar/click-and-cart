package com.project.sb_ecommerce.repository;

import com.project.sb_ecommerce.model.Address;
import com.project.sb_ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a from Address a WHERE a.user.userId = ?1")
    List<Address> getAddressesByUser(Long userId);
}
