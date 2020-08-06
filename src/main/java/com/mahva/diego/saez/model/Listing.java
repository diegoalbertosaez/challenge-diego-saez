package com.mahva.diego.saez.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "listing")
public class Listing {

	@Id
	private String id;

	@Column(name = "owner_id")
	private String ownerId;

	private String name;

	private String slug;

	private String description;

	private Integer adults;

	private Integer children;

	@Column(name = "is_pets_allowed")
	private Boolean isPetsAllowed;

	@Column(name = "base_price")
	private Double basePrice;

	@Column(name = "cleaning_fee")
	private Double cleaningFee;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "weekly_discount")
	private Double weeklyDiscount;

	@Column(name = "monthly_discount")
	private Double monthlyDiscount;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "listing")
	private List<SpecialPrice> specialPrices;
}
