package com.orderservice.system.checkout;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.UUID;

@Data
public class CheckoutRequestDto {
    @NotBlank(message = "cartID Required")
    private ObjectId cartId;
    private String paymentMethod;
    private String PhoneNumber;
}
