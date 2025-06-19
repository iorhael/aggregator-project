package com.senla.aggregator.controller;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.product.ProductCreateDto;
import com.senla.aggregator.dto.product.ProductDetailedDto;
import com.senla.aggregator.dto.product.ProductFilterDto;
import com.senla.aggregator.dto.product.ProductInfoDto;
import com.senla.aggregator.dto.product.ProductNameDescriptionProjection;
import com.senla.aggregator.dto.product.ProductPreviewDto;
import com.senla.aggregator.dto.product.ProductPreviewProjection;
import com.senla.aggregator.dto.product.ProductUpdateDto;
import com.senla.aggregator.model.Role;
import com.senla.aggregator.service.product.ProductService;
import com.senla.aggregator.validation.fileTypes.AllowedFileTypes;
import com.senla.aggregator.validation.validImage.ValidImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.controller.helper.Constants.*;

@Validated
@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
@Tag(name = "Products Resource", description = "CRUD, filtration and verification")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Get product by ID",
            description = "Returns detailed information about a product including rating, characteristics and offers"
    )
    @GetMapping("/{id}")
    public ProductDetailedDto getProduct(@PathVariable UUID id) {
        return productService.getProduct(id);
    }

    @Operation(
            summary = "Filter products",
            description = "Performs filtering based on provided product name, vendor, category, rating, and characteristics"
    )
    @PostMapping("/search")
    public PaginationStatsDto<ProductPreviewDto> searchProducts(@RequestBody ProductFilterDto dto,
                                                                @RequestParam(defaultValue = "0") int pageNo,
                                                                @RequestParam(defaultValue = "15") int pageSize) {
        return productService.filterProducts(dto, pageNo, pageSize);
    }

    @Operation(
            summary = "Get paginated products",
            description = "Get products without being tied to any category"
    )
    @GetMapping("/previews")
    public PaginationStatsDto<ProductPreviewDto> getProducts(@RequestParam(defaultValue = "0") int pageNo,
                                                             @RequestParam(defaultValue = "15") int pageSize) {
        return productService.getProducts(pageNo, pageSize);
    }

    @Operation(
            summary = "Get product names and descriptions by category",
            description = "Returns product IDs, names and short descriptions from a specified category"
    )
    @GetMapping("/info/{categoryId}")
    public PaginationStatsDto<ProductNameDescriptionProjection> getProductsInfo(@PathVariable UUID categoryId,
                                                                                @RequestParam(defaultValue = "0") int pageNo,
                                                                                @RequestParam(defaultValue = "15") int pageSize) {
        return productService.getProductsNameDescription(categoryId, pageNo, pageSize);
    }

    @Operation(
            summary = "Get product previews by category",
            description = "Returns a list of product preview cards for a given category"
    )
    @GetMapping("/previews/{categoryId}")
    public PaginationStatsDto<ProductPreviewProjection> getProductsPreviews(@PathVariable UUID categoryId,
                                                                            @RequestParam(defaultValue = "0") int pageNo,
                                                                            @RequestParam(defaultValue = "15") int pageSize) {
        return productService.getProductsPreviews(categoryId, pageNo, pageSize);
    }

    @Operation(
            summary = "Create new product",
            description = "Allows a retailer to create a product. Admin-created products are marked as trusted",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            encoding = {
                                    @Encoding(name = "product", contentType = MediaType.APPLICATION_JSON_VALUE),
                                    @Encoding(name = "image", contentType = "image/png, image/jpeg")
                            }
                    )
            )
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RETAILER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductInfoDto createProduct(@RequestPart(name = "image", required = false)
                                        @AllowedFileTypes(maxFileSize = 4096,
                                                allowedFileTypes = {
                                                        ContentType.PNG,
                                                        ContentType.JPEG
                                                }) @ValidImage MultipartFile image,
                                        @RequestPart("product") @Valid ProductCreateDto product,
                                        Authentication authentication) throws IOException {
        Boolean isCreatorTrusted = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(Role.ADMIN.getPrefixedRole()));

        return productService.createProduct(product, image, isCreatorTrusted);
    }

    @Operation(
            summary = "Verify products",
            description = "Allows admin to verify a list of products by their IDs"
    )
    @PatchMapping("/verification")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfoDto verifyProduct(@RequestBody List<UUID> productIds) {
        int verifiedCount = productService.verifyProducts(productIds);

        return ResponseInfoDto.builder()
                .message(String.format(PRODUCTS_VERIFICATION_MESSAGE, verifiedCount))
                .build();
    }

    @Operation(
            summary = "Update product",
            description = "Allows admin to update product name, description and characteristics",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            encoding = {
                                    @Encoding(name = "product", contentType = MediaType.APPLICATION_JSON_VALUE),
                                    @Encoding(name = "image", contentType = "image/png, image/jpeg")
                            }
                    )
            )
    )
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductInfoDto updateProduct(@RequestPart(value = "image", required = false)
                                        @AllowedFileTypes(maxFileSize = 4096,
                                                allowedFileTypes = {
                                                        ContentType.PNG,
                                                        ContentType.JPEG
                                                })
                                        @ValidImage MultipartFile image,
                                        @RequestPart(required = false) ProductUpdateDto product,
                                        @PathVariable UUID id) throws IOException {
        return productService.updateProduct(product, image, id);
    }

    @Operation(
            summary = "Delete product",
            description = "Allows admin to delete a product by ID"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfoDto deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, PRODUCT, id))
                .build();
    }
}
