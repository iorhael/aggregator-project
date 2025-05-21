package com.senla.aggregator.controller;

import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.productCard.ProductCardCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardDetailedDto;
import com.senla.aggregator.dto.productCard.ProductCardFilterDto;
import com.senla.aggregator.dto.productCard.ProductCardPreviewDto;
import com.senla.aggregator.dto.productCard.ProductCardUpdateDto;
import com.senla.aggregator.service.productCard.ProductCardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.controller.helper.Constants.*;

@RestController
@RequestMapping("api/product_cards")
@RequiredArgsConstructor
@Tag(name = "Product Cards Resource", description = "CRUD and filtration")
public class ProductCardController {

    private final ProductCardService productCardService;

    @GetMapping("/{id}")
    public ProductCardDetailedDto findProductCard(@PathVariable UUID id) {
        return productCardService.getProductCard(id);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('RETAILER')")
    public List<ProductCardDetailedDto> findRetailerProductCards(@RequestParam(defaultValue = "0") int pageNo,
                                                                 @RequestParam(defaultValue = "15") int pageSize,
                                                                 Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        return productCardService.getRetailerProductCards(ownerId, pageNo, pageSize);
    }

    @PostMapping("/search/{productId}")
    public List<ProductCardPreviewDto> searchProductCards(@RequestBody ProductCardFilterDto dto,
                                                          @PathVariable UUID productId,
                                                          @RequestParam(defaultValue = "0") int pageNo,
                                                          @RequestParam(defaultValue = "15") int pageSize) {
        return productCardService.filterProductCards(dto, productId, pageNo, pageSize);
    }

    @PostMapping
    @PreAuthorize("hasRole('RETAILER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCardPreviewDto createProductCard(@Valid @RequestBody ProductCardCreateDto product,
                                                   Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        return productCardService.createProductCard(product, ownerId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RETAILER')")
    public ProductCardDetailedDto updateProductCard(@Valid @RequestBody ProductCardUpdateDto product,
                                                    @PathVariable UUID id,
                                                    Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        return productCardService.updateProductCard(product, id, ownerId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto deleteProductCard(@PathVariable UUID id, Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        productCardService.deleteProductCard(id, ownerId);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, PRODUCT_CARD, id))
                .build();
    }

    @DeleteMapping("/all")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto deleteAllProductCards(Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());
        int count = productCardService.deleteAllProductCards(ownerId);

        return ResponseInfoDto.builder()
                .message(String.format(PRODUCT_CARDS_DELETION_MESSAGE, count))
                .build();
    }
}
