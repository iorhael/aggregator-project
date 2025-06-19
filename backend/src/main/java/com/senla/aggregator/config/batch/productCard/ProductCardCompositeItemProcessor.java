package com.senla.aggregator.config.batch.productCard;

import com.senla.aggregator.config.batch.helper.ValidatingItemProcessorWithGroups;
import com.senla.aggregator.dto.productCard.ProductCardImportDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;

import java.util.ArrayList;
import java.util.List;

public class ProductCardCompositeItemProcessor implements ItemProcessor<ProductCardImportDto, ProductCardImportDto> {

    private final ItemProcessor<ProductCardImportDto, ProductCardImportDto> delegate;

    public ProductCardCompositeItemProcessor(List<ItemProcessor<ProductCardImportDto, ProductCardImportDto>> processors,
                                             Class<?>... validationGroups) {
        ValidatingItemProcessorWithGroups<ProductCardImportDto> validatingProcessor =
                new ValidatingItemProcessorWithGroups<>(validationGroups);
        validatingProcessor.setFilter(true);

        List<ItemProcessor<ProductCardImportDto, ProductCardImportDto>> delegates =
                new ArrayList<>(processors);
        delegates.addFirst(validatingProcessor);

        CompositeItemProcessor<ProductCardImportDto, ProductCardImportDto> compositeProcessor =
                new CompositeItemProcessor<>();
        compositeProcessor.setDelegates(delegates);

        this.delegate = compositeProcessor;
    }

    @Override
    public ProductCardImportDto process(@NotNull ProductCardImportDto item) throws Exception {
        return delegate.process(item);
    }
}
