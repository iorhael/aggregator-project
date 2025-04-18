package com.senla.aggregator.controller;

import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.store.StoreCreateDto;
import com.senla.aggregator.dto.store.StoreGetDto;
import com.senla.aggregator.dto.store.StoreUpdateDto;
import com.senla.aggregator.service.store.StoreService;
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

import static com.senla.aggregator.controller.ControllerMessages.DELETION_MESSAGE;
import static com.senla.aggregator.controller.ControllerMessages.STORE;

@RestController
@RequestMapping("api/stores")
@RequiredArgsConstructor
@Tag(name = "Stores Resource", description = "Search and manage stores")
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public List<StoreGetDto> findAllStores(@RequestParam(defaultValue = "0") int pageNo,
                                           @RequestParam(defaultValue = "15") int pageSize) {
        return storeService.getAllStores(pageNo, pageSize);
    }

    @GetMapping("/{retailerId}")
    public List<StoreGetDto> findAllStoresByRetailerId(@RequestParam(defaultValue = "0") int pageNo,
                                                         @RequestParam(defaultValue = "15") int pageSize,
                                                         @PathVariable UUID retailerId) {
        return storeService.getStoresOfRetailer(retailerId, pageNo, pageSize);
    }

    @PostMapping
    @PreAuthorize("hasRole('RETAILER')")
    @ResponseStatus(HttpStatus.CREATED)
    public StoreGetDto createStore(@Valid @RequestBody StoreCreateDto store,
                                   Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        return storeService.createStore(store, ownerId);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('RETAILER')")
    public List<StoreGetDto> findMyStores(@RequestParam(defaultValue = "0") int pageNo,
                                          @RequestParam(defaultValue = "15") int pageSize,
                                          Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        return storeService.getMyStores(ownerId, pageNo, pageSize);
    }

    @PutMapping
    @PreAuthorize("hasRole('RETAILER')")
    public StoreGetDto updateStore(@Valid @RequestBody StoreUpdateDto store,
                                   Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        return storeService.updateStore(store, ownerId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RETAILER')")
    public ResponseInfoDto deleteRetailer(@PathVariable UUID id, Principal principal) {
        UUID ownerId = UUID.fromString(principal.getName());

        storeService.deleteStore(id, ownerId);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, STORE, ownerId))
                .build();
    }
}
