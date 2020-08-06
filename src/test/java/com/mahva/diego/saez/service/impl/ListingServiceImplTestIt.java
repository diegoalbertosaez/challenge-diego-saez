package com.mahva.diego.saez.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.mahva.diego.saez.dto.CheckoutDto;
import com.mahva.diego.saez.dto.ListingDeletionDto;
import com.mahva.diego.saez.dto.ListingDto;
import com.mahva.diego.saez.dto.ReservationCostSummaryDto;
import com.mahva.diego.saez.dto.SpecialPriceDeletionDto;
import com.mahva.diego.saez.dto.SpecialPriceDto;
import com.mahva.diego.saez.service.ListingService;

/**
 * Integration test for ListingServiceImpl
 * 
 * @author diegosaez
 *
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional(readOnly = true)
class ListingServiceImplTestIt {

	@Autowired
	private ListingService listingService;

	@Test
	void testCreate() {
		ListingDto listingDto = ListingDto.builder().name("name").basePrice(100D)
				.ownerId("0e89855a-bb9f-4c53-a638-3306e4c6a400").build();
		ListingDto result = listingService.create(listingDto);
		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getId());
		Assertions.assertNotNull(result.getName());
		Assertions.assertNotNull(result.getBasePrice());
		Assertions.assertEquals(100D, result.getBasePrice());
		Assertions.assertEquals("name", result.getName());
	}

	@Test
	void testUpdate() {
		ListingDto listingDto = listingService.findById("3fce319d-333f-4603-82a0-1e8f3e2be545");
		listingDto.setName("other name");
		ListingDto result = listingService.update(listingDto);
		Assertions.assertNotNull(result);
		Assertions.assertEquals("other name", result.getName());

	}

	@Test
	void testFindAll() {
		List<ListingDto> result = listingService.findAll();
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.size() <= 1);
	}

	@Test
	void testDelete() {
		ListingDeletionDto result = listingService.delete("3fce319d-333f-4603-82a0-1e8f3e2be545");
		Assertions.assertNotNull(result);
		Assertions.assertEquals("3fce319d-333f-4603-82a0-1e8f3e2be545", result.getId());
	}

	@Test
	void testFindById() {
		ListingDto result = listingService.findById("3fce319d-333f-4603-82a0-1e8f3e2be545");
		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getId());
		Assertions.assertEquals("3fce319d-333f-4603-82a0-1e8f3e2be545", result.getId());
	}

	@Test
	@Ignore(value = "This test is ignored to avoid an error in the validation of the current date")
	void testAddSpecialPrice() {
		SpecialPriceDto specialPriceDto = SpecialPriceDto.builder().date(LocalDate.of(2022, 03, 01)).price(25D).build();
		SpecialPriceDto result = listingService.addSpecialPrice(specialPriceDto,
				"3fce319d-333f-4603-82a0-1e8f3e2be545");
		Assertions.assertNotNull(result);
		Assertions.assertEquals(25D, result.getPrice());
	}

	@Test
	void testDeleteSpecialPrice() {
		SpecialPriceDeletionDto result = listingService.deleteSpecialPrice("3fce319d-333f-4603-82a0-1e8f3e2be545",
				"81446f17-79ca-4caf-98f8-c796aea7df31");
		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getId());
		Assertions.assertEquals("81446f17-79ca-4caf-98f8-c796aea7df31", result.getId());
	}

	@Test
	@Ignore(value = "This test is ignored to avoid an error in the validation of the current date")
	void testCalculateCostReservation() {
		CheckoutDto checkoutDto = CheckoutDto.builder().checkin(LocalDate.of(2020, 9, 10))
				.checkout(LocalDate.of(2020, 9, 20)).build();
		ReservationCostSummaryDto result = listingService.calculateCostReservation(checkoutDto,
				"3fce319d-333f-4603-82a0-1e8f3e2be545");

		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getNightsCount());
		Assertions.assertNotNull(result.getNightsCost());
		Assertions.assertNotNull(result.getDiscount());
		Assertions.assertNotNull(result.getCleaningFee());
		Assertions.assertNotNull(result.getTotal());
		Assertions.assertEquals(10, result.getNightsCount());
		Assertions.assertEquals(1000D, result.getNightsCost());
		Assertions.assertEquals(50D, result.getDiscount());
		Assertions.assertEquals(100D, result.getCleaningFee());
		Assertions.assertEquals(1050D, result.getTotal());
	}

}
