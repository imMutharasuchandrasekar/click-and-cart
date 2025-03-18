package com.project.sb_ecommerce.service.impl;

import com.project.sb_ecommerce.DTOs.Requests.CartDTO;
import com.project.sb_ecommerce.DTOs.Requests.ProductDTO;
import com.project.sb_ecommerce.Utilities.AuthUtil;
import com.project.sb_ecommerce.exceptions.APIException;
import com.project.sb_ecommerce.exceptions.ResourceNotFoundException;
import com.project.sb_ecommerce.model.Cart;
import com.project.sb_ecommerce.model.CartItem;
import com.project.sb_ecommerce.model.Product;
import com.project.sb_ecommerce.repository.CartItemRepository;
import com.project.sb_ecommerce.repository.CartRepository;
import com.project.sb_ecommerce.repository.ProductRepository;
import com.project.sb_ecommerce.service.CartService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService
{
    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    private ModelMapper modelMapper;

    private static final String ADD = "add";
    private static final String SUBTRACT = "subtract";

    @Override
    public CartDTO addProductToCart( Long productId, Integer quantity )
    {
        Cart cart  = retiveOrCreateCart();

        Product productInDb = productRepository.findById( productId )
                .orElseThrow(() -> new ResourceNotFoundException( "Product", "productId", productId ) );

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId( cart.getCartId(), productId );
        if ( cartItem != null )
        {
            throw new APIException( "Product " + productInDb.getProductName() + " already exists in the cart" );
        }

        if ( productInDb.getQuantity() == 0 )
        {
            throw new APIException( productInDb.getProductName() + " is not available" );
        }

        if ( productInDb.getQuantity() < quantity )
        {
            throw new APIException( "Please, make an order of the " + productInDb.getProductName()
                    + " less than or equal to the quantity " + productInDb.getQuantity() + "." );
        }

        CartItem newCartItem = new CartItem();

        newCartItem.setProduct( productInDb );
        newCartItem.setCart( cart );
        newCartItem.setQuantity( quantity );
        newCartItem.setProductPrice( productInDb.getSpecialPrice() );

        cartItemRepository.save( newCartItem );

        cart.getCartItems().add( newCartItem );
        cart.setTotalPrice( cart.getTotalPrice() + ( productInDb.getSpecialPrice() * quantity ) );
        cartRepository.save( cart );

        CartDTO cartDTO = modelMapper.map( cart, CartDTO.class );
        List<ProductDTO> productDTOList = constructProductDTOListFromCartItems( cart );
        cartDTO.setProducts( productDTOList );

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts()
    {
        List<Cart> carts = cartRepository.findAll();
        if ( carts.size() == 0 )
        {
            throw new APIException( "No cart exists" );
        }

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> products = constructProductDTOListFromCartItems( cart );
            cartDTO.setProducts( products );
            return cartDTO;
        }).toList();

        return cartDTOs;
    }

    @Override
    public CartDTO getLoggedInUserCart()
    {
        String loggedInUserEmail = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail( loggedInUserEmail );

        if( userCart == null )
        {
            throw new APIException( "No Cart was found for the logged in user." );
        }

        CartDTO responseCartDTO = modelMapper.map( userCart, CartDTO.class );
        userCart.getCartItems().forEach( ci -> ci.getProduct().setQuantity( ci.getQuantity() ));

        List<ProductDTO> productDTOList = constructProductDTOListFromCartItems( userCart );
        responseCartDTO.setProducts( productDTOList );
        return responseCartDTO;
    }

    @Override
    public CartDTO updateProductQuantity( Long productId, String operation )
    {
        String loggedInUserEmail = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail( loggedInUserEmail );

        CartItem savedCartItem = cartItemRepository.findCartItemByProductIdAndCartId( userCart.getCartId(), productId );
        Product productInDb = productRepository.findById( productId ).orElseThrow(
                () -> new ResourceNotFoundException( "Product", "productId", productId ) );

        if( ADD.equals( operation ) )
        {
            if( productInDb.getQuantity() >= 1 )
            {
                savedCartItem.setQuantity( savedCartItem.getQuantity() + 1 );
                userCart.setTotalPrice( userCart.getTotalPrice() + productInDb.getSpecialPrice() );
            }
            else
            {
                throw new APIException( "The specified product is out of stock" );
            }
        }
        else if( SUBTRACT.equals( operation ) )
        {
            if( savedCartItem.getQuantity() > 1 )
            {
                savedCartItem.setQuantity( savedCartItem.getQuantity() - 1 );
                userCart.setTotalPrice( userCart.getTotalPrice() - productInDb.getSpecialPrice() );
            }
            else
                throw new APIException( "Are you sure you want to remove the product from the cart ?" );
        }

        cartItemRepository.save( savedCartItem );
        Cart updatedCart = cartRepository.save( userCart );

        CartDTO updatedCartDTO = modelMapper.map( updatedCart, CartDTO.class );

        updatedCart.getCartItems().forEach( ci -> ci.getProduct().setQuantity( ci.getQuantity() ));
        List<ProductDTO> productDTOList = constructProductDTOListFromCartItems( updatedCart );
        updatedCartDTO.setProducts( productDTOList );
        return updatedCartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart( Long cartId, Long productId )
    {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow( () -> new ResourceNotFoundException( "Cart", "cartId", cartId ) );
        Product productInDb = productRepository.findById( productId ).orElseThrow(
                () -> new ResourceNotFoundException( "Product", "productId", productId ) );

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId( cartId, productId );

        if ( cartItem == null )
        {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        productInDb.setQuantity( productInDb.getQuantity()  + cartItem.getQuantity() );
        productRepository.save( productInDb );

        cart.setTotalPrice( cart.getTotalPrice() -
                ( cartItem.getProductPrice() * cartItem.getQuantity() ) );
        cartRepository.save( cart );

        cartItemRepository.deleteCartItemByProductIdAndCartId( cartId, productId );

        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
    }

    private List<ProductDTO> constructProductDTOListFromCartItems( Cart cart )
    {
        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map( item.getProduct(), ProductDTO.class );
            map.setQuantity( item.getQuantity() );
            return map;
        });
        return  productStream.toList();
    }

    private Cart retiveOrCreateCart()
    {
        Cart userCart  = cartRepository.findCartByEmail( authUtil.loggedInEmail() );
        if(userCart != null){
            return userCart;
        }

        Cart cart = new Cart( authUtil.loggedInUser(), 0.00 );
        Cart newCart = cartRepository.save( cart );
        return newCart;
    }
}
