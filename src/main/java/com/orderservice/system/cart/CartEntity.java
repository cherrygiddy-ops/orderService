package com.orderservice.system.cart;

import com.orderservice.system.products.ProductsEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "cart")
public class CartEntity {

    @Id
    private ObjectId id;   // ✅ MongoDB ObjectId
    private String userId;
    private Date createdAt = new Date();

    // Embedded cart items
    private Set<CartItemsEntity> items = new LinkedHashSet<>();

    public void addToCart(CartItemsEntity cartItem) {
        items.add(cartItem);
    }

    public void removeFromCart(ObjectId productId) {
        var cartItem = getCartItems(productId);
        if (cartItem != null) {
            items.remove(cartItem);
        }
    }

    public void clearCart() {
        items.clear();
    }

    public CartItemsEntity getCartItems(ObjectId productId) {
        return items.stream()
                .filter(carti -> carti.getProduct().getId().equals(productId.toHexString()))
                .findFirst()
                .orElse(null);
    }

    public CartItemsEntity updateOrAddCartItem(ProductsEntity product) {
        var cartItem = getCartItems(new ObjectId(product.getId()));
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItemsEntity();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            this.addToCart(cartItem);
        }
        return cartItem;
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItemsEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
