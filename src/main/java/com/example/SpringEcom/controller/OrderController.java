package com.example.SpringEcom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SpringEcom.model.dto.OrderRequest;
import com.example.SpringEcom.model.dto.OrderResponse;
import com.example.SpringEcom.service.OrderService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping("/orders/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
        
        OrderResponse response = service.placeOrder(orderRequest);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrderResponses() {
        
        List<OrderResponse> responses = service.getAllOrderResponses();
        
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}