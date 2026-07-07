package com.example.SpringEcom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringEcom.model.Order;
import com.example.SpringEcom.model.OrderItem;
import com.example.SpringEcom.model.dto.OrderItemRequest;
import com.example.SpringEcom.model.dto.OrderItemResponse;
import com.example.SpringEcom.model.dto.OrderRequest;
import com.example.SpringEcom.model.dto.OrderResponse;
import com.example.SpringEcom.repo.OrderRepo;
import com.example.SpringEcom.repo.ProductRepo;
import com.example.SpringEcom.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.management.RuntimeErrorException;

@Service
public class OrderService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderRepo orderRepo;

    public OrderResponse placeOrder(OrderRequest request) {
        
        Order order = new Order();

        String generatedReceipt = "ORDER_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setOrderId(generatedReceipt);

        order.setCustomerName(request.customerName());
        order.setEmail(request.email());
        
        order.setStatus("Order Placed");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> finalOrderItems = new ArrayList<>();

        for(OrderItemRequest itemFromRequest : request.items()){
            Product product = productRepo.findById(itemFromRequest.productId())
            .orElseThrow(() -> new RuntimeException("Product Not Found"));

            product.setStockQuantity(product.getStockQuantity() - itemFromRequest.quantity());
            productRepo.save(product);

            BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(itemFromRequest.quantity()));

            OrderItem finalItem = OrderItem.builder()
                .product(product)
                .quantity(itemFromRequest.quantity())
                .totalPrice(totalPrice)
                .order(order)
                .build();
        
            finalOrderItems.add(finalItem);
        }

        order.setOrderItems(finalOrderItems);
    
        Order savedOrder = orderRepo.save(order);

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for(OrderItem item : order.getOrderItems()) {
            OrderItemResponse orderItemResponse = new OrderItemResponse(
                item.getProduct().getName(),
                item.getQuantity(),
                item.getTotalPrice()
            );
            itemResponses.add(orderItemResponse);
        }

        OrderResponse orderResponse = new OrderResponse(
            savedOrder.getOrderId(),
            savedOrder.getCustomerName(),
            savedOrder.getEmail(),
            savedOrder.getStatus(),
            savedOrder.getOrderDate(), 
            itemResponses
        );

        return orderResponse; 
    }

    public List<OrderResponse> getAllOrderResponses() {
            
        List<Order> allOrders = orderRepo.findAll();
        
        List<OrderResponse> finalResponses = new ArrayList<>();

        for (Order order : allOrders) {
            
            List<OrderItemResponse> itemResponses = new ArrayList<>();

            for (OrderItem item : order.getOrderItems()) {
                
                OrderItemResponse simpleItem = new OrderItemResponse(
                    item.getProduct().getName(), 
                    item.getQuantity(), 
                    item.getTotalPrice()
                );
                
                itemResponses.add(simpleItem);
            }

            OrderResponse simpleOrder = new OrderResponse(
                order.getOrderId(),
                order.getCustomerName(),
                order.getEmail(),
                order.getStatus(),
                order.getOrderDate(),
                itemResponses
            );

            finalResponses.add(simpleOrder);
        }

        return finalResponses;
    }
}