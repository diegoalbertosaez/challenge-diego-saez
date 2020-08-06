package com.mahva.diego.saez.mapper;

import com.mahva.diego.saez.dto.SpecialPriceDto;
import com.mahva.diego.saez.model.SpecialPrice;

public class SpecialPriceMapper {

	private SpecialPriceMapper() {

	}

	public static SpecialPriceDto toDto(SpecialPrice model) {

		SpecialPriceDto specialPriceDto = new SpecialPriceDto();

		if (model != null) {
			specialPriceDto.setDate(model.getDate());
			specialPriceDto.setPrice(model.getPrice());
			specialPriceDto.setId(model.getId());
		}

		return specialPriceDto;
	}

	public static SpecialPrice toModel(SpecialPriceDto dto) {

		SpecialPrice specialPrice = new SpecialPrice();

		if (dto != null) {
			specialPrice.setId(dto.getId());
			specialPrice.setDate(dto.getDate());
			specialPrice.setPrice(dto.getPrice());
		}

		return specialPrice;
	}

}
