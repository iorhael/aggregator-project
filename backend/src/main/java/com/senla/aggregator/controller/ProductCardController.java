package com.senla.aggregator.controller;

import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.productCard.ProductCardCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardDetailedDto;
import com.senla.aggregator.dto.productCard.ProductCardFilterDto;
import com.senla.aggregator.dto.productCard.ProductCardPreviewDto;
import com.senla.aggregator.dto.productCard.ProductCardUpdateDto;
import com.senla.aggregator.model.ProductCard_;
import com.senla.aggregator.service.productCard.ProductCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import java.util.UUID;

import static com.senla.aggregator.controller.helper.Constants.*;

@RestController
@RequestMapping("api/product_cards")
@RequiredArgsConstructor
@Tag(name = "Product Cards Resource", description = "CRUD and filtration for product cards")
public class ProductCardController {

    private final ProductCardService productCardService;

    @Operation(
            summary = "Get product card by ID",
            description = "Returns detailed information about a specific product card by its ID."
    )
    @GetMapping("/{id}")
    public ProductCardDetailedDto findProductCard(@PathVariable UUID id) {
        return productCardService.getProductCard(id);
    }

    @Operation(
            summary = "Get my product cards",
            description = "Returns all product cards created by the current retailer (paginated)."
    )
    @GetMapping("/my")
    @PreAuthorize("hasRole('RETAILER')")
    public PaginationStatsDto<ProductCardDetailedDto> findMyProductCards(@PageableDefault(
                                                                                 sort = ProductCard_.UPDATED_AT,
                                                                                 direction = Sort.Direction.DESC,
                                                                                 size = 15) Pageable pageable,
                                                                         Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());
        return productCardService.getRetailerProductCards(ownerId, pageable);
    }

    @GetMapping("/my/count")
    @PreAuthorize("hasRole('RETAILER')")
    public long getMyProductCardsCount(Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());
        return productCardService.countRetailerProductCards(ownerId);
    }

    @Operation(
            summary = "Filter product cards by product ID",
            description = "Applies filter criteria to product cards by product ID and returns matching results (paginated)."
    )
    @PostMapping("/search/{productId}")
    public PaginationStatsDto<ProductCardPreviewDto> searchProductCards(@RequestBody ProductCardFilterDto dto,
                                                                        @PathVariable UUID productId,
                                                                        @RequestParam(defaultValue = "0") int pageNo,
                                                                        @RequestParam(defaultValue = "15") int pageSize) {
        return productCardService.filterProductCards(dto, productId, pageNo, pageSize);
    }

    @Operation(
            summary = "Create product card",
            description = "Creates a new product card for the current retailer."
    )
    @PostMapping
    @PreAuthorize("hasRole('RETAILER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCardPreviewDto createProductCard(@Valid @RequestBody ProductCardCreateDto product,
                                                   Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());
        return productCardService.createProductCard(product, ownerId);
    }

    @Operation(
            summary = "Update product card",
            description = "Updates the details of a specific product card for the current retailer."
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RETAILER')")
    public ProductCardDetailedDto updateProductCard(@Valid @RequestBody ProductCardUpdateDto product,
                                                    @PathVariable UUID id,
                                                    Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());
        return productCardService.updateProductCard(product, id, ownerId);
    }

    @Operation(
            summary = "Delete product card",
            description = "Deletes a specific product card owned by the current retailer."
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto deleteProductCard(@PathVariable UUID id, Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());
        productCardService.deleteProductCard(id, ownerId);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, PRODUCT_CARD, id))
                .build();
    }

    @Operation(
            summary = "Delete all product cards",
            description = "Deletes all product cards owned by the current retailer."
    )
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
