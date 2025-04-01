package com.senla.aggregator.service.retailer;

import com.senla.aggregator.dto.retailer.RetailerCreateDto;
import com.senla.aggregator.dto.retailer.RetailerGetDto;
import com.senla.aggregator.dto.retailer.RetailerUpdateDto;
import com.senla.aggregator.mapper.RetailerMapper;
import com.senla.aggregator.model.Retailer;
import com.senla.aggregator.repository.retailer.RetailerRepository;
import com.senla.aggregator.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.service.exception.ExceptionMessages.RETAILER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RetailerServiceImpl implements RetailerService {

    private final RetailerRepository retailerRepository;

    private final UserRepository userRepository;

    private final RetailerMapper retailerMapper;

    @Override
    @Transactional
    public RetailerGetDto registerRetailer(RetailerCreateDto dto, UUID ownerId) {
        Retailer retailer = retailerMapper.toRetailer(dto);
        retailer.setOwner(userRepository.getReferenceById(ownerId));

        retailerRepository.save(retailer);

        return retailerMapper.toRetailerGetDto(retailer);
    }

    @Override
    public RetailerGetDto getRetailerByOwnerId(UUID id) {
        return retailerRepository.findWithOwnerByOwnerId(id)
                .map(retailerMapper::toRetailerGetDto)
                .orElseThrow(() -> new EntityNotFoundException(RETAILER_NOT_FOUND));
    }

    @Override
    public List<RetailerGetDto> getAllRetailers(int pageNo, int pageSize) {
        return retailerRepository.findWithOwnerBy(PageRequest.of(pageNo, pageSize))
                .stream()
                .map(retailerMapper::toRetailerGetDto)
                .toList();
    }

    @Override
    @Transactional
    public RetailerGetDto updateRetailer(RetailerUpdateDto dto, UUID ownerId) {
        Retailer retailer = retailerRepository.findWithOwnerByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(RETAILER_NOT_FOUND));

        retailerMapper.updateRetailer(retailer, dto);

        return retailerMapper.toRetailerGetDto(retailer);
    }

    @Override
    public void deleteRetailer(UUID ownerId) {
        retailerRepository.deleteByOwnerId(ownerId);
    }
}
