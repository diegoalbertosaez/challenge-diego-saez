package com.mahva.diego.saez.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mahva.diego.saez.model.SpecialPrice;

@Repository
public interface SpecialPriceRepository extends JpaRepository<SpecialPrice, String> {

	boolean existsByListingIdAndDate(String listingId, LocalDate date);
	void deleteByListingId(String listingId);
}
