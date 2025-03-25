package com.senla.aggregator.service.productCard;

import com.senla.aggregator.dto.productCard.ProductCardCreateDto;
import com.senla.aggregator.dto.productCard.ProductCardGetDto;
import com.senla.aggregator.dto.productCard.ProductCardUpdateDto;
import com.senla.aggregator.mapper.ProductCardMapper;
import com.senla.aggregator.model.PriceHistory;
import com.senla.aggregator.model.ProductCard;
import com.senla.aggregator.model.Retailer;
import com.senla.aggregator.repository.product.ProductRepository;
import com.senla.aggregator.repository.productCard.ProductCardRepository;
import com.senla.aggregator.specification.ProductCardSpecification;
import com.senla.aggregator.repository.retailer.RetailerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public ProductCardGetDto createProductCard(ProductCardCreateDto dto, UUID ownerId) {
        ProductCard productCard = productCardMapper.toProductCard(dto);

        productCard.setProduct(productRepository.getReferenceById(dto.getProductId()));

        Retailer retailer = retailerRepository.findRetailerByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(RETAILER_NOT_FOUND));
        productCard.setRetailer(retailer);

        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(dto.getPrice());
        priceHistory.setCard(productCard);
        productCard.getPriceHistories().add(priceHistory);

        productCardRepository.save(productCard);

        return productCardMapper.toProductCardGetDto(productCard);
    }

    @Override
    public List<ProductCardGetDto> getMyProductCards(UUID ownerId, int pageNo, int pageSize) {
        return productCardRepository.findWithRetailerAndProductByRetailerOwnerId(ownerId,
                        PageRequest.of(pageNo, pageSize, Sort.by("product.name")))
                .stream()
                .map(productCardMapper::toProductCardGetDto)
                .toList();
    }

    @Override
    public List<ProductCardGetDto> getAllProductCardsOfRetailer(String retailerName, int pageNo, int pageSize) {
        return productCardRepository.findWithRetailerAndProductByRetailerName(retailerName,
                        PageRequest.of(pageNo, pageSize, Sort.by("product.name")))
                .stream()
                .map(productCardMapper::toProductCardGetDto)
                .toList();
    }

    @Override
    public List<ProductCardGetDto> getProductCardsBySpecification(
            String productName,
            String retailerName,
            String discount,
            Boolean installmentAvailable,
            String color,
            String ram,
            String size,
            String strap,
            String warranty,
            int pageNo,
            int pageSize) {

        Specification<ProductCard> specification = ProductCardSpecification.buildSpecification(
               productName,
               retailerName,
               discount,
               installmentAvailable,
               color,
               ram,
               size,
               strap,
               warranty
        );
       return productCardRepository.findAll(specification, PageRequest.of(pageNo, pageSize))
               .stream()
               .map(productCardMapper::toProductCardGetDto)
               .toList();
    }

    @Override
    @Transactional
    public ProductCardGetDto updateProductCard(ProductCardUpdateDto dto, UUID id, UUID ownerId) {
        ProductCard productCard = productCardRepository.findByIdAndRetailerOwnerId(id, ownerId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_CARD_NOT_FOUND));

        productCardMapper.updateProductCard(productCard, dto);

        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(dto.getPrice());
        priceHistory.setCard(productCard);

        productCard.getPriceHistories().add(priceHistory);

        return productCardMapper.toProductCardGetDto(productCard);
    }

    @Override
    public void deleteProductCard(UUID id, UUID ownerId) {
        productCardRepository.findByIdAndRetailerOwnerId(id, ownerId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_CARD_NOT_FOUND));

        productCardRepository.deleteById(id);
    }
}
