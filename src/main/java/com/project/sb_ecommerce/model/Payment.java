package com.project.sb_ecommerce.model;

import com.project.sb_ecommerce.model.Enums.PaymentMethod;
import com.project.sb_ecommerce.model.Enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    // Name of the gateway like stripe, phonepay, razorpay etcs..
    private String paymentGatewayName;

    // The reference Id returned by the payment gateway for the transaction.
    private String paymentTransactionId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentGatewayStatus;

    private String paymentGatewayResponseMessage;


    public Payment( PaymentMethod paymentMethod, String paymentGatewayName, String paymentTransactionId,
                    PaymentStatus paymentGatewayStatus, String paymentGatewayResponseMessage )
    {
        this.paymentMethod = paymentMethod;
        this.paymentGatewayName = paymentGatewayName;
        this.paymentTransactionId = paymentTransactionId;
        this.paymentGatewayStatus = paymentGatewayStatus;
        this.paymentGatewayResponseMessage = paymentGatewayResponseMessage;
    }
}
