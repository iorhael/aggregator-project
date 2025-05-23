package com.senla.aggregator.service.store;

import com.senla.aggregator.dto.store.StoreCreateDto;
import com.senla.aggregator.dto.store.StoreGetDto;
import com.senla.aggregator.dto.store.StoreUpdateDto;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    StoreGetDto createStore(StoreCreateDto store, UUID ownerId);

    List<StoreGetDto> getAllStores(int pageNo, int pageSize);

    List<StoreGetDto> getMyStores(UUID ownerId, int pageNo, int pageSize);

    List<StoreGetDto> getStoresOfRetailer(UUID retailerId, int pageNo, int pageSize);

    StoreGetDto updateStore(StoreUpdateDto store, UUID ownerId);

    void deleteStore(UUID id, UUID ownerId);
}
