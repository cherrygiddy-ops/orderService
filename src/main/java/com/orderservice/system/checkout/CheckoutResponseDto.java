package com.orderservice.system.checkout;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Data
public class CheckoutResponseDto {
    private Long orderId;
    private String stripeCheckoutUrl;
    private String PhoneNumber;
}
