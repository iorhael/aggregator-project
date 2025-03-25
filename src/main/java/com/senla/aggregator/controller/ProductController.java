package com.senla.aggregator.controller;

import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.product.ProductDetailedViewDto;
import com.senla.aggregator.dto.product.ProductSimpleViewDto;
import com.senla.aggregator.dto.product.ProductUpdateDto;
import com.senla.aggregator.service.product.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.controller.ControllerMessages.*;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
@Tag(name = "Products Resource", description = "View and customize products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasAnyRole('RETAILER', 'AUTHOR')")
    public List<ProductDetailedViewDto> findAllProducts(@RequestParam(defaultValue = "0") int pageNo,
                                                        @RequestParam(defaultValue = "15") int pageSize) {
       return productService.getAllProducts(pageNo, pageSize);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductSimpleViewDto createProduct(@Valid @RequestBody ProductSimpleViewDto product) {
       return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductSimpleViewDto updateProduct(@Valid @RequestBody ProductUpdateDto product,
                                              @PathVariable UUID id) {
       return productService.updateProduct(product, id);
    }

    @PutMapping("/customize/{id}")
    @PreAuthorize("hasRole('RETAILER')")
    public ProductSimpleViewDto customizeProduct(@Valid @RequestBody ProductUpdateDto product,
                                                 @PathVariable UUID id) {
        return productService.customizeProduct(product, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfoDto deleteProduct(@PathVariable UUID id) {
       productService.deleteProduct(id);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, PRODUCT, id))
                .build();
    }
}
