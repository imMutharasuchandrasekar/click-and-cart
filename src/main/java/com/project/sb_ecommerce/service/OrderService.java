package com.project.sb_ecommerce.service;

import com.project.sb_ecommerce.DTOs.Requests.OrderDTO;
import com.project.sb_ecommerce.model.Enums.PaymentMethod;
import com.project.sb_ecommerce.model.Enums.PaymentStatus;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

public interface OrderService
{
    @Transactional
    OrderDTO placeOrder(String email, Long addressId, PaymentMethod paymentMethod, String paymentGatewayName, String paymentTransactionId, PaymentStatus paymentGatewayStatus, String paymentGatewayResponseMessage) throws MessagingException;
}
