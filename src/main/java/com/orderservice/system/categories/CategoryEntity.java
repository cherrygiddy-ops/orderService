package com.orderservice.system.categories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@Document(collection = "categories")
public class CategoryEntity {

    @Id
    private Byte id;   // You can use String or ObjectId if preferred

    private String name;
}
