package com.senla.aggregator.service.favorite;

import com.senla.aggregator.dto.favorite.FavoriteCreateDto;
import com.senla.aggregator.dto.favorite.FavoriteGetDto;
import com.senla.aggregator.dto.favorite.FavoriteUpdateDto;
import com.senla.aggregator.mapper.FavoriteMapper;
import com.senla.aggregator.model.Favorite;
import com.senla.aggregator.model.Favorite_;
import com.senla.aggregator.model.UserTag;
import com.senla.aggregator.repository.favorite.FavoriteRepository;
import com.senla.aggregator.repository.product.ProductRepository;
import com.senla.aggregator.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.service.exception.ExceptionMessages.FAVORITE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final FavoriteMapper favoriteMapper;

    @Override
    @Transactional
    public FavoriteGetDto getFavorite(UUID id) {
        return favoriteRepository.findById(id)
                .map(favoriteMapper::toFavoriteGetDto)
                .orElseThrow(() -> new EntityNotFoundException(FAVORITE_NOT_FOUND));
    }

    @Override
    @Transactional
    public FavoriteGetDto createFavorite(FavoriteCreateDto dto, UUID userId) {
        Favorite favorite = favoriteMapper.toFavorite(dto);

        favorite.setUser(userRepository.getReferenceById(userId));
        favorite.setProduct(productRepository.getReferenceById(dto.getProductId()));

        return favoriteMapper.toFavoriteGetDto(favoriteRepository.save(favorite));
    }

    @Override
    @Transactional
    public List<FavoriteGetDto> getMyFavorites(UUID userId, int pageNo, int pageSize) {
        return favoriteRepository.findAllByUserId(userId, PageRequest.of(pageNo, pageSize,
                        Sort.by(Sort.Direction.DESC, Favorite_.CREATED_AT)))
                .stream()
                .map(favoriteMapper::toFavoriteGetDto)
                .toList();
    }

    @Override
    @Transactional
    public List<FavoriteGetDto> filterMyFavorites(UserTag tag, UUID userId, int pageNo, int pageSize) {
        return favoriteRepository.findAllByUserIdAndTag(userId, tag, PageRequest.of(pageNo, pageSize,
                        Sort.by(Sort.Direction.DESC, Favorite_.CREATED_AT)))
                .stream()
                .map(favoriteMapper::toFavoriteGetDto)
                .toList();
    }

    @Override
    public List<UserTag> getAllUserTags() {
        return Arrays.stream(UserTag.values()).toList();
    }

    @Override
    @Transactional
    public FavoriteGetDto updateFavorite(FavoriteUpdateDto dto, UUID id, UUID userId) {
        Favorite favorite = favoriteRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(FAVORITE_NOT_FOUND));

        favoriteMapper.updateFavorite(favorite, dto);

        return favoriteMapper.toFavoriteGetDto(favorite);
    }

    @Override
    @Transactional
    public void checkOwnershipAndDeleteFavorite(UUID id, UUID userId) {
        favoriteRepository.deleteByIdAndUserId(id, userId);
    }
}
