package com.senla.aggregator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCardProperties {

    @JsonProperty("discount")
    @Column(name = "discount")
    private String discount;

    @JsonProperty("installment_available")
    @Column(name = "installment_available")
    private boolean installmentAvailable;

    @JsonProperty("warranty")
    @Column(name = "warranty")
    private String warranty;

    @JsonProperty("color_options")
    @Column(name = "color_options")
    private List<String> colorOptions;

    @JsonProperty("ram_options")
    @Column(name = "ram_options")
    private List<String> ramOptions;

    @JsonProperty("size_options")
    @Column(name = "size_options")
    private List<String> sizeOptions;

    @JsonProperty("strap_options")
    @Column(name = "strap_options")
    private List<String> strapOptions;
}
