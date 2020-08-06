package com.mahva.diego.saez.service.impl;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.jdbc.datasource.init.ScriptParseException;

import com.mahva.diego.saez.dto.CheckoutDto;
import com.mahva.diego.saez.dto.ListingDto;
import com.mahva.diego.saez.dto.ReservationCostSummaryDto;
import com.mahva.diego.saez.dto.SpecialPriceDto;
import com.mahva.diego.saez.exception.BadArgumentException;
import com.mahva.diego.saez.exception.BusinessException;
import com.mahva.diego.saez.exception.NotFoundException;
import com.mahva.diego.saez.model.Listing;
import com.mahva.diego.saez.model.SpecialPrice;
import com.mahva.diego.saez.repository.ListingRepository;
import com.mahva.diego.saez.repository.SpecialPriceRepository;
import com.mahva.diego.saez.repository.UserRepository;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ListingServiceImpl.class, LocalDate.class })
class ListingServiceImplTest {

	private static final int NIGHTS_LIMIT = 28;

	@Test
	void testListingServiceImpl() {
		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(null, null, null, null, null);
		Assertions.assertNotNull(listingServiceImpl);
	}

	@Test
	void testCreate() {
		ListingDto listingDto = ListingDto.builder().adults(3).basePrice(10D).children(2).cleaningFee(2D)
				.description("description").imageUrl("url").isPetsAllowed(true).monthlyDiscount(0.1D).name("name")
				.ownerId("11a0c438-8ed5-42a6-b9e9-f137ed24ef24").build();
		Listing listing = Listing.builder().id("aee298cd-4197-48f1-9a25-31ef8e95cb79").adults(3).basePrice(10D)
				.children(2).cleaningFee(2D).description("description").imageUrl("url").isPetsAllowed(true)
				.monthlyDiscount(0.1D).name("name").ownerId("11a0c438-8ed5-42a6-b9e9-f137ed24ef24").build();

		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.save(EasyMock.anyObject(Listing.class))).andReturn(listing);
		UserRepository userRepositoryMock = EasyMock.createMock(UserRepository.class);
		EasyMock.expect(userRepositoryMock.existsById(EasyMock.anyString())).andReturn(true);
		EasyMock.replay(listingRepositoryMock, userRepositoryMock);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, userRepositoryMock, null,
				null, null);
		ListingDto result = listingServiceImpl.create(listingDto);
		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getId());
		Assertions.assertNotNull(result.getImageUrl());
		Assertions.assertNotNull(result.getName());
		Assertions.assertNotNull(result.getOwnerId());
		Assertions.assertEquals("name", result.getName());
		Assertions.assertEquals("aee298cd-4197-48f1-9a25-31ef8e95cb79", result.getId());
		Assertions.assertEquals("11a0c438-8ed5-42a6-b9e9-f137ed24ef24", result.getOwnerId());
		EasyMock.verify(listingRepositoryMock, userRepositoryMock);

	}

	@Test
	void testCreate_EmptyOwnerId_ShouldTrowBadArgumentException() {
		ListingServiceImpl listingService = new ListingServiceImpl(null, null, null, null, null);
		ListingDto listingDto = ListingDto.builder().build();
		Assertions.assertThrows(BadArgumentException.class, () -> listingService.create(listingDto));
	}

	@Test
	void testCreate_EmptyName_ShouldTrowBadArgumentException() {
		UserRepository userRepositoryMock = EasyMock.createMock(UserRepository.class);
		EasyMock.expect(userRepositoryMock.existsById(EasyMock.anyString())).andReturn(true);
		EasyMock.replay(userRepositoryMock);

		ListingServiceImpl listingService = new ListingServiceImpl(null, userRepositoryMock, null, null, null);
		ListingDto listingDto = ListingDto.builder().ownerId("owner").build();
		Assertions.assertThrows(BadArgumentException.class, () -> listingService.create(listingDto));
		EasyMock.verify(userRepositoryMock);
	}

	@Test
	void testCreate_EmptyListingDto_ShouldTrowBadArgumentException() {
		ListingServiceImpl listingService = new ListingServiceImpl(null, null, null, null, null);
		Assertions.assertThrows(BadArgumentException.class, () -> listingService.create(null));
	}

	@Test
	void testUpdate() {
		ListingDto listingDto = ListingDto.builder().id("aee298cd-4197-48f1-9a25-31ef8e95cb79").adults(3).basePrice(10D)
				.children(2).cleaningFee(2D).description("description").imageUrl("url").isPetsAllowed(true)
				.monthlyDiscount(0.1D).name("name-upated").ownerId("11a0c438-8ed5-42a6-b9e9-f137ed24ef24").build();
		Listing listing = Listing.builder().id("aee298cd-4197-48f1-9a25-31ef8e95cb79").adults(3).basePrice(10D)
				.children(2).cleaningFee(2D).description("description").imageUrl("url").isPetsAllowed(true)
				.monthlyDiscount(0.1D).name("name-upated").ownerId("11a0c438-8ed5-42a6-b9e9-f137ed24ef24").build();

		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.save(EasyMock.anyObject(Listing.class))).andReturn(listing);
		EasyMock.expect(listingRepositoryMock.existsById(EasyMock.anyString())).andReturn(true);
		UserRepository userRepositoryMock = EasyMock.createMock(UserRepository.class);
		EasyMock.expect(userRepositoryMock.existsById(EasyMock.anyString())).andReturn(true);
		EasyMock.replay(listingRepositoryMock, userRepositoryMock);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, userRepositoryMock, null,
				null, null);

		ListingDto result = listingServiceImpl.update(listingDto);
		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getId());
		Assertions.assertNotNull(result.getImageUrl());
		Assertions.assertNotNull(result.getName());
		Assertions.assertNotNull(result.getOwnerId());
		Assertions.assertEquals("name-upated", result.getName());
		Assertions.assertEquals("aee298cd-4197-48f1-9a25-31ef8e95cb79", result.getId());
		Assertions.assertEquals("11a0c438-8ed5-42a6-b9e9-f137ed24ef24", result.getOwnerId());
		EasyMock.verify(listingRepositoryMock, userRepositoryMock);

	}

	@Test
	void testUpdate_ListingDoesNotExist_ShouldTrowNotFoundException() {

		ListingDto listingDto = ListingDto.builder().id("aee298cd-4197-48f1-9a25-31ef8e95cb79").adults(3).basePrice(10D)
				.children(2).cleaningFee(2D).description("description").imageUrl("url").isPetsAllowed(true)
				.monthlyDiscount(0.1D).name("name-upated").ownerId("11a0c438-8ed5-42a6-b9e9-f137ed24ef24").build();

		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.existsById(EasyMock.anyString())).andReturn(false);
		UserRepository userRepositoryMock = EasyMock.createMock(UserRepository.class);
		EasyMock.expect(userRepositoryMock.existsById(EasyMock.anyString())).andReturn(true);
		EasyMock.replay(listingRepositoryMock, userRepositoryMock);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, userRepositoryMock, null,
				null, null);
		Assertions.assertThrows(NotFoundException.class, () -> listingServiceImpl.update(listingDto));
		EasyMock.verify(listingRepositoryMock);

	}

	@Test
	void testFindAll() {
		List<Listing> listings = new ArrayList<>();
		listings.add(Listing.builder().id("e5e3a0bf-4358-4ccb-9666-f791699acf18").build());
		listings.add(Listing.builder().id("b19aadaa-19e2-48dc-b427-5042dd515582").build());

		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.findAll()).andReturn(listings);
		EasyMock.replay(listingRepositoryMock);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, null, null, null, null);

		List<ListingDto> result = listingServiceImpl.findAll();

		Assertions.assertNotNull(result);
		Assertions.assertEquals(2, result.size());
		EasyMock.verify(listingRepositoryMock);
	}

	@Test
	void testFindAll_RespositoryFail_ShouldTrowBusinessException() {
		List<Listing> listings = new ArrayList<>();
		listings.add(Listing.builder().id("e5e3a0bf-4358-4ccb-9666-f791699acf18").build());
		listings.add(Listing.builder().id("b19aadaa-19e2-48dc-b427-5042dd515582").build());

		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.findAll()).andThrow(new ScriptParseException("Error to listing", null));
		EasyMock.replay(listingRepositoryMock);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, null, null, null, null);

		Assertions.assertThrows(BusinessException.class, () -> listingServiceImpl.findAll());
		EasyMock.verify(listingRepositoryMock);

	}

	@Test
	void testDelete() {
		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.existsById(EasyMock.anyString())).andReturn(true);
		listingRepositoryMock.deleteById(EasyMock.anyString());
		EasyMock.expectLastCall();
		EasyMock.replay(listingRepositoryMock);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, null, null, null, null);
		listingServiceImpl.delete("b19aadaa-19e2-48dc-b427-5042dd515582");
		EasyMock.verify(listingRepositoryMock);

	}

	@Test
	void testDelete_ListingDoesNotExist_ShouldThrowNotFoundException() {
		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.existsById(EasyMock.anyString())).andReturn(false);
		EasyMock.replay(listingRepositoryMock);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, null, null, null, null);
		Assertions.assertThrows(NotFoundException.class,
				() -> listingServiceImpl.delete("b19aadaa-19e2-48dc-b427-5042dd515582"));
		EasyMock.verify(listingRepositoryMock);
	}

	@Test
	void testFindById() {
		Optional<Listing> listingOptional = Optional
				.of(Listing.builder().id("e5e3a0bf-4358-4ccb-9666-f791699acf18").build());
		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.existsById(EasyMock.anyString())).andReturn(true);
		EasyMock.expect(listingRepositoryMock.findById(EasyMock.anyString())).andReturn(listingOptional);
		EasyMock.replay(listingRepositoryMock);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, null, null, null, null);
		ListingDto result = listingServiceImpl.findById("e5e3a0bf-4358-4ccb-9666-f791699acf18");
		Assertions.assertNotNull(result);
		Assertions.assertEquals("e5e3a0bf-4358-4ccb-9666-f791699acf18", result.getId());
		EasyMock.verify(listingRepositoryMock);
	}

	@Test
	void testFindById_ListingDoesNotExist_ShouldTrowNotFoundException() {
		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.existsById(EasyMock.anyString())).andReturn(false);
		EasyMock.replay(listingRepositoryMock);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, null, null, null, null);
		Assertions.assertThrows(NotFoundException.class,
				() -> listingServiceImpl.findById("xxxxxx-xxx-xxxx-xxxx-xxxxxxx"));
		EasyMock.verify(listingRepositoryMock);
	}

	@Test
	void testAddSpecialPrice() {
		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.existsById(EasyMock.anyString())).andReturn(true);
		EasyMock.expect(listingRepositoryMock.getOne(EasyMock.anyString()))
				.andReturn(Listing.builder().basePrice(500D).build());
		SpecialPriceRepository specialPriceRepository = EasyMock.createMock(SpecialPriceRepository.class);
		EasyMock.expect(specialPriceRepository.save(EasyMock.anyObject(SpecialPrice.class)))
				.andReturn(SpecialPrice.builder().id("52d5f505-d887-46c8-a1a8-3466cdde744d").build());
		EasyMock.expect(specialPriceRepository.existsByListingIdAndDate(EasyMock.anyObject(String.class),
				EasyMock.anyObject(LocalDate.class))).andReturn(false);

		EasyMock.replay(listingRepositoryMock, specialPriceRepository);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, null,
				specialPriceRepository, null, null);
		SpecialPriceDto specialPriceDto = SpecialPriceDto.builder().price(10D).date(LocalDate.of(2020, 11, 8)).build();
		SpecialPriceDto result = listingServiceImpl.addSpecialPrice(specialPriceDto,
				"e5e3a0bf-4358-4ccb-9666-f791699acf18");

		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getId());
		Assertions.assertEquals("52d5f505-d887-46c8-a1a8-3466cdde744d", result.getId());
		EasyMock.verify(listingRepositoryMock, specialPriceRepository);
	}

	@Test
	void testAddSpecialPrice_DateBeforeToToday_ShouldThrowBadArgumentException() {
		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.existsById(EasyMock.anyString())).andReturn(true);
		EasyMock.replay(listingRepositoryMock);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, null, null, null, null);
		SpecialPriceDto specialPriceDto = SpecialPriceDto.builder().price(10D).date(LocalDate.of(2020, 7, 8)).build();
		Assertions.assertThrows(BadArgumentException.class,
				() -> listingServiceImpl.addSpecialPrice(specialPriceDto, "e5e3a0bf-4358-4ccb-9666-f791699acf18"));

		EasyMock.verify(listingRepositoryMock);
	}

	@Test
	void testDeleteSpecialPrice() {
		List<SpecialPrice> specialPrices = new ArrayList<>();
		specialPrices.add(SpecialPrice.builder().id("c9440071-2db1-453a-b173-4012600bd83f").build());
		Listing listing = Listing.builder().id("23ff39c8-ac91-4774-9584-f804d4a48dbd").specialPrices(specialPrices)
				.build();
		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.existsById(EasyMock.anyString())).andReturn(true);
		EasyMock.expect(listingRepositoryMock.getOne(EasyMock.anyString())).andReturn(listing);
		SpecialPriceRepository specialPriceRepository = EasyMock.createMock(SpecialPriceRepository.class);
		specialPriceRepository.deleteById(EasyMock.anyString());
		EasyMock.expectLastCall();
		EasyMock.replay(listingRepositoryMock, specialPriceRepository);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, null,
				specialPriceRepository, null, null);
		listingServiceImpl.deleteSpecialPrice("23ff39c8-ac91-4774-9584-f804d4a48dbd",
				"c9440071-2db1-453a-b173-4012600bd83f");
		EasyMock.verify(listingRepositoryMock);
	}

	@Test
	void testDeleteSpecialPrice_SpecialPriceNotBelongToListing_ShouldTrowBadArgumentException() {
		List<SpecialPrice> specialPrices = new ArrayList<>();
		specialPrices.add(SpecialPrice.builder().id("c9440071-2db1-453a-b173-4012600bd83f").build());
		Listing listing = Listing.builder().id("23ff39c8-ac91-4774-9584-f804d4a48dbd").specialPrices(specialPrices)
				.build();
		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.existsById(EasyMock.anyString())).andReturn(true);
		EasyMock.expect(listingRepositoryMock.getOne(EasyMock.anyString())).andReturn(listing);
		SpecialPriceRepository specialPriceRepository = EasyMock.createMock(SpecialPriceRepository.class);
		specialPriceRepository.deleteById(EasyMock.anyString());
		EasyMock.expectLastCall();
		EasyMock.replay(listingRepositoryMock, specialPriceRepository);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, null,
				specialPriceRepository, null, null);
		Assertions.assertThrows(BadArgumentException.class, () -> listingServiceImpl
				.deleteSpecialPrice("23ff39c8-ac91-4774-9584-f804d4a48dbd", "44946467-92f2-486a-b171-f040094f7993"));
		EasyMock.verify(listingRepositoryMock);
	}

	@Test
	void testCalculateCostReservation() {
		CheckoutDto checkoutDto = CheckoutDto.builder().checkin(LocalDate.of(2020, 9, 20))
				.checkout(LocalDate.of(2020, 9, 30)).build();

		Listing listing = Listing.builder().id("23ff39c8-ac91-4774-9584-f804d4a48dbd").basePrice(100D)
				.weeklyDiscount(0.05).cleaningFee(20D).build();// discount weekly 5%

		Clock clock = Clock.fixed(Instant.parse("2020-08-04T00:00:00.00Z"), ZoneId.of("UTC"));
		ListingRepository listingRepositoryMock = EasyMock.createMock(ListingRepository.class);
		EasyMock.expect(listingRepositoryMock.existsById(EasyMock.anyString())).andReturn(true);
		EasyMock.expect(listingRepositoryMock.getOne(EasyMock.anyString())).andReturn(listing);
		EasyMock.replay(listingRepositoryMock);

		ListingServiceImpl listingServiceImpl = new ListingServiceImpl(listingRepositoryMock, null, null, NIGHTS_LIMIT,
				clock);
		ReservationCostSummaryDto result = listingServiceImpl.calculateCostReservation(checkoutDto,
				"23ff39c8-ac91-4774-9584-f804d4a48dbd");

		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getCleaningFee());
		Assertions.assertNotNull(result.getDiscount());
		Assertions.assertNotNull(result.getNightsCost());
		Assertions.assertNotNull(result.getNightsCount());
		Assertions.assertNotNull(result.getTotal());

		Assertions.assertEquals(200D, result.getCleaningFee());
		Assertions.assertEquals(50D, result.getDiscount());
		Assertions.assertEquals(1000D, result.getNightsCost());
		Assertions.assertEquals(10, result.getNightsCount());
		Assertions.assertEquals(1150, result.getTotal());
		EasyMock.verify(listingRepositoryMock);
	}

}
