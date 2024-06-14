package com.example.springboot.controllers;


import com.example.springboot.dto.ProductRecordDto;
import com.example.springboot.models.ProductsModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ProductControler {

    @Autowired
    ProductRepository productRepository;


    //MÉTODO PARA RECEBER OS ATRIBUTOS PARA SEREM SALVOS NO BANCO
    @PostMapping("/products")
    public ResponseEntity<ProductsModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto){
        var productsModel = new ProductsModel();
        BeanUtils.copyProperties(productRecordDto,productsModel); //fazer a cpnversão de DTO para Model
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productsModel));
    }

    //MÉTODO PARA RETORNAR DADOS DO BANCO
    @GetMapping("/products")
    public ResponseEntity<List<ProductsModel>> getallProducts(){
        return  ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    //MÉTODO PARA RETORNAR DADOS DO BANCO POR ID
    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id){
        Optional<ProductsModel> product0 = productRepository.findById(id);

        if (product0.isEmpty()){ //is.isEmpty (for vazio)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(product0.get());
    }

    //MÉTODO PARA ATUALIZAR
    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto){
        Optional<ProductsModel> product0 = productRepository.findById(id);

        if (product0.isEmpty()){ // (product.isEmpti for vazio...)
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        var productModel = product0.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }

    //MÉTODO PARA DELETAR
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable (value = "id") UUID id){
        Optional<ProductsModel> produtc0 = productRepository.findById(id);
        if (produtc0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        productRepository.delete(produtc0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product delete successfully");
    }


}

