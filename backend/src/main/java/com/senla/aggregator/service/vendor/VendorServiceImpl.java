package com.senla.aggregator.service.vendor;

import com.senla.aggregator.dto.vendor.VendorCreateDto;
import com.senla.aggregator.dto.vendor.VendorGetDto;
import com.senla.aggregator.mapper.VendorMapper;
import com.senla.aggregator.model.Vendor;
import com.senla.aggregator.model.Vendor_;
import com.senla.aggregator.repository.VendorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.service.exception.ExceptionMessages.VENDOR_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    private final VendorMapper vendorMapper;

    @Override
    public VendorGetDto getVendor(UUID id) {
        return vendorRepository.findById(id)
                .map(vendorMapper::toVendorGetDto)
                .orElseThrow(() -> new EntityNotFoundException(VENDOR_NOT_FOUND));
    }

    @Override
    @Transactional
    public VendorGetDto createVendor(VendorCreateDto dto) {
        Vendor vendor = vendorMapper.toVendor(dto);
        vendorRepository.save(vendor);

        return vendorMapper.toVendorGetDto(vendor);
    }

    @Override
    public List<VendorGetDto> getAllVendors(int pageNo, int pageSize) {
        return vendorRepository.findAll(PageRequest.of(pageNo, pageSize,
                        Sort.by(Vendor_.NAME)))
                .map(vendorMapper::toVendorGetDto)
                .toList();
    }

    @Override
    @Transactional
    public VendorGetDto updateVendor(VendorCreateDto dto, UUID id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(VENDOR_NOT_FOUND));

        vendorMapper.updateVendor(vendor, dto);

        return vendorMapper.toVendorGetDto(vendor);
    }

    @Override
    public void deleteVendor(UUID id) {
        vendorRepository.deleteById(id);
    }
}
