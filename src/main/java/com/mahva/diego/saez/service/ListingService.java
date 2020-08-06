package com.mahva.diego.saez.service;

import java.util.List;

import com.mahva.diego.saez.dto.CheckoutDto;
import com.mahva.diego.saez.dto.ListingDeletionDto;
import com.mahva.diego.saez.dto.ListingDto;
import com.mahva.diego.saez.dto.ReservationCostSummaryDto;
import com.mahva.diego.saez.dto.SpecialPriceDeletionDto;
import com.mahva.diego.saez.dto.SpecialPriceDto;

public interface ListingService {

	/**
	 * Get all listings.
	 * 
	 * @author diegosaez
	 */
	List<ListingDto> findAll();

	/**
	 * Delete a listing by id
	 * 
	 * @param id id of listing to delete
	 * @author diegosaez
	 */
	ListingDeletionDto delete(String id);

	/**
	 * Find a listing by its id
	 * 
	 * @author diegosaez
	 */
	ListingDto findById(String id);

	/**
	 * Create a new listing
	 * 
	 * @param listingDto listing to create
	 * @author diegosaez
	 */
	ListingDto create(ListingDto listingDto);

	/**
	 * Update a listing
	 * 
	 * 
	 * @param listingDto to update
	 * @return {@link ListingDto} updated
	 */
	ListingDto update(ListingDto listingDto);

	/**
	 * Add special price to listing
	 * 
	 * @param specialPriceDto special price to add
	 * @param listingId       listing id to associate special price
	 * @return {@link SpecialPriceDto}
	 */
	SpecialPriceDto addSpecialPrice(SpecialPriceDto specialPriceDto, String listingId);

	/**
	 * Delete a special price from listing
	 * 
	 * @param listingId      listing containing the special price
	 * @param specialPriceId special price to delete
	 * @return {@link SpecialPriceDeletionDto}
	 */
	SpecialPriceDeletionDto deleteSpecialPrice(String listingId, String specialPriceId);

	/**
	 * Calculate the reservation cost for the dates
	 * 
	 * @param checkoutDto - Checkin and checkout date to calculate the reservation
	 * @return {@link ReservationCostSummaryDto}
	 */
	ReservationCostSummaryDto calculateCostReservation(CheckoutDto checkoutDto, String listingId);
}
