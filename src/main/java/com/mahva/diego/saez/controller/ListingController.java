package com.mahva.diego.saez.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mahva.diego.saez.dto.CheckoutDto;
import com.mahva.diego.saez.dto.ListingDeletionDto;
import com.mahva.diego.saez.dto.ListingDto;
import com.mahva.diego.saez.dto.ReservationCostSummaryDto;
import com.mahva.diego.saez.dto.SpecialPriceDeletionDto;
import com.mahva.diego.saez.dto.SpecialPriceDto;
import com.mahva.diego.saez.service.ListingService;

/**
 * Rest controller for manage listing.
 * 
 * 
 * @author diegosaez
 *
 */
@RestController
@RequestMapping("/api")
public class ListingController {

	private ListingService listingService;

	/**
	 * Constructor with injected dependencies.
	 * 
	 * @param listingService
	 * @author diegosaez
	 */
	@Autowired
	public ListingController(ListingService listingService) {
		this.listingService = listingService;
	}

	/**
	 * Get all listings.
	 * 
	 * @author diegosaez
	 * @author diegosaez
	 * @return List<ListingDto>
	 */
	@GetMapping("/listings")
	public List<ListingDto> findAll() {
		return this.listingService.findAll();
	}

	/**
	 * Create a new listing.
	 * 
	 * @author diegosaez
	 * @param listingDto
	 * @return {@link ListingDto}
	 */
	@PostMapping("/listings")
	public ListingDto create(@RequestBody ListingDto listingDto) {
		return this.listingService.create(listingDto);
	}

	/**
	 * Find a listing by its id.
	 * 
	 * @author diegosaez
	 * @param id
	 * @return {@link ListingDto}
	 */
	@GetMapping("/listings/{id}")
	public ListingDto findById(@PathVariable("id") String id) {
		return this.listingService.findById(id);
	}

	/**
	 * Delete a listing by id
	 * 
	 * @author diegosaez
	 * @param id
	 * @return {@link ListingDeletionDto}
	 */
	@DeleteMapping("/listings/{id}")
	public ListingDeletionDto delete(@PathVariable("id") String id) {
		return this.listingService.delete(id);
	}

	/**
	 * Update a listing
	 * 
	 * @author diegosaez
	 * @param listingDto
	 * @return {@link ListingDto}
	 */
	@PutMapping("/listings")
	public ListingDto update(@RequestBody ListingDto listingDto) {
		return this.listingService.update(listingDto);
	}

	/**
	 * Add special price to listing
	 * 
	 * @author diegosaez
	 * @param specialPriceDto
	 * @param listingId
	 * @return {@link SpecialPriceDto}
	 */
	@PostMapping("/listings/{id}/special-prices")
	public SpecialPriceDto addSpecialPrice(@RequestBody SpecialPriceDto specialPriceDto,
			@PathVariable("id") String listingId) {
		return listingService.addSpecialPrice(specialPriceDto, listingId);
	}

	/**
	 * Delete a special price from listing
	 * 
	 * @author diegosaez
	 * @param listingId
	 * @param specialPriceId
	 * @return {@link SpecialPriceDeletionDto}
	 */
	@DeleteMapping("/listings/{listingId}/special-prices/{specialPriceId}")
	public SpecialPriceDeletionDto deleteSpecialPrice(@PathVariable("listingId") String listingId,
			@PathVariable("specialPriceId") String specialPriceId) {
		return listingService.deleteSpecialPrice(listingId, specialPriceId);
	}

	/**
	 * Calculate the reservation cost for the dates
	 * 
	 * @author diegosaez
	 * @param checkoutDto
	 * @param listingId
	 * @return {@link ReservationCostSummaryDto}
	 */
	@GetMapping("/listings/{id}/checkout")
	public ReservationCostSummaryDto calculateReservationCost(@RequestBody CheckoutDto checkoutDto,
			@PathVariable("id") String listingId) {
		return listingService.calculateCostReservation(checkoutDto, listingId);
	}
}
