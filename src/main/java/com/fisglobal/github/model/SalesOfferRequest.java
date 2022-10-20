package com.fisglobal.github.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SalesOfferRequest {
	@NotBlank(message = "Offer name is mandatory.")
	private String name;
	@NotNull(message = "Offer price is mandatory.")
	@Positive(message = "Offer price must be a positive number.")
	private Double price;
	@NotBlank(message = "Offer currency is mandatory.")
	private String currency;
	@DecimalMin(message = "ExpiryTime (in minutes) is mandatory and must be more than or equals to 1.", value = "1")
	private long expiryTimeInMin;
}
