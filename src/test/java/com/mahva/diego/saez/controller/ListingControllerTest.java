package com.mahva.diego.saez.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mahva.diego.saez.dto.CheckoutDto;
import com.mahva.diego.saez.dto.ListingDeletionDto;
import com.mahva.diego.saez.dto.ListingDto;
import com.mahva.diego.saez.dto.ReservationCostSummaryDto;
import com.mahva.diego.saez.dto.SpecialPriceDeletionDto;
import com.mahva.diego.saez.dto.SpecialPriceDto;
import com.mahva.diego.saez.service.ListingService;
import com.mahva.diego.saez.service.impl.ListingServiceImpl;

class ListingControllerTest {

	@Test
	void testListingController() {
		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(null, null, null, null, null);
		ListingController controller = new ListingController(listingServiceImpl);
		Assertions.assertNotNull(controller);

	}

	@Test
	void testFindAll() {
		List<ListingDto> listings = new ArrayList<>();
		listings.add(ListingDto.builder().id("44946467-92f2-486a-b171-f040094f7993").build());
		listings.add(ListingDto.builder().id("59fd2ef3-9b97-4230-a216-0c135f26f44c").build());

		ListingServiceImpl listingServiceImplMock = EasyMock.createMock(ListingServiceImpl.class);
		EasyMock.expect(listingServiceImplMock.findAll()).andReturn(listings);
		EasyMock.replay(listingServiceImplMock);
		ListingController controller = new ListingController(listingServiceImplMock);
		List<ListingDto> result = controller.findAll();
		Assertions.assertNotNull(result);
		Assertions.assertEquals(2, result.size());
		EasyMock.verify(listingServiceImplMock);
	}

	@Test
	void testCreate() {

		ListingDto listingDto = ListingDto.builder().basePrice(250D).name("name").build();
		ListingService listingServiceImplMock = EasyMock.createMock(ListingServiceImpl.class);
		EasyMock.expect(listingServiceImplMock.create(EasyMock.anyObject(ListingDto.class))).andReturn(listingDto);
		EasyMock.replay(listingServiceImplMock);

		ListingController controller = new ListingController(listingServiceImplMock);

		ListingDto result = controller.create(listingDto);
		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getName());
		Assertions.assertNotNull(result.getBasePrice());
		Assertions.assertEquals("name", result.getName());
		Assertions.assertEquals(250D, result.getBasePrice());
		EasyMock.verify(listingServiceImplMock);

	}

	@Test
	void testFindById() {
		ListingDto listingDto = ListingDto.builder().id("c74dfb72-0bb6-49eb-b11d-398871dac915").basePrice(250D)
				.name("name").build();
		ListingService listingServiceImplMock = EasyMock.createMock(ListingServiceImpl.class);
		EasyMock.expect(listingServiceImplMock.findById(EasyMock.anyString())).andReturn(listingDto);
		EasyMock.replay(listingServiceImplMock);

		ListingController controller = new ListingController(listingServiceImplMock);
		ListingDto result = controller.findById("c74dfb72-0bb6-49eb-b11d-398871dac915");
		Assertions.assertNotNull(result);
		Assertions.assertEquals("c74dfb72-0bb6-49eb-b11d-398871dac915", result.getId());
		EasyMock.verify(listingServiceImplMock);

	}

	@Test
	void testDelete() {
		ListingService listingServiceImplMock = EasyMock.createMock(ListingServiceImpl.class);
		EasyMock.expect(listingServiceImplMock.delete(EasyMock.anyString()))
				.andReturn(ListingDeletionDto.builder().id("c74dfb72-0bb6-49eb-b11d-398871dac915").build());
		EasyMock.replay(listingServiceImplMock);

		ListingController controller = new ListingController(listingServiceImplMock);
		ListingDeletionDto result = controller.delete("c74dfb72-0bb6-49eb-b11d-398871dac915");
		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getId());
		Assertions.assertEquals("c74dfb72-0bb6-49eb-b11d-398871dac915", result.getId());
		EasyMock.verify(listingServiceImplMock);
	}

	@Test
	void testUpdate() {
		ListingDto listingDto = ListingDto.builder().id("c74dfb72-0bb6-49eb-b11d-398871dac915").name("updated name")
				.build();
		ListingService listingServiceImplMock = EasyMock.createMock(ListingServiceImpl.class);
		EasyMock.expect(listingServiceImplMock.update(EasyMock.anyObject(ListingDto.class))).andReturn(listingDto);
		EasyMock.replay(listingServiceImplMock);

		ListingController controller = new ListingController(listingServiceImplMock);
		controller.update(listingDto);
		EasyMock.verify(listingServiceImplMock);
	}

	@Test
	void testAddSpecialPrice() {
		SpecialPriceDto specialPriceDto = SpecialPriceDto.builder().date(LocalDate.now()).price(20D).build();
		ListingService listingServiceImplMock = EasyMock.createMock(ListingServiceImpl.class);
		EasyMock.expect(
				listingServiceImplMock.addSpecialPrice(EasyMock.anyObject(SpecialPriceDto.class), EasyMock.anyString()))
				.andReturn(specialPriceDto);
		EasyMock.replay(listingServiceImplMock);

		ListingController controller = new ListingController(listingServiceImplMock);
		SpecialPriceDto result = controller.addSpecialPrice(specialPriceDto, "c74dfb72-0bb6-49eb-b11d-398871dac91");
		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getPrice());
		Assertions.assertNotNull(result.getDate());
		EasyMock.verify(listingServiceImplMock);

	}

	@Test
	void testDeleteSpecialPrice() {
		SpecialPriceDeletionDto specialPriceDeletionDto = SpecialPriceDeletionDto.builder()
				.id("c74dfb72-0bb6-49eb-b11d-398871dac91").build();
		ListingService listingServiceImplMock = EasyMock.createMock(ListingServiceImpl.class);
		EasyMock.expect(listingServiceImplMock.deleteSpecialPrice(EasyMock.anyString(), EasyMock.anyString()))
				.andReturn(specialPriceDeletionDto);
		EasyMock.replay(listingServiceImplMock);

		ListingController controller = new ListingController(listingServiceImplMock);
		SpecialPriceDeletionDto result = controller.deleteSpecialPrice("c74dfb72-0bb6-49eb-b11d-398871dac91",
				"c74dfb72-0bb6-49eb-b11d-398871dac915");
		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getId());
		Assertions.assertEquals("c74dfb72-0bb6-49eb-b11d-398871dac91", result.getId());
		EasyMock.verify(listingServiceImplMock);
	}

	@Test
	void testCalculateReservationCost() {
		CheckoutDto checkoutDto = CheckoutDto.builder().checkin(LocalDate.of(2020, 8, 10))
				.checkout(LocalDate.of(2020, 8, 21)).build();
		ReservationCostSummaryDto costSummaryDto = ReservationCostSummaryDto.builder().discount(50D).nightsCost(1000D)
				.cleaningFee(100D).nightsCount(11).total(1050D).build();
		ListingService listingServiceImplMock = EasyMock.createMock(ListingServiceImpl.class);
		EasyMock.expect(
				listingServiceImplMock.calculateCostReservation(checkoutDto, "c74dfb72-0bb6-49eb-b11d-398871dac91"))
				.andReturn(costSummaryDto);
		EasyMock.replay(listingServiceImplMock);

		ListingController controller = new ListingController(listingServiceImplMock);
		ReservationCostSummaryDto result = controller.calculateReservationCost(checkoutDto,
				"c74dfb72-0bb6-49eb-b11d-398871dac91");
		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getCleaningFee());
		Assertions.assertNotNull(result.getDiscount());
		Assertions.assertNotNull(result.getNightsCost());
		Assertions.assertNotNull(result.getNightsCount());
		Assertions.assertNotNull(result.getTotal());
		Assertions.assertEquals(100D, result.getCleaningFee());
		Assertions.assertEquals(50D, result.getDiscount());
		Assertions.assertEquals(1000D, result.getNightsCost());
		Assertions.assertEquals(11, result.getNightsCount());
		Assertions.assertEquals(1050D, result.getTotal());
		EasyMock.verify(listingServiceImplMock);
	}

}
