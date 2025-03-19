package com.project.sb_ecommerce.controller;

import com.project.sb_ecommerce.DTOs.Requests.OrderDTO;
import com.project.sb_ecommerce.DTOs.Requests.OrderRequestDTO;
//import com.project.sb_ecommerce.payload.StripePaymentDto;
import com.project.sb_ecommerce.model.Enums.PaymentMethod;
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//import com.ecommerce.project.service.StripeService;
import com.project.sb_ecommerce.service.OrderService;
import com.project.sb_ecommerce.Utilities.AuthUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController
{
    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;

//    @Autowired
//    private StripeService stripeService;

    @PostMapping("/user/order/payment")
    public ResponseEntity<OrderDTO> orderProducts( @RequestBody OrderRequestDTO orderRequestDTO ) throws MessagingException
    {
        String userEmail = authUtil.loggedInEmail();
        System.out.println("orderRequestDTO DATA: " + orderRequestDTO);
        OrderDTO order = orderService.placeOrder(
                userEmail,
                orderRequestDTO.getAddressId(),
                orderRequestDTO.getPaymentMethod(),
                orderRequestDTO.getPaymentGatewayName(),
                orderRequestDTO.getPaymentTransactionId(),
                orderRequestDTO.getPaymentGatewayStatus(),
                orderRequestDTO.getPaymentGatewayResponseMessage()
        );
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

 /*   @PostMapping("/order/stripe-client-secret")
    public ResponseEntity<String> createStripeClientSecret(@RequestBody StripePaymentDto stripePaymentDto) throws StripeException {
            PaymentIntent paymentIntent = stripeService.paymentIntent(stripePaymentDto);
            return new ResponseEntity<>(paymentIntent.getClientSecret(), HttpStatus.CREATED);
        }

  */
}