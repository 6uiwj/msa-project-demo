package com.sparta.order.application.service;

import com.sparta.order.infrastructure.dto.ProductDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/v1/products/{productId}")
    ResponseEntity<ProductDto> getProduct(@PathVariable("productId") UUID productId);
}
