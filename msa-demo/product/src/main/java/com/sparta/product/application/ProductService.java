package com.sparta.product.application;

import com.sparta.product.application.dto.request.ProductDto;
import com.sparta.product.application.dto.response.ProductResponseDto;
import com.sparta.product.domain.entity.Product;
import com.sparta.product.domain.repository.ProductRepository;
import com.sparta.product.infrastructure.dto.StockMessage;
import com.sparta.product.infrastructure.dto.StockReduceSuccessResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final Logger LOGGER  = LoggerFactory.getLogger(ProductService.class);

    //private final ProductRepository productRepository;
    //private final ProductJpaRepository productJpaRepository;
    private final ProductRepository productRepository;

    public ProductResponseDto getProduct(UUID productId) {
        LOGGER.info("[getProduct] id {}", productId);
        //Product product = productJpaRepository.findById(number).get();
        Product product = productRepository.selectProduct(productId);
        LOGGER.info("[getProduct] product number : {}, name : {}", product.getName(), product.getName());

        return ProductResponseDto.from(product);
    }

    public ProductResponseDto saveProduct(ProductDto productDto) {
        Product product = productDto.toEntity();
        //Product savedProduct = productJpaRepository.save(product);
        Product savedProduct = productRepository.insertProduct(product);

        LOGGER.info("[saveProduct] saveProduct : {}", savedProduct);

        ProductResponseDto responseDto = ProductResponseDto.from(product);
        return responseDto;
    }

    public ProductResponseDto changeProductName(UUID productId, String name) throws Exception {
        // Product foundProduct = productJpaRepository.findById(number).get();
        //foundProduct.updateProduct(name);
        //Product changedProduct = productJpaRepository.save(foundProduct);
        Product changedProduct = productRepository.updateProduct(productId, name);
        ProductResponseDto responseDto = ProductResponseDto.from(changedProduct);

        return responseDto;
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public StockReduceSuccessResponseDto reduceStock(StockMessage stockMessage) {
        Product product = productRepository.selectProduct(stockMessage.getProductId());

        product.reduceStock(stockMessage.getQuantity());

        product = productRepository.saveAndFlushProduct(product);

        StockReduceSuccessResponseDto responseDto =
            StockReduceSuccessResponseDto.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(stockMessage.getQuantity())
                .orderId(stockMessage.getOrderId())
                .sagaId(stockMessage.getSagaId())
                .build();

    return responseDto;
    }

    public void deleteProduct(UUID productId) throws Exception {
        //productJpaRepository.deleteById(number);
        productRepository.deleteProduct(productId);
    }
}
