package com.fisglobal.github.scheduler;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.fisglobal.github.config.ScheduledConfig;
@SpringJUnitConfig({ScheduledConfig.class})
public class SalesOfferUpdateSchedulerTest {
	@SpyBean
	SalesOfferUpdateScheduler salesOfferUpdateScheduler;
	 @Test
	    public void whenWaitOneSecond_checkAndUpdateExpiredSalesOfferTask()  throws InterruptedException {
		 await()
	      .atMost(Durations.ONE_SECOND)
	      .untilAsserted(() -> verify(salesOfferUpdateScheduler, atLeast(1)).checkAndUpdateExpiredSalesOfferTask());
	 }

}
