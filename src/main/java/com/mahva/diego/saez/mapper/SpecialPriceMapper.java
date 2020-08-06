package com.mahva.diego.saez.mapper;

import com.mahva.diego.saez.dto.SpecialPriceDto;
import com.mahva.diego.saez.model.SpecialPrice;

/**
 * Mapper for special price
 * 
 * @author diegosaez
 *
 */
public class SpecialPriceMapper {

	private SpecialPriceMapper() {

	}

	/**
	 * Convert model entity to dto
	 * 
	 * @param model
	 * @return {@link SpecialPriceDto}
	 */
	public static SpecialPriceDto toDto(SpecialPrice model) {

		SpecialPriceDto specialPriceDto = new SpecialPriceDto();

		if (model != null) {
			specialPriceDto.setDate(model.getDate());
			specialPriceDto.setPrice(model.getPrice());
			specialPriceDto.setId(model.getId());
		}

		return specialPriceDto;
	}

	/**
	 * Convert a dto to entity model
	 * 
	 * @param dto
	 * @return {@link SpecialPrice}
	 */
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
