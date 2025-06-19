package com.senla.aggregator.service.store;

import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.dto.store.StoreCreateDto;
import com.senla.aggregator.dto.store.StoreGetDto;
import com.senla.aggregator.mapper.StoreMapper;
import com.senla.aggregator.model.Retailer;
import com.senla.aggregator.model.Store;
import com.senla.aggregator.model.Store_;
import com.senla.aggregator.repository.RetailerRepository;
import com.senla.aggregator.repository.StoreRepository;
import com.senla.aggregator.util.PaginationUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.service.exception.ExceptionMessages.RETAILER_NOT_FOUND;
import static com.senla.aggregator.service.exception.ExceptionMessages.STORE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    private final RetailerRepository retailerRepository;

    private final StoreMapper storeMapper;

    @Override
    @Transactional
    public StoreGetDto createStore(StoreCreateDto dto, UUID ownerId) {
        Store store = storeMapper.toStore(dto);

        Retailer retailer = retailerRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(RETAILER_NOT_FOUND));

        store.setRetailer(retailer);
        storeRepository.save(store);

        return storeMapper.toStoreGetDto(store);
    }

    @Override
    public List<StoreGetDto> getAllStores(int pageNo, int pageSize) {
        return storeRepository.findAllWithRetailerBy(PageRequest.of(pageNo, pageSize,
                        Sort.by("retailer.name", Store_.ADDRESS)))
                .stream()
                .map(storeMapper::toStoreGetDto)
                .toList();
    }

    @Override
    public PaginationStatsDto<StoreGetDto> getMyStores(UUID ownerId, int pageNo, int pageSize) {
        Page<StoreGetDto> content = storeRepository.findAllByRetailerOwnerId(ownerId, PageRequest.of(pageNo, pageSize,
                        Sort.by(Store_.ADDRESS)))
                .map(storeMapper::toStoreGetDto);
        return PaginationUtil.convertToPaginationStatsDto(content);
    }

    @Override
    public int countRetailerStore(UUID retailerOwnerId) {
        return storeRepository.countByRetailerOwnerId(retailerOwnerId);
    }

    @Override
    public PaginationStatsDto<StoreGetDto> getStoresOfRetailer(UUID retailerId, int pageNo, int pageSize) {
        Page<StoreGetDto> content = storeRepository.findByRetailerId(retailerId, PageRequest.of(pageNo, pageSize,
                        Sort.by(Store_.ADDRESS)))
                .map(storeMapper::toStoreGetDto);

        return PaginationUtil.convertToPaginationStatsDto(content);
    }

    @Override
    @Transactional
    public StoreGetDto updateStore(StoreCreateDto dto, UUID storeId, UUID ownerId) {
        Store store = storeRepository.findByIdAndRetailerOwnerId(storeId, ownerId)
                .orElseThrow(() -> new EntityNotFoundException(STORE_NOT_FOUND));

        storeMapper.updateStore(store, dto);

        return storeMapper.toStoreGetDto(store);
    }

    @Override
    public void deleteStore(UUID id, UUID ownerId) {
        storeRepository.findAllByIdAndRetailerOwnerId(id, ownerId)
                .orElseThrow(() -> new EntityNotFoundException(STORE_NOT_FOUND));

        storeRepository.deleteById(id);
    }
}
