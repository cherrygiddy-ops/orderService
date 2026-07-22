package com.orderservice.system.cart;

import com.orderservice.system.products.ProductNotFoundException;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("orders/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponseDto> createCart() {
        var response = cartService.createCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{cartId}")
    public CartResponseDto getCartDetails(@PathVariable("cartId") String cartId) {
        return cartService.getCartDetails(new ObjectId(cartId));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addToCart(@PathVariable("cartId") String cartId,
                                       @RequestBody AddToCartRequest request) {
        var response = cartService.addToCart(new ObjectId(cartId), String.valueOf(request.getProductId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(@PathVariable("cartId") String cartId,
                                            @PathVariable("productId") String productId,
                                            @RequestBody UpdateCartItemRequest request) {
        var response = cartService.updateCartItem(new ObjectId(cartId), productId, request.getQuantity());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("cartId") String cartId,
                                            @PathVariable("productId") String productId) {
        cartService.deleteCartItem(new ObjectId(cartId), productId);
        return ResponseEntity.ok("Product Deleted");
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable("cartId") String cartId) {
        cartService.clearCart(new ObjectId(cartId));
        return ResponseEntity.ok().body("Cart Cleared");
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<?> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }
}
