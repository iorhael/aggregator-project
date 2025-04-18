package com.senla.aggregator.service.favorite;

import com.senla.aggregator.dto.favorite.FavoriteCreateDto;
import com.senla.aggregator.dto.favorite.FavoriteGetDto;
import com.senla.aggregator.dto.favorite.FavoriteUpdateDto;
import com.senla.aggregator.model.UserTag;

import java.util.List;
import java.util.UUID;

public interface FavoriteService {

    FavoriteGetDto getFavorite(UUID id);

    FavoriteGetDto createFavorite(FavoriteCreateDto dto, UUID userId);

    List<FavoriteGetDto> getMyFavorites(UUID userId, int pageNo, int pageSize);

    List<FavoriteGetDto> filterMyFavorites(UserTag tag, UUID userId, int pageNo, int pageSize);

    List<UserTag> getAllUserTags();

    FavoriteGetDto updateFavorite(FavoriteUpdateDto dto, UUID id, UUID userId);

    void checkOwnershipAndDeleteFavorite(UUID id, UUID userId);
}
