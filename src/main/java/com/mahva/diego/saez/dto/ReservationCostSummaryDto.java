package com.mahva.diego.saez.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationCostSummaryDto {

	@JsonProperty("nights_count")
	private Integer nightsCount;

	@JsonProperty("nights_cost")
	private Double nightsCost;

	private Double discount;

	@JsonProperty("cleaning_fee")
	private Double cleaningFee;

	private Double total;
}
