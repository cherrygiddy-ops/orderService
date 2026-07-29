package com.orderservice.system.checkout;

import com.orderservice.system.cart.CartNotFoundException;
import com.orderservice.system.order.CartEmptyException;
import com.orderservice.system.order.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders/checkout")
public class CheckoutController {
    private final CheckOutService checkOutService;

    @PostMapping()
    public CheckoutResponseDto checkout(@RequestBody CheckoutRequestDto requestDto) {
        return checkOutService.placeOrder(requestDto.getCartId(), requestDto.getPhoneNumber());
    }



    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<?> handleCartEmpty (){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is Empty");
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<?> handleCartNotFound (){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart Not Found");
    }

    @ExceptionHandler(OrderAlreadyUpdatedException.class)
    public ResponseEntity<?> handleOrderPaidException (){
        return ResponseEntity.status(HttpStatus.FOUND).body("Order  Already Updated");
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundException (){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order  Not Found");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handlePaymentServiceNotFoundException (){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unsupported payment gateway: ");
    }
}
