package com.mahva.diego.saez.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mahva.diego.saez.dto.CheckoutDto;
import com.mahva.diego.saez.dto.ListingDto;
import com.mahva.diego.saez.dto.SpecialPriceDto;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@AutoConfigureMockMvc
@ComponentScan("com.mahva.diego.saez")
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional(readOnly = true)
class ListingControllerTestIt {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testFindAll() throws Exception {
		ResultActions result = mockMvc.perform(get("/api/listings")).andExpect(status().isOk());
		result.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	void testCreate() throws Exception {
		ListingDto listingDto = ListingDto.builder().adults(3).basePrice(10D).children(2).cleaningFee(2D)
				.description("description").imageUrl("url").isPetsAllowed(true).monthlyDiscount(0.1D).name("name")
				.ownerId("0e89855a-bb9f-4c53-a638-3306e4c6a400").build();
		String json = toJson(listingDto);
		ResultActions result = mockMvc
				.perform(post("/api/listings").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
				.andExpect(status().isOk());
		result.andExpect(jsonPath("$.name").value("name"));
		result.andExpect(jsonPath("$.id").isNotEmpty());
	}

	@Test
	void testFindById() throws Exception {
		ResultActions result = mockMvc.perform(get("/api/listings/3fce319d-333f-4603-82a0-1e8f3e2be545"))
				.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").isNotEmpty());
		result.andExpect(jsonPath("$.id").value("3fce319d-333f-4603-82a0-1e8f3e2be545"));
	}

	@Test
	void testDelete() throws Exception {
		ResultActions result = mockMvc.perform(delete("/api/listings/3fce319d-333f-4603-82a0-1e8f3e2be545"))
				.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").isNotEmpty());
		result.andExpect(jsonPath("$.id").value("3fce319d-333f-4603-82a0-1e8f3e2be545"));
	}

	@Test
	void testUpdate() throws Exception {
		ListingDto listingDto = ListingDto.builder().id("3fce319d-333f-4603-82a0-1e8f3e2be545").adults(3).basePrice(10D)
				.children(2).cleaningFee(2D).description("description").imageUrl("url").isPetsAllowed(true)
				.monthlyDiscount(0.1D).name("name updated").ownerId("0e89855a-bb9f-4c53-a638-3306e4c6a400").build();
		String json = toJson(listingDto);
		ResultActions result = mockMvc
				.perform(put("/api/listings").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
				.andExpect(status().isOk());
		result.andExpect(jsonPath("$.name").value("name updated"));
		result.andExpect(jsonPath("$.id").isNotEmpty());
	}

	@Test
	void testAddSpecialPrice() throws Exception {
		SpecialPriceDto specialPriceDto = SpecialPriceDto.builder().price(10D).date(LocalDate.of(2020, 11, 8)).build();
		String json = toJson(specialPriceDto);
		ResultActions result = mockMvc.perform(post("/api/listings/3fce319d-333f-4603-82a0-1e8f3e2be545/special-prices")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andExpect(status().isOk());
		result.andExpect(jsonPath("$.price").isNotEmpty());
		result.andExpect(jsonPath("$.price").value("10.0"));
		result.andExpect(jsonPath("$.id").isNotEmpty());
	}

	@Test
	void testDeleteSpecialPrice() throws Exception {
		ResultActions result = mockMvc.perform(delete(
				"/api/listings/3fce319d-333f-4603-82a0-1e8f3e2be545/special-prices/81446f17-79ca-4caf-98f8-c796aea7df31"))
				.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").isNotEmpty());
		result.andExpect(jsonPath("$.id").value("81446f17-79ca-4caf-98f8-c796aea7df31"));
	}

	@Test
	@Ignore(value = "This test is ignored to avoid an error in the validation of the current date")
	void testCalculateReservationCost() throws Exception {

		CheckoutDto checkoutDto = CheckoutDto.builder().checkin(LocalDate.of(2020, 9, 10))
				.checkout(LocalDate.of(2020, 9, 20)).build();
		String json = toJson(checkoutDto);
		ResultActions result = mockMvc.perform(get("/api/listings/3fce319d-333f-4603-82a0-1e8f3e2be545/checkout")
				.content(json).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());

		result.andExpect(jsonPath("nights_count").isNotEmpty());
		result.andExpect(jsonPath("nights_cost").isNotEmpty());
		result.andExpect(jsonPath("discount").isNotEmpty());
		result.andExpect(jsonPath("cleaning_fee").isNotEmpty());
		result.andExpect(jsonPath("total").isNotEmpty());
		result.andExpect(jsonPath("$.nights_count").value("10"));
		result.andExpect(jsonPath("$.nights_cost").value("1000.0"));
		result.andExpect(jsonPath("$.discount").value("50.0"));
		result.andExpect(jsonPath("$.cleaning_fee").value("100.0"));
		result.andExpect(jsonPath("$.total").value("1050.0"));

	}

	private String toJson(Object object) {
		String json = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			SimpleModule simpleModule = new SimpleModule();
			simpleModule.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
				@Override
				public void serialize(LocalDate date, JsonGenerator jsonGenerator,
						SerializerProvider serializerProvider) throws IOException {
					jsonGenerator.writeString(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				}
			});
			objectMapper.registerModule(simpleModule);
			json = objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			return json;
		}
		return json;
	}
}
