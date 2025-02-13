package uz.pdp.springsecurity.service.schedules;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountScheduleService.class);

    private final DiscountTransactionService discountTransactionService;


   @Scheduled(cron = "0 0 0 * * ?") // Har kuni soat 00:00 da ishga tushadi
    public void processDiscounts() {
        try {
            logger.info("🔄 Removing expired discounts...");
            discountTransactionService.removeExpiredDiscounts();

            logger.info("🔄 Activating scheduled discounts...");
            discountTransactionService.activateScheduledDiscounts();
        } catch (Exception e) {
            logger.error("❌ Error in discount processing: ", e);
        }
    }
}
