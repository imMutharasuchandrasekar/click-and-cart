package com.project.sb_ecommerce.DTOs.Requests;

import com.project.sb_ecommerce.model.Enums.PaymentMethod;
import com.project.sb_ecommerce.model.Enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO
{
    private Long paymentId;
    private PaymentMethod paymentMethod;
    private String paymentGatewayName;
    private String PaymentTransactionId;
    private PaymentStatus paymentGatewayStatus;
    private String paymentGatewayResponseMessage;
}
