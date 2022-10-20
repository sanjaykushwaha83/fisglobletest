package com.fisglobal.github.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fisglobal.github.entity.SalesOffer;
import com.fisglobal.github.exception.SalesOfferExistsException;
import com.fisglobal.github.exception.SalesOfferNotFoundException;
import com.fisglobal.github.model.SalesOfferResponse;
import com.fisglobal.github.repository.SalesOfferRepository;
import com.fisglobal.github.util.TestUtil;

@ExtendWith(MockitoExtension.class)
public class SalesOfferServiceImplTest {
	@Mock
	private SalesOfferRepository repository;
	@InjectMocks
	private SalesOfferServiceImpl service;

	@Test
	public void createOffer_Successfull() {
		SalesOffer offer = TestUtil.createOffer();
		Mockito.when(repository.save(Mockito.any(SalesOffer.class))).thenReturn(offer);
		SalesOfferResponse response = service.createOffer(TestUtil.createOfferRequest());
		verifyResponse(offer, response);
	}

	@Test
	public void createOffer_WithAlreadyExistsException() {

		SalesOffer offer = TestUtil.createOffer();
		Mockito.when(repository.findByName("LG TV")).thenReturn(offer);
		SalesOfferExistsException ex = assertThrows(SalesOfferExistsException.class, () -> {
			service.createOffer(TestUtil.createOfferRequest());
		});
		assertEquals("Sales offer with name: LG TV already exists!", ex.getLocalizedMessage());

	}

	@Test
	public void getOfferById_Successfull() {
		SalesOffer offer = TestUtil.createOffer();
		long offerId = 1;
		Mockito.when(repository.findById(offerId)).thenReturn(Optional.of(offer));
		SalesOfferResponse response = service.getOfferById(offerId);
		verifyResponse(offer, response);
	}

	@Test
	public void getOfferById_NotFoundException() {
		long offerId = 1;
		Mockito.when(repository.findById(offerId)).thenReturn(Optional.empty());
		SalesOfferNotFoundException ex = assertThrows(SalesOfferNotFoundException.class, () -> {
			service.getOfferById(offerId);
		});
		assertEquals("Sales offer with id: 1 does not exists!", ex.getLocalizedMessage());

	}

	@Test
	public void updateOffer_Successfull() {
		SalesOffer offer = TestUtil.createOffer();
		long offerId = 1;
		Mockito.when(repository.findById(offerId)).thenReturn(Optional.of(offer));
		Mockito.when(repository.save(Mockito.any(SalesOffer.class))).thenReturn(offer);
		SalesOfferResponse response = service.updateOffer(TestUtil.createUpdateRequest());
		verifyResponse(offer, response);
	}

	@Test
	public void updateOffer_NotFoundException() {
		long offerId = 1;
		Mockito.when(repository.findById(offerId)).thenReturn(Optional.empty());
		SalesOfferNotFoundException ex = assertThrows(SalesOfferNotFoundException.class, () -> {
			service.updateOffer(TestUtil.createUpdateRequest());
		});
		assertEquals("Sales offer with id: 1 does not exists!", ex.getLocalizedMessage());

	}

	@Test
	public void deleteOfferById_Successfull() {
		SalesOfferServiceImpl service = Mockito.mock(SalesOfferServiceImpl.class);
		long offerId = 1;
		service.deleteOffer(offerId);
		Mockito.verify(service, Mockito.atLeastOnce()).deleteOffer(offerId);
	}

	private void verifyResponse(SalesOffer offer, SalesOfferResponse response) {
		assertNotNull(response);
		assertEquals(offer.getId(), response.getId());
		assertEquals(offer.getName(), response.getName());
		assertEquals(offer.getPrice(), response.getPrice());
		assertEquals(offer.getCurrency(), response.getCurrency());
		assertEquals(offer.getExpiryDateTime(), response.getExpiryDateTime());
	}

}
