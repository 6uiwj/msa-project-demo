package com.sparta.product.infrastructure;

import com.sparta.product.domain.entity.Product;
import com.sparta.product.domain.repository.ProductRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product insertProduct(Product product) {
        Product savedProduct = productJpaRepository.save(product);
        return savedProduct;
    }

    @Override
    public Product selectProduct(UUID productId) {
        Optional<Product> selectedProduct = productJpaRepository.findById(productId);
        if (selectedProduct.isPresent()) {
            Product product = selectedProduct.get();
            return product;
        } else throw new NoSuchElementException();
    }

    @Transactional
    @Override
    public Product updateProduct(UUID productId, String name) {
        Product product = productJpaRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        product.updateProduct(name);
        //@Transactional을 붙이면 엔티티의 내용이 변경되면 Dirty Checking 발생 -> 자동으로 save. 즉 save 메서드를 쓸 필요 없음
        //-> 단 @Transactional 은 서비스 레이어에 붙이는게 좋음

        //return productJpaRepository.save(product);
        return product;
    }

    @Override
    public void deleteProduct(UUID productId) {
        Product selectedProduct = productJpaRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        productJpaRepository.delete(selectedProduct);
    }

    @Override
    public Product saveAndFlushProduct(Product product) {
        return productJpaRepository.saveAndFlush(product);
    }
}
