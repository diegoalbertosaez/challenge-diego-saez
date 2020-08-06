package com.mahva.diego.saez.mapper;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.mahva.diego.saez.dto.ListingDto;
import com.mahva.diego.saez.dto.SpecialPriceDto;
import com.mahva.diego.saez.model.Listing;

/**
 * Mapper for listing entity
 * 
 * @author diegosaez
 *
 */
public class ListingMapper {

	private ListingMapper() {

	}

	/**
	 * Convert model entity to dto
	 * 
	 * @param model
	 * @return {@link ListingDto}
	 */
	public static ListingDto toDto(Listing model) {

		final ListingDto listingDTO = new ListingDto();

		if (model != null) {

			BeanUtils.copyProperties(model, listingDTO, "specialPrices");
			listingDTO.setSpecialPrices(new ArrayList<>());

			if (!CollectionUtils.isEmpty(model.getSpecialPrices())) {

				model.getSpecialPrices().forEach(specialPrice -> {
					SpecialPriceDto specialPriceDTO = new SpecialPriceDto();
					BeanUtils.copyProperties(specialPrice, specialPriceDTO);
					listingDTO.getSpecialPrices().add(specialPriceDTO);
				});

			}

		}

		return listingDTO;
	}

	/**
	 * Convert a dto to entity model
	 * 
	 * @param dto
	 * @return {@link Listing}
	 */
	public static Listing toModel(ListingDto dto) {

		Listing listing = null;

		if (dto != null) {
			listing = new Listing();
			BeanUtils.copyProperties(dto, listing, "specialPrices");
		}

		return listing;
	}

}
