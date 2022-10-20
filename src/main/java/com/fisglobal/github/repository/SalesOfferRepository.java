package com.fisglobal.github.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fisglobal.github.entity.SalesOffer;

public interface SalesOfferRepository extends CrudRepository<SalesOffer, Long> {
	
	@Query("select offer from SalesOffer offer where offer.name = ?1")
	SalesOffer findByName(String name);
}
