package com.fisglobal.github.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesOfferUpdateRequest extends SalesOfferRequest {
	private long id;
	private boolean active;
}
