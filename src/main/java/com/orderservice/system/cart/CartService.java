package com.orderservice.system.cart;

import com.orderservice.system.products.ProductNotFoundException;
import com.orderservice.system.products.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductsRepository productsRepository;

    public CartResponseDto createCart() {
        var entity = cartRepository.save(new CartEntity());
        return cartMapper.toDto(entity);
    }

    public CartResponseDto getCartDetails(ObjectId cartId) {
        var cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        return cartMapper.toDto(cart);
    }

    public CartItemsDto addToCart(ObjectId cartId, String productId) {
        var cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        var product = productsRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        var cartItem = cart.updateOrAddCartItem(product);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public void deleteCartItem(ObjectId cartId, String productId) {
        var cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        cart.removeFromCart(new ObjectId(productId));
        cartRepository.save(cart);
    }

    public CartItemsDto updateCartItem(ObjectId cartId, String productId, int quantity) {
        var cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        var cartItem = cart.getCartItems(new ObjectId(productId));
        if (cartItem == null) throw new ProductNotFoundException();
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public void clearCart(ObjectId cartId) {
        var cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        cart.clearCart();
        cartRepository.save(cart);
    }
}
