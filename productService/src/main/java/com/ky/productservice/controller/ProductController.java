package com.ky.productservice.controller;

import com.ky.productservice.dto.CommentDto;
import com.ky.productservice.dto.ProdDto;
import com.ky.productservice.request.create.CreateProductRequest;
import com.ky.productservice.request.update.UpdateProductRequest;
import com.ky.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdDto> getProdDtoById(@PathVariable Integer id){
        return ResponseEntity.ok(productService.getProductDtoById(id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Integer id){
        return ResponseEntity.ok(productService.getComments(id));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProdDto>> getAll(){
        return ResponseEntity.ok(productService.getAll());
    }


    @GetMapping("/isInStock/{id}")
    public ResponseEntity<Boolean> isInStock(@PathVariable Integer id){
        return ResponseEntity.ok(productService.isInStock(id));
    }

    @GetMapping("/reduceStock/{id}")
    public ResponseEntity<String> reduceStock(@PathVariable Integer id,
                                       @RequestParam Integer qty){
        return ResponseEntity.ok(productService.reduceStock(id, qty));
    }

    @PostMapping("/createProduct")
    public ResponseEntity<ProdDto> createProduct(@RequestBody CreateProductRequest request){
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ProdDto> updateProduct(@Valid @RequestBody UpdateProductRequest request,
                                                 @PathVariable Integer id){
        return ResponseEntity.ok(productService.updateProduct(request, id));
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
