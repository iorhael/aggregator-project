package com.senla.aggregator.service.product;

import com.senla.aggregator.dto.product.ProductDetailedViewDto;
import com.senla.aggregator.dto.product.ProductSimpleViewDto;
import com.senla.aggregator.dto.product.ProductUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductSimpleViewDto createProduct(ProductSimpleViewDto product);

    List<ProductDetailedViewDto> getAllProducts(int pageNo, int pageSize);

    ProductSimpleViewDto updateProduct(ProductUpdateDto product, UUID id);

    ProductSimpleViewDto customizeProduct(ProductUpdateDto product, UUID id);

    void deleteProduct(UUID id);
}
