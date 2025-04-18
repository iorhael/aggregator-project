package com.senla.aggregator.controller;

import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.dto.favorite.FavoriteCreateDto;
import com.senla.aggregator.dto.favorite.FavoriteGetDto;
import com.senla.aggregator.dto.favorite.FavoriteUpdateDto;
import com.senla.aggregator.model.UserTag;
import com.senla.aggregator.service.favorite.FavoriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
import static com.senla.aggregator.controller.ControllerMessages.FAVORITE;

@RestController
@RequestMapping("api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorites Resource", description = "CRUD and some filtration")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/{id}")
    public FavoriteGetDto findFavorite(@PathVariable UUID id) {
        return favoriteService.getFavorite(id);
    }

    @GetMapping("/me")
    public List<FavoriteGetDto> findMyFavorites(@RequestParam(defaultValue = "0") int pageNo,
                                                @RequestParam(defaultValue = "15") int pageSize,
                                                Principal principal) {
        UUID userId = UUID.fromString(principal.getName());

        return favoriteService.getMyFavorites(userId, pageNo, pageSize);
    }

    @GetMapping("/me/filter/{userTag}")
    public List<FavoriteGetDto> findMyFavorites(@RequestParam(defaultValue = "0") int pageNo,
                                                @RequestParam(defaultValue = "15") int pageSize,
                                                @PathVariable UserTag userTag,
                                                Principal principal) {
        UUID userId = UUID.fromString(principal.getName());

        return favoriteService.filterMyFavorites(userTag, userId, pageNo, pageSize);
    }

    @GetMapping("/tags")
    public List<UserTag> getAllUserTags() {
        return favoriteService.getAllUserTags();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FavoriteGetDto createFavorite(@Valid @RequestBody FavoriteCreateDto dto,
                                         Principal principal) {
        UUID authorId = UUID.fromString(principal.getName());

        return favoriteService.createFavorite(dto, authorId);
    }

    @PutMapping("/{id}")
    public FavoriteGetDto updateFavorite(@Valid @RequestBody FavoriteUpdateDto dto,
                                         @PathVariable UUID id,
                                         Principal principal) {
        UUID authorId = UUID.fromString(principal.getName());

        return favoriteService.updateFavorite(dto, id, authorId);
    }

    @DeleteMapping("/{id}")
    public ResponseInfoDto deleteFavorite(@PathVariable UUID id, Principal principal) {
        UUID userId = UUID.fromString(principal.getName());

        favoriteService.checkOwnershipAndDeleteFavorite(id, userId);

        return ResponseInfoDto.builder()
                .message(String.format(DELETION_MESSAGE, FAVORITE, id))
                .build();
    }
}
