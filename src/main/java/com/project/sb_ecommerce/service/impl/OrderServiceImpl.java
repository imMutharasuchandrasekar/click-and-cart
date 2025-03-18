package com.project.sb_ecommerce.service.impl;

import com.project.sb_ecommerce.DTOs.Requests.OrderDTO;
import com.project.sb_ecommerce.DTOs.Requests.OrderItemDTO;
import com.project.sb_ecommerce.exceptions.APIException;
import com.project.sb_ecommerce.exceptions.ResourceNotFoundException;
import com.project.sb_ecommerce.model.*;
import com.project.sb_ecommerce.model.Enums.OrderStatus;
import com.project.sb_ecommerce.model.Enums.PaymentMethod;
import com.project.sb_ecommerce.model.Enums.PaymentStatus;
import com.project.sb_ecommerce.repository.*;
import com.project.sb_ecommerce.service.CartService;
import com.project.sb_ecommerce.service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Service
public class OrderServiceImpl implements OrderService
{
    @Autowired
    CartRepository cartRepository;

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;

    @Autowired
    ModelMapper modelMapper;


    @Transactional
    @Override
    public OrderDTO placeOrder( String userEmail, Long addressId,
                               PaymentMethod paymentMethod, String paymentGatewayName,
                               String paymentTransactionId, PaymentStatus paymentGatewayStatus,
                               String paymentGatewayResponseMessage ) throws ResourceNotFoundException, APIException
    {
        Cart userCart = cartRepository.findCartByEmail( userEmail );
        if ( userCart == null )
            throw new ResourceNotFoundException( "Cart", "email", userEmail );

        Address userAddress = addressRepository.findById(addressId).orElseThrow( () ->
                new APIException( "No Address Found for the user" ) );

        List<CartItem> cartItems = userCart.getCartItems();
        if ( cartItems.isEmpty() )
            throw new APIException( "User's cart is empty" );

        Order newOrder = new Order(
                userEmail, LocalDate.now(), userCart.getTotalPrice(), OrderStatus.PLACED, userAddress );

        Payment paymentObj = new Payment( paymentMethod,
                paymentGatewayName, paymentTransactionId, paymentGatewayStatus, paymentGatewayResponseMessage );
        paymentObj.setOrder(newOrder);
        Payment savedPayment = paymentRepository.save( paymentObj );

        newOrder.setPayment( savedPayment );
        Order savedOrder = orderRepository.save( newOrder );

        List<OrderItem> orderItems = new ArrayList<>();
        for ( CartItem cartItem : cartItems )
        {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct( cartItem.getProduct() );
            orderItem.setQuantity( cartItem.getQuantity() );
            orderItem.setDiscount( cartItem.getProductPrice() );
            orderItem.setOrderedProductPrice( cartItem.getProductPrice() );
            orderItem.setOrder( savedOrder );
            orderItems.add( orderItem );
        }

        orderItems = orderItemRepository.saveAll( orderItems );
        List<CartItem> cartItemsToRemove = new ArrayList<>();

        userCart.getCartItems().forEach( item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            // Reduce stock quantity
            product.setQuantity( product.getQuantity() - quantity );
            // Save product back to the database
            productRepository.save( product );
            cartItemsToRemove.add( item );
        });

        // Remove items from cart
        cartItemsToRemove.forEach(item ->
                cartService.deleteProductFromCart( userCart.getCartId(), item.getProduct().getProductId() ) );

        OrderDTO orderDTO = modelMapper.map( savedOrder, OrderDTO.class );
        orderItems.forEach( item -> orderDTO.getOrderItems()
                .add( modelMapper.map( item, OrderItemDTO.class ) ) );

        orderDTO.setAddressId( addressId );
        return orderDTO;
    }
}

