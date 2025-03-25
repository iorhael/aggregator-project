package com.senla.aggregator.controller;

import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.productCard.ProductCardCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardGetDto;
import com.senla.aggregator.dto.productCard.ProductCardUpdateDto;
import com.senla.aggregator.service.productCard.ProductCardService;
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

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.controller.ControllerMessages.*;

@RestController
@RequestMapping("api/product_cards")
@RequiredArgsConstructor
@Tag(name = "Products Cards Resource", description = "View and customize product cards")
public class ProductCardController {

    private final ProductCardService productCardService;

    @GetMapping("/retailer/{retailerName}")
    public List<ProductCardGetDto> findAllProductCardsOfRetailer(@RequestParam(defaultValue = "0") int pageNo,
                                                                 @RequestParam(defaultValue = "15") int pageSize,
                                                                 @PathVariable String retailerName) {
        return productCardService.getAllProductCardsOfRetailer(retailerName, pageNo, pageSize);
    }

    @GetMapping("/search")
    public List<ProductCardGetDto> searchProductCards(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String retailerName,
            @RequestParam(required = false) String discount,
            @RequestParam(required = false) Boolean installmentAvailable,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String ram,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String strap,
            @RequestParam(required = false) String warranty,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "15") int pageSize
    ) {
        return productCardService.getProductCardsBySpecification(
                productName,
                retailerName,
                discount,
                installmentAvailable,
                color,
                ram,
                size,
                strap,
                warranty,
                pageNo,
                pageSize
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('RETAILER')")
    public List<ProductCardGetDto> findMyProductCards(@RequestParam(defaultValue = "0") int pageNo,
                                                      @RequestParam(defaultValue = "15") int pageSize,
                                                      Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        return productCardService.getMyProductCards(ownerId, pageNo, pageSize);
    }

    @PostMapping
    @PreAuthorize("hasRole('RETAILER')")
    public ProductCardGetDto createProduct(@Valid @RequestBody ProductCardCreateDto product,
                                           Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        return productCardService.createProductCard(product, ownerId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RETAILER')")
    public ProductCardGetDto updateProduct(@Valid @RequestBody ProductCardUpdateDto product,
                                           @PathVariable UUID id,
                                           Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        return productCardService.updateProductCard(product, id, ownerId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto deleteProduct(@PathVariable UUID id, Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        productCardService.deleteProductCard(id, ownerId);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, PRODUCT_CARD, id))
                .build();
    }
}
