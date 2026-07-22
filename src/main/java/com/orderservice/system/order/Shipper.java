package com.orderservice.system.order;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "shippers")
public class Shipper {

    @Id
    private Byte id;   // You can switch to UUID or ObjectId if preferred

    private String name;
}
