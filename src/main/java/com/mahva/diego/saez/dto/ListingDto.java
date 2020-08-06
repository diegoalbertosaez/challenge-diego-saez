package com.mahva.diego.saez.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingDto {

	private String id;

	@JsonProperty("owner_id")
	private String ownerId;

	private String name;

	private String slug;

	private String description;

	private Integer adults;

	private Integer children;

	@JsonProperty("is_pets_allowed")
	private Boolean isPetsAllowed;

	@JsonProperty("base_price")
	private Double basePrice;

	@JsonProperty("cleaning_fee")
	private Double cleaningFee;

	@JsonProperty("image_url")
	private String imageUrl;

	@JsonProperty("weekly_discount")
	private Double weeklyDiscount;

	@JsonProperty("monthly_discount")
	private Double monthlyDiscount;

	private List<SpecialPriceDto> specialPrices;

}
