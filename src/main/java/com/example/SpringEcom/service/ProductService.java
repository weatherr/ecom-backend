package com.example.SpringEcom.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.SpringEcom.repo.ProductRepo;

import com.example.SpringEcom.model.Product;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    public List<Product> getAllProducts(){
        return repo.findAll();
    }

    public Product getProductById(int id) {
        return repo.findById(id).orElse(new Product(-1)); // product with id -1 = This is the fake dummy object!
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException{
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());

        return repo.save(product);
    }

    public void deleteById(int id){
        repo.deleteById(id);
    }

    @Transactional
    public List<Product> searchProducts(String keyword) {
        List<Product> foundProducts = repo.searchProducts(keyword);
        return foundProducts;
    }
}
