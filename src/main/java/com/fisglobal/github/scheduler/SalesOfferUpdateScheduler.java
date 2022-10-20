package com.fisglobal.github.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fisglobal.github.model.SalesOfferResponse;
import com.fisglobal.github.model.SalesOfferUpdateRequest;
import com.fisglobal.github.service.SalesOfferService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SalesOfferUpdateScheduler {

	@Autowired
	private SalesOfferService salesOfferService;

	@Scheduled(fixedRateString = "${sales.offer.update.schedule.time.in.milliseconds:1000}")
	public void checkAndUpdateExpiredSalesOfferTask() throws InterruptedException {
		log.info("Offer cleanup task ran at: - " + LocalDateTime.now());

		List<SalesOfferResponse> offers = salesOfferService.getAllOffers();
		for (SalesOfferResponse offer : offers) {
			log.info("id:"+offer.getId()+" CreatedDateTime:"+offer.getCreatedDateTime()+" ExpiryDateTime:"+offer.getExpiryDateTime()+" isActive:"+offer.isActive());
			if (LocalDateTime.now().isAfter(offer.getExpiryDateTime())) {
				SalesOfferResponse res= salesOfferService.updateOffer(transformRequset(offer));
				log.info("After update: id:"+res.getId()+" CreatedDateTime:"+res.getCreatedDateTime()+" ExpiryDateTime:"+res.getExpiryDateTime()+" isActive:"+res.isActive());
			}
		}
		//Thread.sleep(10000);
	}

	private SalesOfferUpdateRequest transformRequset(SalesOfferResponse offer) {
		SalesOfferUpdateRequest updateOffer = new SalesOfferUpdateRequest();
		updateOffer.setName(offer.getName());
		updateOffer.setActive(false);
		updateOffer.setId(offer.getId());
		updateOffer.setCurrency(offer.getCurrency());
		updateOffer.setPrice(offer.getPrice());
		return updateOffer;
	}

}
