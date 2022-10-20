package com.fisglobal.github.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesOfferResponse {
	private long id;
	private String name;
	private Double price;
	private String currency;
	private LocalDateTime createdDateTime;
	private boolean active;
	private LocalDateTime expiryDateTime;
}
