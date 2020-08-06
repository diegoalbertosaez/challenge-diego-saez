package com.mahva.diego.saez.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "special_price")
public class SpecialPrice {

	@Id
	private String id;

	@Column(name = "listing_id", insertable = false, updatable = false)
	private String listingId;

	@ManyToOne(fetch = FetchType.LAZY)
	private Listing listing;

	private LocalDate date;

	private Double price;

}
