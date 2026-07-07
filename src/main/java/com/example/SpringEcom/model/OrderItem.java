package com.example.SpringEcom.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import com.example.SpringEcom.model.Order;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    private int quantity;
    private BigDecimal totalPrice;
}