package com.mahva.diego.saez.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutDto {

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate checkin;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate checkout;

}
