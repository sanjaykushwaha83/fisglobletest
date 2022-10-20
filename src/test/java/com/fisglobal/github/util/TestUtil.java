package com.fisglobal.github.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import com.fisglobal.github.entity.SalesOffer;
import com.fisglobal.github.model.SalesOfferRequest;
import com.fisglobal.github.model.SalesOfferResponse;
import com.fisglobal.github.model.SalesOfferUpdateRequest;

public final class TestUtil {
	private TestUtil() {
	}

	public static SalesOfferRequest createOfferRequest() {
		SalesOfferRequest offerRequest = new SalesOfferRequest("LG TV", 299.99, "GBP", 2);
		return offerRequest;
	}
	public static SalesOfferRequest createOfferRequest2() {
		SalesOfferRequest offerRequest = new SalesOfferRequest("SAMSUNG TV", 399.99, "GBP", 1);
		return offerRequest;
	}
	public static SalesOfferRequest createOfferRequest3() {
		SalesOfferRequest offerRequest = new SalesOfferRequest("LG MONITOR", 299.99, "GBP", 3);
		return offerRequest;
	}

	public static SalesOfferResponse createOfferResponse() {
		SalesOfferResponse offerResponse = new SalesOfferResponse(1, "LG TV", 299.99, "GBP", LocalDateTime.now(), true,
				LocalDateTime.now().plusMinutes(5));
		return offerResponse;
	}
	

	public static SalesOfferUpdateRequest createUpdateRequest() {
		SalesOfferUpdateRequest updateRequest = new SalesOfferUpdateRequest();
		updateRequest.setName("LG TV");
		updateRequest.setPrice(299.99);
		updateRequest.setCurrency("GBP");
		updateRequest.setExpiryTimeInMin(5);
		updateRequest.setActive(true);
		updateRequest.setId(1);
		return updateRequest;
	}
	

	public static  SalesOffer createOffer() {
		SalesOffer offer = new SalesOffer(1, "LG TV", 299.99, "GBP", true, LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(5));
		return offer;
	}
}
