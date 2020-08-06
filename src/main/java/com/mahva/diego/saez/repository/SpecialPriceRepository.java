package com.mahva.diego.saez.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mahva.diego.saez.model.SpecialPrice;

/**
 * Repository for SpecialPrice entity
 * 
 * @author diegosaez
 *
 */
@Repository
public interface SpecialPriceRepository extends JpaRepository<SpecialPrice, String> {

	/**
	 * Check if a special price exists for an listingId and a date
	 * 
	 * @param listingId
	 * @param date
	 * @return true if exists
	 */
	boolean existsByListingIdAndDate(String listingId, LocalDate date);

	/**
	 * Delete a special price by listingId
	 * 
	 * @param listingId
	 */
	void deleteByListingId(String listingId);
}
