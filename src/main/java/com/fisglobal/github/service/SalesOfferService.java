package com.fisglobal.github.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fisglobal.github.model.SalesOfferRequest;
import com.fisglobal.github.model.SalesOfferResponse;
import com.fisglobal.github.model.SalesOfferUpdateRequest;

@Component
public interface SalesOfferService {

	/**
	 * Create sales offer.
	 * 
	 * @param Offer request.
	 * @return sales offer response.
	 */
	public SalesOfferResponse createOffer(SalesOfferRequest request);
	
	/**
	 * Get a sales offer by id.
	 * 
	 * @param Offer Id.
	 * @return sales offer response.
	 */
	public SalesOfferResponse getOfferById(Long offerId);
	
	/**
	 * Get all offers.
	 * 
	 * @return sales offer response.
	 */
	public List<SalesOfferResponse> getAllOffers();
	
	/**
	 * Delete a sales offer by id.
	 * 
	 * @param Offer Id.
	 */
	public void deleteOffer(Long offerId);

	/**
	 * Update sales offer.
	 * 
	 * @param Offer update request.
	 * @return sales offer response.
	 */
	public SalesOfferResponse updateOffer(SalesOfferUpdateRequest updateRequest);
}
