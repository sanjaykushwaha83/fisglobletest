package com.fisglobal.github.controller;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisglobal.github.exception.SalesOfferExistsException;
import com.fisglobal.github.exception.SalesOfferNotFoundException;
import com.fisglobal.github.model.SalesOfferRequest;
import com.fisglobal.github.model.SalesOfferResponse;
import com.fisglobal.github.model.SalesOfferUpdateRequest;
import com.fisglobal.github.service.SalesOfferService;
import com.fisglobal.github.util.TestUtil;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = SalesOfferController.class)
public class SalesOfferControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();

	@MockBean
	private SalesOfferService salesOfferService;

	private static final String[] VALIDATION_MESSAGES = {
			"ExpiryTime (in minutes) is mandatory and must be more than or equals to 1.", "Offer price is mandatory.",
			"Offer name is mandatory.", "Offer currency is mandatory." };

	@Test
	public void createOffer_With_Valid_Request_Successfull() throws Exception {
		SalesOfferResponse offerResponse = TestUtil.createOfferResponse();
		SalesOfferRequest offerRequest = TestUtil.createOfferRequest();
		Mockito.when(salesOfferService.createOffer(Mockito.any(SalesOfferRequest.class))).thenReturn(offerResponse);
		ResultActions response = mockMvc.perform(post("/api/salesoffer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(offerRequest)));
		verifyResponse(response, 201, offerResponse);

	}

	private void verifyResponse(ResultActions response, int httpStatus, SalesOfferResponse offerResponse)
			throws Exception {
		response.andDo(print()).andExpect(status().is(httpStatus))
				.andExpect(jsonPath("$.name", is(offerResponse.getName()))).andExpect(jsonPath("$.active", is(true)))
				.andExpect(jsonPath("$.price", is(offerResponse.getPrice())))
				.andExpect(jsonPath("$.currency", is(offerResponse.getCurrency())));
	}

	@Test
	public void createOffer_With_Empty_Request_GotValidationMessages() throws Exception {
		SalesOfferRequest offerRequest = new SalesOfferRequest();
		Mockito.when(salesOfferService.createOffer(Mockito.any(SalesOfferRequest.class))).thenReturn(null);
		ResultActions response = mockMvc.perform(post("/api/salesoffer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(offerRequest)));
		response.andDo(print()).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$", hasItems(VALIDATION_MESSAGES)));
	}

	@Test
	public void createOffer_With_Valid_Request_AlreadyExistException() throws Exception {
		String errorMessage = "This offer is already exist!";
		SalesOfferRequest offerRequest = new SalesOfferRequest("LG TV", 299.99, "GBP", 2);
		Mockito.when(salesOfferService.createOffer(Mockito.any(SalesOfferRequest.class)))
				.thenThrow(new SalesOfferExistsException(errorMessage));
		ResultActions response = mockMvc.perform(post("/api/salesoffer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(offerRequest)));
		response.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$", is(errorMessage)));
	}

	@Test
	public void getOffer_ById_Valid_Response() throws Exception {
		long offerId = 1;
		SalesOfferResponse offerResponse = new SalesOfferResponse(offerId, "LG TV", 299.99, "GBP", LocalDateTime.now(),
				true, LocalDateTime.now().plusMinutes(5));

		Mockito.when(salesOfferService.getOfferById(offerId)).thenReturn(offerResponse);
		ResultActions response = mockMvc.perform(get("/api/salesoffer/{id}", offerId));
		verifyResponse(response, 200, offerResponse);

	}

	@Test
	public void getOffer_ById_NotFound() throws Exception {
		long offerId = 11;
		Mockito.when(salesOfferService.getOfferById(offerId)).thenThrow(SalesOfferNotFoundException.class);
		ResultActions response = mockMvc.perform(get("/api/salesoffer/{id}", offerId));
		response.andDo(print()).andExpect(status().is4xxClientError());
	}

	@Test
	public void getAllOffers_Valid_Response() throws Exception {
		SalesOfferResponse offerResponse1 = TestUtil.createOfferResponse();
		SalesOfferResponse offerResponse2 = new SalesOfferResponse(2, "SAMSUNG TV", 199.99, "GBP", LocalDateTime.now(),
				false, LocalDateTime.now().plusMinutes(5));
		SalesOfferResponse offerResponse3 = new SalesOfferResponse(3, "LG MONITOR", 99.99, "GBP", LocalDateTime.now(),
				true, LocalDateTime.now().plusMinutes(5));

		Mockito.when(salesOfferService.getAllOffers())
				.thenReturn(Arrays.asList(offerResponse1, offerResponse2, offerResponse3));
		ResultActions response = mockMvc.perform(get("/api/salesoffer/getAll"));
		response.andDo(print()).andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$", Matchers.hasSize(3)));

	}

	@Test
	public void deleteOffer_ById_Success() throws Exception {
		long offerId = 1;
		Mockito.doNothing().when(salesOfferService).deleteOffer(offerId);
		ResultActions response = mockMvc.perform(delete("/api/salesoffer/{id}", offerId));
		response.andDo(print()).andExpect(status().is2xxSuccessful());

	}

	@Test
	public void deleteOffer_ById_OfferNotFound() throws Exception {
		long offerId = 11;
		Mockito.doThrow(SalesOfferNotFoundException.class).when(salesOfferService).deleteOffer(offerId);
		ResultActions response = mockMvc.perform(delete("/api/salesoffer/{id}", offerId));
		response.andDo(print()).andExpect(status().is4xxClientError());

	}

	@Test
	public void updateOffer_With_Valid_Request_Successfull() throws Exception {
		SalesOfferResponse offerResponse = TestUtil.createOfferResponse();
		SalesOfferUpdateRequest updateRequest = TestUtil.createUpdateRequest();
		Mockito.when(salesOfferService.updateOffer(Mockito.any(SalesOfferUpdateRequest.class)))
				.thenReturn(offerResponse);
		ResultActions response = mockMvc.perform(put("/api/salesoffer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)));
		verifyResponse(response, 200, offerResponse);

	}

	@Test
	public void updateOffer_With_Empty_Request_GotValidationMessages() throws Exception {
		SalesOfferUpdateRequest updateRequest = new SalesOfferUpdateRequest();
		Mockito.when(salesOfferService.updateOffer(Mockito.any(SalesOfferUpdateRequest.class))).thenReturn(null);
		ResultActions response = mockMvc.perform(put("/api/salesoffer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)));
		response.andDo(print()).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$", hasItems(VALIDATION_MESSAGES)));
	}
}
