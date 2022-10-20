package com.fisglobal.github.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fisglobal.github.model.SalesOfferRequest;
import com.fisglobal.github.model.SalesOfferResponse;
import com.fisglobal.github.model.SalesOfferUpdateRequest;
import com.fisglobal.github.service.SalesOfferService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/salesoffer")
public class SalesOfferController {

	@Autowired
	private SalesOfferService salesOfferService;

	@PostMapping
	@Operation(summary = "Craete a sales offer with expiry time.")
	public ResponseEntity<SalesOfferResponse> createOffer(@RequestBody @Valid SalesOfferRequest request) {
		SalesOfferResponse createResponse = salesOfferService.createOffer(request);
		return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(summary = "Update a sales offer with expiry time.")
	public ResponseEntity<SalesOfferResponse> updateOffer(@RequestBody @Valid SalesOfferUpdateRequest request) {
		SalesOfferResponse createResponse = salesOfferService.updateOffer(request);
		return new ResponseEntity<>(createResponse, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a sales offer by id.")
	public ResponseEntity<?> getOffer(@PathVariable Long id) {
		SalesOfferResponse response = salesOfferService.getOfferById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getAll")
	@Operation(summary = "Get all sales offers.")
	public ResponseEntity<List<SalesOfferResponse>> getAllOffers() {
		List<SalesOfferResponse> response = salesOfferService.getAllOffers();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a sales offer.")
	public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
		salesOfferService.deleteOffer(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
