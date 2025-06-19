package com.senla.aggregator.service.retailer;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.retailer.RetailerCreateDto;
import com.senla.aggregator.dto.retailer.RetailerGetDto;
import com.senla.aggregator.dto.retailer.RetailerUpdateDto;
import com.senla.aggregator.dto.retailer.RetailerWithAutoUpdateCardDto;
import com.senla.aggregator.mapper.RetailerMapper;
import com.senla.aggregator.model.AutoUpdateCard;
import com.senla.aggregator.model.Retailer;
import com.senla.aggregator.repository.AutoUpdateCardRepository;
import com.senla.aggregator.repository.RetailerRepository;
import com.senla.aggregator.repository.UserRepository;
import com.senla.aggregator.service.minio.MinioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.senla.aggregator.service.exception.ExceptionMessages.RETAILER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RetailerServiceImpl implements RetailerService {
    private static final String RETAILER_LOGO_DIRECTORY = "retailers";

    private final MinioService minioService;
    private final UserRepository userRepository;
    private final RetailerMapper retailerMapper;
    private final RetailerRepository retailerRepository;
    private final AutoUpdateCardRepository autoUpdateCardRepository;

    @Override
    @Transactional
    public RetailerGetDto registerRetailer(RetailerCreateDto dto,
                                           MultipartFile logo,
                                           UUID ownerId) throws IOException {
        Retailer retailer = retailerMapper.toRetailer(dto);
        retailer.setOwner(userRepository.getReferenceById(ownerId));

        if (Objects.nonNull(logo)) {
            try (InputStream stream = logo.getInputStream()) {
                String imageLink = minioService.saveImage(
                        stream,
                        RETAILER_LOGO_DIRECTORY,
                        ContentType.fromValue(logo.getContentType())
                );
                retailer.setLogoLink(imageLink);
            }
        }

        retailerRepository.save(retailer);

        return retailerMapper.toRetailerGetDto(retailer);
    }

    @Override
    public RetailerGetDto getRetailerByName(String name) {
        return retailerRepository.findByName(name)
                .map(retailerMapper::toRetailerGetDto)
                .orElseThrow(() -> new EntityNotFoundException(RETAILER_NOT_FOUND));
    }

    @Override
    @Transactional
    public RetailerWithAutoUpdateCardDto getRetailerWithAutoUpdateInfo(UUID ownerId) {
        Retailer retailer = retailerRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(RETAILER_NOT_FOUND));
        AutoUpdateCard autoUpdateInfo = autoUpdateCardRepository.findById(retailer.getId())
                .orElse(new AutoUpdateCard());

        return retailerMapper.from(retailer, autoUpdateInfo);
    }

    @Override
    public List<RetailerGetDto> getAllRetailers(int pageNo, int pageSize) {
        return retailerRepository.findAllBy(PageRequest.of(pageNo, pageSize))
                .stream()
                .map(retailerMapper::toRetailerGetDto)
                .toList();
    }

    @Override
    @Transactional
    public RetailerGetDto updateRetailer(RetailerUpdateDto dto,
                                         MultipartFile newLogo,
                                         UUID ownerId) throws IOException {
        Retailer retailer = retailerRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(RETAILER_NOT_FOUND));

        if (Objects.nonNull(newLogo)) {
            try (InputStream stream = newLogo.getInputStream()) {
                String newImageLink = minioService.updateImage(
                        retailer.getLogoLink(),
                        RETAILER_LOGO_DIRECTORY,
                        stream,
                        ContentType.fromValue(newLogo.getContentType())
                );
                retailer.setLogoLink(newImageLink);
            }
        }

        retailerMapper.updateRetailer(retailer, dto);

        return retailerMapper.toRetailerGetDto(retailer);
    }

    @Override
    @Transactional
    public void deleteMyRetailer(UUID ownerId) {
        Retailer retailer = retailerRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(RETAILER_NOT_FOUND));

        if (Objects.nonNull(retailer.getLogoLink()))
            minioService.deleteImage(retailer.getLogoLink());

        retailerRepository.deleteById(retailer.getId());
    }

    @Override
    public void deleteRetailer(UUID retailerId) {
        Retailer retailer = retailerRepository.findById(retailerId)
                .orElseThrow(() -> new EntityNotFoundException(RETAILER_NOT_FOUND));

        if (Objects.nonNull(retailer.getLogoLink()))
            minioService.deleteImage(retailer.getLogoLink());

        retailerRepository.deleteById(retailerId);
    }
}
