package com.orderservice.system.order;

import com.orderservice.system.products.ProductsEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderItemsEntity {

    @Field("product")
    private ProductsEntity product; // can embed product snapshot or just store productId

    @Field("quantity")
    private Integer quantity;

    @Field("unit_price")
    private BigDecimal unitPrice;

    @Field("total_price")
    private BigDecimal totalPrice;

    public OrderItemsEntity(ProductsEntity product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
        this.totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
