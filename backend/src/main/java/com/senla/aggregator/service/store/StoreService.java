package com.senla.aggregator.service.store;

import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.dto.store.StoreCreateDto;
import com.senla.aggregator.dto.store.StoreGetDto;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    StoreGetDto createStore(StoreCreateDto store, UUID ownerId);

    List<StoreGetDto> getAllStores(int pageNo, int pageSize);

    PaginationStatsDto<StoreGetDto> getMyStores(UUID retailerOwnerId, int pageNo, int pageSize);

    int countRetailerStore(UUID retailerOwnerId);

    PaginationStatsDto<StoreGetDto> getStoresOfRetailer(UUID retailerId, int pageNo, int pageSize);

    StoreGetDto updateStore(StoreCreateDto store, UUID storeId, UUID ownerId);

    void deleteStore(UUID id, UUID ownerId);
}
