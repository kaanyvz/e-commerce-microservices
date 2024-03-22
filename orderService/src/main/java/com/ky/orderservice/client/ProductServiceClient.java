package com.ky.orderservice.client;

import com.ky.orderservice.dto.feignDtos.ProdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "productService",
        path = "/v1/products"
)
public interface ProductServiceClient {

    @GetMapping("/{id}")
    ResponseEntity<ProdDto> getProdDtoById(@PathVariable Integer id);

    @GetMapping("/isInStock/{id}")
    ResponseEntity<Boolean> isInStock(@PathVariable Integer id);

    @GetMapping("/reduceStock/{id}")
    ResponseEntity<String> reduceStock(@PathVariable Integer id,
                                              @RequestParam Integer qty);
}
