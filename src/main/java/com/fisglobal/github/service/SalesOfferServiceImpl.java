package com.fisglobal.github.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fisglobal.github.entity.SalesOffer;
import com.fisglobal.github.exception.SalesOfferExistsException;
import com.fisglobal.github.exception.SalesOfferNotFoundException;
import com.fisglobal.github.model.SalesOfferRequest;
import com.fisglobal.github.model.SalesOfferResponse;
import com.fisglobal.github.model.SalesOfferUpdateRequest;
import com.fisglobal.github.repository.SalesOfferRepository;

/**
 * Sales offer service to perform create/fetch and delete operations.
 *
 */
@Component
public class SalesOfferServiceImpl implements SalesOfferService {

	@Autowired
	private SalesOfferRepository repository;

	@Override
	public SalesOfferResponse createOffer(SalesOfferRequest request) {
		SalesOffer offer = repository.findByName(request.getName());
		if (offer != null) {
			throw new SalesOfferExistsException("Sales offer with name: " + request.getName() + " already exists!");
		}
		return transformResponse(repository.save(transformRequest(request)));
	}

	@Override
	public SalesOfferResponse getOfferById(Long offerId) {
		return transformResponse(getOffer(offerId).get());
	}

	private Optional<SalesOffer> getOffer(Long offerId) {
		Optional<SalesOffer> offer = repository.findById(offerId);
		if (offer.isEmpty()) {
			throw new SalesOfferNotFoundException("Sales offer with id: " + offerId + " does not exists!");
		}
		return offer;
	}

	@Override
	public List<SalesOfferResponse> getAllOffers() {
		List<SalesOfferResponse> offerList = new ArrayList<SalesOfferResponse>();
		repository.findAll().forEach(offer -> {
			offerList.add(transformResponse(offer));

		});
		return offerList;
	}

	@Override
	public void deleteOffer(Long offerId) {
		getOffer(offerId);
		repository.deleteById(offerId);
	}

	private SalesOfferResponse transformResponse(SalesOffer offer) {
		SalesOfferResponse offerResponse = null;
		if (offer != null) {
			offerResponse = new SalesOfferResponse();
			offerResponse.setId(offer.getId());
			offerResponse.setName(offer.getName());
			offerResponse.setPrice(offer.getPrice());
			offerResponse.setCurrency(offer.getCurrency());
			offerResponse.setActive(offer.isActive());
			offerResponse.setCreatedDateTime(offer.getCreatedDateTime());
			offerResponse.setExpiryDateTime(offer.getExpiryDateTime());
		}
		return offerResponse;
	}

	private SalesOffer transformRequest(SalesOfferRequest offerRequest) {
		SalesOffer salesOffer = new SalesOffer();
		salesOffer.setName(offerRequest.getName());
		salesOffer.setPrice(offerRequest.getPrice());
		salesOffer.setCurrency(offerRequest.getCurrency());
		salesOffer.setActive(true);
		salesOffer.setCreatedDateTime(LocalDateTime.now());
		salesOffer.setExpiryDateTime(LocalDateTime.now().plusMinutes(offerRequest.getExpiryTimeInMin()));
		return salesOffer;
	}

	@Override
	public SalesOfferResponse updateOffer(SalesOfferUpdateRequest updateRequest) {
		SalesOffer salesOffer = getOffer(updateRequest.getId()).get();
		salesOffer.setName(updateRequest.getName());
		salesOffer.setPrice(updateRequest.getPrice());
		salesOffer.setCurrency(updateRequest.getCurrency());
		salesOffer.setActive(updateRequest.isActive());
		if (updateRequest.getExpiryTimeInMin() != 0) {
			salesOffer.setExpiryDateTime(LocalDateTime.now().plusMinutes(updateRequest.getExpiryTimeInMin()));
		}
		return transformResponse(repository.save(salesOffer));
	}

}
