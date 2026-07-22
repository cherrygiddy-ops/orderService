package com.orderservice.system.cart;

import com.orderservice.system.products.ProductsEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class CartItemsEntity {

    // Mongo will embed this inside CartEntity
    @Field("product")
    private ProductsEntity product;

    @Field("quantity")
    private Integer quantity;

    public BigDecimal getTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
