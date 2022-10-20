package com.fisglobal.github.integrationtest;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisglobal.github.model.SalesOfferRequest;
import com.fisglobal.github.model.SalesOfferUpdateRequest;
import com.fisglobal.github.util.TestUtil;

@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureMockMvc
public class IntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void createGetUpdateDeleteOffer_Success() throws Exception {
		// Creating multiple offers and verifying
		createAndVerifyMultipleOffers();
		Thread.sleep(60000);
		// Get and verify Offer with Id 1
		getAndVerifyOffer();
		// Get and verify all Offers with all active status
		getAndVerifyAllOffers();

		// Update and verify offer with id 1
		updateAndVerifyOffer();
		Thread.sleep(60000);
		// Get and verify all Offers after scheduler executed, second offer should be
		// inactive now.
		getAndVerifyAllOffersAfterOneMin();

	}

	private void createAndVerifyMultipleOffers() throws Exception, JsonProcessingException {
		SalesOfferRequest createRequest = TestUtil.createOfferRequest();
		SalesOfferRequest createRequest2 = TestUtil.createOfferRequest2();
		SalesOfferRequest createRequest3 = TestUtil.createOfferRequest3();

		createAndVerifyOffer(createRequest);
		createAndVerifyOffer(createRequest2);
		createAndVerifyOffer(createRequest3);
	}

	private void updateAndVerifyOffer() throws Exception, JsonProcessingException {
		SalesOfferUpdateRequest updateRequest = TestUtil.createUpdateRequest();
		updateRequest.setId(1);
		updateRequest.setName("LG TV NEW");
		ResultActions updateResponse = mockMvc.perform(put("/api/salesoffer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)));
		verifyResponse(updateResponse, 200, updateRequest);
	}

	private void getAndVerifyOffer() throws Exception {
		ResultActions getresponse = mockMvc.perform(get("/api/salesoffer/{id}", 1));
		verifyResponse(getresponse, 200, TestUtil.createOfferRequest());
	}

	private void getAndVerifyAllOffers() throws Exception {
		ResultActions getresponse = mockMvc.perform(get("/api/salesoffer/getAll"));
		verifyGetAllResponse(getresponse, 200, false);
	}

	private void getAndVerifyAllOffersAfterOneMin() throws Exception {
		ResultActions getresponse = mockMvc.perform(get("/api/salesoffer/getAll"));
		verifyGetAllResponse(getresponse, 200, true);
	}

	private void createAndVerifyOffer(SalesOfferRequest createRequest) throws Exception, JsonProcessingException {
		ResultActions response = mockMvc.perform(post("/api/salesoffer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createRequest)));
		verifyResponse(response, 201, createRequest);
	}

	private void verifyResponse(ResultActions response, int httpStatus, SalesOfferRequest offerResponse)
			throws Exception {
		response.andDo(print()).andExpect(status().is(httpStatus))
				.andExpect(jsonPath("$.name", is(offerResponse.getName()))).andExpect(jsonPath("$.active", is(true)))
				.andExpect(jsonPath("$.price", is(offerResponse.getPrice())))
				.andExpect(jsonPath("$.currency", is(offerResponse.getCurrency())));
	}

	private void verifyGetAllResponse(ResultActions response, int httpStatus, boolean isVeryAfterSchedulerRun)
			throws Exception {
		SalesOfferRequest expResponse1 = TestUtil.createOfferRequest();
		SalesOfferRequest expResponse2 = TestUtil.createOfferRequest2();
		SalesOfferRequest expResponse3 = TestUtil.createOfferRequest3();
		response.andDo(print()).andExpect(status().is(httpStatus)).andExpect(jsonPath("$", Matchers.hasSize(3)))
				.andExpect(jsonPath("$.[0].name", is(isVeryAfterSchedulerRun ? "LG TV NEW" : expResponse1.getName())))
				.andExpect(jsonPath("$.[0].active", is(true)))
				.andExpect(jsonPath("$.[0].price", is(expResponse1.getPrice())))
				.andExpect(jsonPath("$.[0].currency", is(expResponse1.getCurrency())))
				.andExpect(jsonPath("$.[1].name", is(expResponse2.getName())))
				.andExpect(jsonPath("$.[1].active", is(isVeryAfterSchedulerRun ? false : true)))
				.andExpect(jsonPath("$.[1].price", is(expResponse2.getPrice())))
				.andExpect(jsonPath("$.[1].currency", is(expResponse2.getCurrency())))
				.andExpect(jsonPath("$.[2].name", is(expResponse3.getName())))
				.andExpect(jsonPath("$.[2].active", is(true)))
				.andExpect(jsonPath("$.[2].price", is(expResponse3.getPrice())))
				.andExpect(jsonPath("$.[2].currency", is(expResponse3.getCurrency())));
	}

}
