package com.orderservice.system.order;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "paymentStatus", source = "paymentStatus")
    @Mapping(target = "deliveryStatus", source = "deliveryStatus")
    @Mapping(target = "cartId", expression = "java(map(order.getCart().getId()))")
    OrderResponseDto toDto(OrderEntity order);

    // Custom mapping method for ObjectId → String
    default String map(ObjectId value) {
        return value == null ? null : value.toHexString();
    }
}
