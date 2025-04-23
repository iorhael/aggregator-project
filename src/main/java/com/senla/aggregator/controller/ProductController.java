package com.senla.aggregator.controller;

import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.product.ProductCreateDto;
import com.senla.aggregator.dto.product.ProductDetailedDto;
import com.senla.aggregator.dto.product.ProductFilterDto;
import com.senla.aggregator.dto.product.ProductInfoDto;
import com.senla.aggregator.dto.product.ProductNameDescriptionDto;
import com.senla.aggregator.dto.product.ProductPreviewDto;
import com.senla.aggregator.dto.product.ProductUpdateDto;
import com.senla.aggregator.model.Role;
import com.senla.aggregator.service.product.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.controller.helper.Messages.*;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
@Tag(name = "Products Resource", description = "CRUD, filtration and verification")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ProductDetailedDto getProduct(@PathVariable UUID id) {
        return productService.getProduct(id);
    }

    @PostMapping("/search")
    public List<ProductPreviewDto> searchProducts(@RequestBody ProductFilterDto dto,
                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                  @RequestParam(defaultValue = "15") int pageSize) {
        return productService.filterProducts(dto, pageNo, pageSize);
    }

    @GetMapping("/info/{categoryId}")
    public List<ProductNameDescriptionDto> getProductsInfo(@PathVariable UUID categoryId,
                                                           @RequestParam(defaultValue = "0") int pageNo,
                                                           @RequestParam(defaultValue = "15") int pageSize) {
        return productService.getProductsNameDescription(categoryId, pageNo, pageSize);
    }

    @GetMapping("/previews/{categoryId}")
    public List<ProductPreviewDto> getProductsPreviews(@PathVariable UUID categoryId,
                                                       @RequestParam(defaultValue = "0") int pageNo,
                                                       @RequestParam(defaultValue = "15") int pageSize) {
        return productService.getProductsPreviews(categoryId, pageNo, pageSize);
    }

    @PostMapping
    @PreAuthorize("hasRole('RETAILER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductInfoDto createProduct(@Valid @RequestBody ProductCreateDto product, Authentication authentication) {
        Boolean isCreatorTrusted = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(Role.ADMIN.getPrefixedRole()));

        return productService.createProduct(product, isCreatorTrusted);
    }

    @PatchMapping("/verification")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfoDto verifyProduct(@RequestBody List<UUID> productIds) {
        int verifiedCount = productService.verifyProducts(productIds);

        return ResponseInfoDto.builder()
                .message(String.format(PRODUCTS_VERIFICATION_MESSAGE, verifiedCount))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductInfoDto updateProduct(@RequestBody ProductUpdateDto product,
                                        @PathVariable UUID id) {
        return productService.updateProduct(product, id);
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
