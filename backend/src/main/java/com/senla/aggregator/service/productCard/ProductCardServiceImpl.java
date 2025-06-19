package com.senla.aggregator.service.productCard;

import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.dto.productCard.ProductCardCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardDetailedDto;
import com.senla.aggregator.dto.productCard.ProductCardFilterDto;
import com.senla.aggregator.dto.productCard.ProductCardPreviewDto;
import com.senla.aggregator.dto.productCard.ProductCardUpdateDto;
import com.senla.aggregator.mapper.ProductCardMapper;
import com.senla.aggregator.model.PriceHistory;
import com.senla.aggregator.model.ProductCard;
import com.senla.aggregator.model.ProductCard_;
import com.senla.aggregator.model.Retailer;
import com.senla.aggregator.repository.ProductRepository;
import com.senla.aggregator.repository.RetailerRepository;
import com.senla.aggregator.repository.productCard.ProductCardRepository;
import com.senla.aggregator.specification.ProductCardSpecification;
import com.senla.aggregator.util.PaginationUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.senla.aggregator.service.exception.ExceptionMessages.PRODUCT_CARD_NOT_FOUND;
import static com.senla.aggregator.service.exception.ExceptionMessages.RETAILER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductCardServiceImpl implements ProductCardService {

    private final ProductCardRepository productCardRepository;

    private final RetailerRepository retailerRepository;

    private final ProductRepository productRepository;

    private final ProductCardMapper productCardMapper;

    @Override
    @Transactional
    public ProductCardDetailedDto getProductCard(UUID id) {
        return productCardRepository.findDetailedById(id)
                .map(productCardMapper::toProductCardDetailedDto)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_CARD_NOT_FOUND));
    }

    @Override
    @Transactional
    public PaginationStatsDto<ProductCardDetailedDto> getRetailerProductCards(UUID retailerOwnerId, Pageable pageable) {
        Page<ProductCardDetailedDto> content = productCardRepository.findAllByRetailerOwnerId(
                        retailerOwnerId,
                        pageable
                )
                .map(productCardMapper::toProductCardDetailedDto);

        return PaginationUtil.convertToPaginationStatsDto(content);
    }

    @Override
    public long countRetailerProductCards(UUID retailerOwnerId) {
        return productCardRepository.countByRetailerOwnerId(retailerOwnerId);
    }

    @Override
    @Transactional
    public ProductCardPreviewDto createProductCard(ProductCardCreateDto dto, UUID retailerOwnerId) {
        ProductCard productCard = productCardMapper.toProductCard(dto);

        productCard.setProduct(productRepository.getReferenceById(dto.getProductId()));

        Retailer retailer = retailerRepository.findByOwnerId(retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(RETAILER_NOT_FOUND));
        productCard.setRetailer(retailer);

        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(dto.getPrice());
        priceHistory.setCard(productCard);
        productCard.setPriceHistories(new ArrayList<>(List.of(priceHistory)));

        productCardRepository.save(productCard);

        ProductCardPreviewDto result = new ProductCardPreviewDto();
        result.setId(productCard.getId());
        result.setRetailerName(retailer.getName());
        result.setPrice(dto.getPrice());
        result.setMaxDeliveryTime(dto.getMaxDeliveryTime());

        return result;
    }

    @Override
    @Transactional
    public PaginationStatsDto<ProductCardPreviewDto> filterProductCards(ProductCardFilterDto dto,
                                                                        UUID productId,
                                                                        int pageNo,
                                                                        int pageSize) {
        Specification<ProductCard> specification = ProductCardSpecification.buildSpecification(
                productId,
                dto.getRetailerName(),
                dto.getWarranty(),
                dto.getInstallmentPeriod(),
                dto.getMaxDeliveryTime()
        );

        Page<ProductCardPreviewDto> content = productCardRepository.findAll(specification, PageRequest.of(pageNo, pageSize,
                        Sort.by("latestPrice.price", ProductCard_.MAX_DELIVERY_TIME)))
                .map(productCardMapper::toProductCardPreviewDto);

        return PaginationUtil.convertToPaginationStatsDto(content);
    }

    @Override
    @Transactional
    public ProductCardDetailedDto updateProductCard(ProductCardUpdateDto dto,
                                                    UUID productCardId,
                                                    UUID retailerOwnerId) {
        ProductCard productCard = productCardRepository.findDetailedByIdAndRetailerOwnerId(productCardId, retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_CARD_NOT_FOUND));

        productCardMapper.updateProductCard(productCard, dto);

        if (Objects.nonNull(dto.getPrice())) {
            PriceHistory priceHistory = new PriceHistory();
            priceHistory.setPrice(dto.getPrice());
            priceHistory.setCard(productCard);
            productCard.getPriceHistories().add(priceHistory);

            productCard.setLatestPrice(priceHistory);
        }

        return productCardMapper.toProductCardDetailedDto(productCard);
    }

    @Override
    @Transactional
    public void deleteProductCard(UUID productCardId, UUID retailerOwnerId) {
        productCardRepository.findByIdAndRetailerOwnerId(productCardId, retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_CARD_NOT_FOUND));

        productCardRepository.deleteById(productCardId);
    }

    @Override
    @Transactional
    public int deleteAllProductCards(UUID retailerOwnerId) {
        return productCardRepository.deleteAllByRetailerOwnerId(retailerOwnerId);
    }
}
