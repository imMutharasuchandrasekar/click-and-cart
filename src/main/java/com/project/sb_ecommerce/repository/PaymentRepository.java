package com.project.sb_ecommerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.sb_ecommerce.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>
{
}