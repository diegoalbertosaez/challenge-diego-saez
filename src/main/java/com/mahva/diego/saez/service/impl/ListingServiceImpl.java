package com.mahva.diego.saez.service.impl;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mahva.diego.saez.dto.CheckoutDto;
import com.mahva.diego.saez.dto.ListingDeletionDto;
import com.mahva.diego.saez.dto.ListingDto;
import com.mahva.diego.saez.dto.ReservationCostSummaryDto;
import com.mahva.diego.saez.dto.SpecialPriceDeletionDto;
import com.mahva.diego.saez.dto.SpecialPriceDto;
import com.mahva.diego.saez.exception.BadArgumentException;
import com.mahva.diego.saez.exception.BusinessException;
import com.mahva.diego.saez.exception.NotFoundException;
import com.mahva.diego.saez.mapper.ListingMapper;
import com.mahva.diego.saez.mapper.SpecialPriceMapper;
import com.mahva.diego.saez.model.Listing;
import com.mahva.diego.saez.model.SpecialPrice;
import com.mahva.diego.saez.repository.ListingRepository;
import com.mahva.diego.saez.repository.SpecialPriceRepository;
import com.mahva.diego.saez.repository.UserRepository;
import com.mahva.diego.saez.service.ListingService;
import com.mahva.diego.saez.util.LoggerHelper;

/**
 * 
 * Business layer service to manage listings.
 * 
 * @author diegosaez
 *
 */
@Service
public class ListingServiceImpl implements ListingService {

	private final Logger logger = LoggerFactory.getLogger(ListingServiceImpl.class);
	private static final int DAYS_OF_MONTH = 30;
	private static final int DAYS_OF_WEEK = 7;
	private final ListingRepository listingRepository;
	private final UserRepository userRepository;
	private final SpecialPriceRepository specialPriceRepository;
	private final Integer numberOfNightLimit;
	private final Clock clock;

	/**
	 * Constructor with injected dependencies.
	 * 
	 * @param listingRepository
	 * @param listingMapper
	 * @author diegosaez
	 */
	@Autowired
	public ListingServiceImpl(ListingRepository listingRepository, UserRepository userRepository,
			SpecialPriceRepository specialPriceRepository, @Value("${nights.limit}") Integer numberOfNightLimit,
			Clock clock) {
		this.listingRepository = listingRepository;
		this.userRepository = userRepository;
		this.specialPriceRepository = specialPriceRepository;
		this.numberOfNightLimit = numberOfNightLimit;
		this.clock = clock;
	}

	/**
	 * Create a new listing.
	 * 
	 * @param listingDto listing to create
	 * @author diegosaez
	 */
	@Override
	@Transactional
	public ListingDto create(ListingDto listingDto) {

		validateListingEmpty(listingDto);
		validateUserExistence(listingDto.getOwnerId());
		validateNameEmpty(listingDto.getName());
		validateBasePriceEmpty(listingDto.getBasePrice());
		listingDto.setId(UUID.randomUUID().toString());
		Listing listingToCreate = ListingMapper.toModel(listingDto);

		return ListingMapper.toDto(listingRepository.save(listingToCreate));

	}

	/**
	 * Update a listing.
	 * 
	 * 
	 * @param listingDto to update
	 * @return {@link ListingDto} updated
	 */
	@Override
	@Transactional
	public ListingDto update(ListingDto listingDto) {

		validateListingEmpty(listingDto);
		validateUserExistence(listingDto.getOwnerId());
		validateNameEmpty(listingDto.getName());
		validateBasePriceEmpty(listingDto.getBasePrice());

		try {

			validateListingExistence(listingDto.getId());
			Listing listingUpdated = listingRepository.save(ListingMapper.toModel(listingDto));
			listingDto = ListingMapper.toDto(listingUpdated);

		} catch (DataAccessException e) {
			LoggerHelper.error(logger, "Error to update listing.", e);
			throw new BusinessException("Error to update listing.", e);
		}

		return listingDto;

	}

	/**
	 * Get all listings.
	 * 
	 * @author diegosaez
	 */
	@Override
	public List<ListingDto> findAll() {

		List<ListingDto> listingDtos = new ArrayList<>();
		try {
			List<Listing> listings = listingRepository.findAll();
			listings.forEach(listing -> listingDtos.add(ListingMapper.toDto(listing)));
		} catch (DataAccessException e) {
			LoggerHelper.error(logger, "Error getting a list of listing.", e);
			throw new BusinessException("Error getting a list of listing.", e);
		}

		return listingDtos;
	}

	/**
	 * Delete a listing by id.
	 * 
	 * @param id id of listing to delete
	 * @author diegosaez
	 */
	@Override
	@Transactional
	public ListingDeletionDto delete(String id) {

		try {
			validateListingExistence(id);
			listingRepository.deleteById(id);
		} catch (DataAccessException e) {
			LoggerHelper.error(logger, "Error to delete listing.", e);
			throw new BusinessException("Error to delete listing.", e);
		}
		return new ListingDeletionDto(id);
	}

	/**
	 * Find a listing by its id.
	 * 
	 * @author diegosaez
	 */
	@Override
	public ListingDto findById(String id) {
		ListingDto listingDto = null;

		try {
			validateListingExistence(id);
			Optional<Listing> listingOptional = listingRepository.findById(id);
			if (listingOptional.isPresent()) {
				listingDto = ListingMapper.toDto(listingOptional.get());
			}
		} catch (DataAccessException e) {
			LoggerHelper.error(logger, "Error getting a listing.", e);
			throw new BusinessException("Error getting a listing.", e);
		}

		return listingDto;

	}

	/**
	 * Add special price to listing.
	 * 
	 * @param specialPriceDto special price to add
	 * @param listingId       listing id to associate special price
	 * @return {@link SpecialPriceDto}
	 */
	@Override
	@Transactional
	public SpecialPriceDto addSpecialPrice(SpecialPriceDto specialPriceDto, String listingId) {

		validateListingExistence(listingId);
		validateSpecialPrice(listingId, specialPriceDto);
		SpecialPrice specialPriceToAdd = SpecialPriceMapper.toModel(specialPriceDto);
		specialPriceToAdd.setListing(Listing.builder().id(listingId).build());
		specialPriceToAdd.setId(UUID.randomUUID().toString());

		return SpecialPriceMapper.toDto(specialPriceRepository.save(specialPriceToAdd));
	}

	/**
	 * Delete a special price from listing.
	 * 
	 * @param listingId      listing containing the special price
	 * @param specialPriceId special price to delete
	 * @return {@link SpecialPriceDeletionDto}
	 */
	@Override
	@Transactional
	public SpecialPriceDeletionDto deleteSpecialPrice(String listingId, String specialPriceId) {

		validateSpecialPriceIdEmpty(specialPriceId);
		validateListingExistence(listingId);
		Listing listing = listingRepository.getOne(listingId);
		Optional<SpecialPrice> specialPriceOptional = listing.getSpecialPrices().stream()
				.filter(specialPrice -> specialPriceId.equals(specialPrice.getId())).findFirst();

		if (specialPriceOptional.isPresent()) {
			specialPriceRepository.deleteById(specialPriceId);
		} else {
			throw new BadArgumentException("The special price does not belong to the listing");
		}

		return new SpecialPriceDeletionDto(specialPriceId);
	}

	/**
	 * Calculate the reservation cost for the dates.
	 * 
	 * @param checkoutDto - Checkin and checkout date to calculate the reservation
	 * @return {@link ReservationCostSummaryDto}
	 */
	@Override
	public ReservationCostSummaryDto calculateCostReservation(CheckoutDto checkoutDto, String listingId) {

		validateListingExistence(listingId);
		validateDatesForCheckout(checkoutDto);
		int nights = calculateAndValidateNights(checkoutDto);
		Listing listing = listingRepository.getOne(listingId);
		List<SpecialPrice> specialPrices = listing.getSpecialPrices();
		BigDecimal totalBasePrice = BigDecimal.ZERO;
		BigDecimal specialPriceTotal = BigDecimal.ZERO;
		BigDecimal total = null;
		LocalDate currentDate = checkoutDto.getCheckin();
		boolean hasSpecialPrice = false;
		for (int i = 1; i <= nights; i++) {
			Optional<SpecialPrice> specialPriceOptional = findSpecialPriceByDate(specialPrices, currentDate);
			if (specialPriceOptional.isPresent()) {
				hasSpecialPrice = true;
				specialPriceTotal = specialPriceTotal.add(BigDecimal.valueOf(specialPriceOptional.get().getPrice()));
				totalBasePrice = totalBasePrice.add(BigDecimal.valueOf(specialPriceOptional.get().getPrice()));
			} else {
				totalBasePrice = totalBasePrice.add(BigDecimal.valueOf(listing.getBasePrice()));
			}
			currentDate = currentDate.plusDays(1);

		}

		Double discount = null;

		if (hasSpecialPrice) {
			LoggerHelper.debug(logger, "The listing has special price ".concat(specialPriceTotal.toString()));
			discount = specialPriceTotal.doubleValue();
		} else {
			LoggerHelper.debug(logger, "The listing hasn't special price, calculating other discount");
			discount = totalBasePrice.multiply(BigDecimal.valueOf(calculateDiscount(listing, nights))).doubleValue();
		}

		Double cleaningFee = calculateCleaningFee(nights, listing.getCleaningFee());
		total = totalBasePrice.subtract(BigDecimal.valueOf(discount)).add(BigDecimal.valueOf(cleaningFee));

		return new ReservationCostSummaryDto(nights, totalBasePrice.doubleValue(), discount, cleaningFee,
				total.doubleValue());

	}

	/**
	 * Calculate the discount to apply.
	 * 
	 * @param listing
	 * @param nights
	 * @return discount
	 */
	private Double calculateDiscount(Listing listing, int nights) {

		Double discount = 0D;
		if (nights >= DAYS_OF_MONTH) {
			LoggerHelper.debug(logger, "Applying monthly discount");
			discount = calculateMonthlyDiscount(listing, nights);
		} else if (nights >= DAYS_OF_WEEK) {
			LoggerHelper.debug(logger, "Applying weekly discount");
			discount = calculateWeeklyDiscount(listing, nights);
		}
		LoggerHelper.debug(logger, "Discount: ".concat(discount.toString()));
		return discount;

	}

	/**
	 * Calculate the weekly discount.
	 * 
	 * @param listing
	 * @param nights
	 * @return Double
	 */
	private Double calculateWeeklyDiscount(Listing listing, int nights) {
		Double weeklyDiscount = listing.getWeeklyDiscount();
		int weeks = nights / DAYS_OF_WEEK;
		return weeks * weeklyDiscount;
	}

	/**
	 * Calculate the month discount.
	 * 
	 * @param listing
	 * @param nights
	 * @return Double
	 */
	private Double calculateMonthlyDiscount(Listing listing, int nights) {
		Double monthlyDiscount = listing.getMonthlyDiscount();
		int months = nights / 30;
		return months * monthlyDiscount;
	}

	/**
	 * Valid if the number of nights exceeds the maximum established.
	 * 
	 * @param checkoutDto
	 * @return int
	 */
	private int calculateAndValidateNights(CheckoutDto checkoutDto) {
		long nights = ChronoUnit.DAYS.between(checkoutDto.getCheckin(), checkoutDto.getCheckout());
		if (numberOfNightLimit.compareTo((int) nights) < 0) {
			LoggerHelper.error(logger, "Can't exceed ".concat(numberOfNightLimit.toString()).concat(" nights"));
			throw new BusinessException("Can't exceed ".concat(numberOfNightLimit.toString()).concat(" nights"));
		}
		return (int) nights;
	}

	/**
	 * Validate dates checkin and checkout: Checkin date must be after today.
	 * Checkout date must be after checkin date.
	 * 
	 * @param checkoutDto
	 */
	private void validateDatesForCheckout(CheckoutDto checkoutDto) {
		if (checkoutDto.getCheckin() == null) {
			LoggerHelper.error(logger, "The checkin date can't be null");
			throw new BadArgumentException("The checkin date can't be null");
		}

		if (checkoutDto.getCheckout() == null) {
			LoggerHelper.error(logger, "The checkout date can't be null");
			throw new BadArgumentException("The checkout date can't be null");
		}

		LocalDate now = LocalDate.now(clock);

		if (checkoutDto.getCheckin().isBefore(now)) {
			LoggerHelper.error(logger, "The checkin date must be after the current date");
			throw new BadArgumentException("The checkin date must be after the current date");
		}

		if (checkoutDto.getCheckout().isBefore(checkoutDto.getCheckin())) {
			LoggerHelper.error(logger, "The checkin date must be before the checkout date");
			throw new BadArgumentException("The checkin date must be before the checkout date");
		}
	}

	/**
	 * Calculate the cleaning fee for all nights.
	 * 
	 * @param nights
	 * @param cleaningFee
	 * @return Double
	 */
	private Double calculateCleaningFee(Integer nights, Double cleaningFee) {
		Double totalCleaningFee = 0D;
		if (cleaningFee != null) {
			totalCleaningFee = nights * cleaningFee;
		}
		LoggerHelper.debug(logger, "Calculating cleaning fee ".concat(totalCleaningFee.toString()));
		return totalCleaningFee;
	}

	/**
	 * Find a special price in list by a date.
	 * 
	 * @param specialPrices - list with special prices
	 * @param date          - date to find
	 * @return Optional<SpecialPrice>
	 */
	private Optional<SpecialPrice> findSpecialPriceByDate(List<SpecialPrice> specialPrices, LocalDate date) {
		LoggerHelper.debug(logger, "Searching special price in date ".concat(date.toString()));
		Optional<SpecialPrice> specialPriceOptional = Optional.empty();
		if (!CollectionUtils.isEmpty(specialPrices)) {
			specialPriceOptional = specialPrices.stream().filter(specialPrice -> specialPrice.getDate().equals(date))
					.findFirst();
		}

		return specialPriceOptional;
	}

	/**
	 * Validate if specialPriceId parameter is empty or null.
	 * 
	 * @param specialPriceId to validate
	 */
	private void validateSpecialPriceIdEmpty(String specialPriceId) {

		if (StringUtils.isEmpty(specialPriceId)) {
			LoggerHelper.error(logger, "The specialPriceId can't be empty!");
			throw new BadArgumentException("The specialPriceId can't be empty!");
		}
	}

	/**
	 * Validate if a listingDto is null.
	 * 
	 * @param listingDto
	 */
	private void validateListingEmpty(ListingDto listingDto) {
		if (listingDto == null) {
			LoggerHelper.error(logger, "The specialPriceId can't be empty!");
			throw new BadArgumentException("The specialPriceId can't be empty!");
		}
	}

	/**
	 * Validate special price: Not empty, date not empty, price not empty and date
	 * must be after today.
	 * 
	 * @param specialPriceDto
	 */
	private void validateSpecialPrice(String listingId, SpecialPriceDto specialPriceDto) {

		if (specialPriceDto == null) {
			LoggerHelper.error(logger, "The special price can't be empty!");
			throw new BadArgumentException("The special price can't be empty!");
		}

		if (specialPriceDto.getDate() == null) {
			LoggerHelper.error(logger, "The date can't be empty!");
			throw new BadArgumentException("The date can't be empty!");
		}

		if (specialPriceDto.getPrice() == null) {
			LoggerHelper.error(logger, "The price can't be empty!");
			throw new BadArgumentException("The price can't be empty!");
		}

		LocalDate now = LocalDate.now();

		if (specialPriceDto.getDate().isBefore(now)) {
			LoggerHelper.error(logger, "The date must be after today!");
			throw new BadArgumentException("The date must be after today!");
		}

		Listing listing = listingRepository.getOne(listingId);

		if (specialPriceDto.getPrice() > listing.getBasePrice()) {
			LoggerHelper.error(logger,
					"The special price must be lowered to ".concat(listing.getBasePrice().toString()));
			throw new BusinessException(
					"The special price must be lowered to ".concat(listing.getBasePrice().toString()));
		}

		if (specialPriceRepository.existsByListingIdAndDate(listingId, specialPriceDto.getDate())) {
			LoggerHelper.error(logger, "The listing already have a special price in this date");
			throw new BadArgumentException("The listing already have a special price in this date");
		}

	}

	/**
	 * Validate if name parameter is empty or null.
	 * 
	 * @param name to validate
	 */
	private void validateNameEmpty(String name) {
		if (StringUtils.isEmpty(name)) {
			LoggerHelper.error(logger, "The name can't be empty!");
			throw new BadArgumentException("The name can't be empty!");
		}
	}

	/**
	 * Validate if the basePrice parameter is empty or null.
	 * 
	 * @param basePrice to validate
	 */
	private void validateBasePriceEmpty(Double basePrice) {
		if (basePrice == null) {
			LoggerHelper.error(logger, "The base price can't be empty!");
			throw new BadArgumentException("The base price can't be empty!");
		}
	}

	/**
	 * Validate existence of user.
	 * 
	 * @param userId id of user to validate
	 */
	private void validateUserExistence(String userId) {

		if (StringUtils.isEmpty(userId)) {
			LoggerHelper.error(logger, "The user id can't be empty!");
			throw new BadArgumentException("The user id can't be empty!");
		}

		if (!userRepository.existsById(userId)) {
			LoggerHelper.error(logger, "The owner with id ".concat(userId).concat(" does not exist!"));
			throw new NotFoundException("The owner with id ".concat(userId).concat(" does not exist!"));
		}

	}

	/**
	 * Validate existence of listing.
	 * 
	 * @param listingId id of listing to validate
	 */
	private void validateListingExistence(String listingId) {

		if (StringUtils.isEmpty(listingId)) {
			LoggerHelper.error(logger, "The listing id can't be empty!");
			throw new BadArgumentException("The listing id can't be empty!");
		}

		if (!listingRepository.existsById(listingId)) {
			LoggerHelper.error(logger, "The listing with id ".concat(listingId).concat(" does not exist."));
			throw new NotFoundException("The listing with id ".concat(listingId).concat(" does not exist."));
		}

	}

}
