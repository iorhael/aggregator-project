package com.senla.aggregator.dto.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senla.aggregator.model.UserTag;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteUpdateDto {

    @JsonProperty("user_notes")
    private String notes;

    @JsonProperty("user_tag")
    private UserTag tag;
}
