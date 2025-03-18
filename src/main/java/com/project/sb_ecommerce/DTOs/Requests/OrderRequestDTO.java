package com.project.sb_ecommerce.DTOs.Requests;

import com.project.sb_ecommerce.model.Enums.PaymentMethod;
import com.project.sb_ecommerce.model.Enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO
{
    private Long addressId;
    private PaymentMethod paymentMethod;
    private String paymentGatewayName;
    private String paymentTransactionId;
    private PaymentStatus paymentGatewayStatus;
    private String paymentGatewayResponseMessage;
}