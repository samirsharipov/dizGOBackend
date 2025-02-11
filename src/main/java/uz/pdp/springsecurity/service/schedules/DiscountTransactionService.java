package uz.pdp.springsecurity.service.schedules;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.springsecurity.entity.Discount;
import uz.pdp.springsecurity.entity.Product;
import uz.pdp.springsecurity.repository.DiscountRepository;
import uz.pdp.springsecurity.repository.ProductRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountTransactionService.class);

    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void removeExpiredDiscounts() {
        processDiscounts(
                discountRepository::findExpiredDiscounts,
                discount -> {
                    discount.setActive(false);
                    discount.setDeleted(true);
                },
                false,
                "expired"
        );
    }

    @Transactional
    public void activateScheduledDiscounts() {
        processDiscounts(
                discountRepository::findScheduledDiscounts,
                discount -> discount.setActive(true),
                true,
                "scheduled"
        );
    }

    private void processDiscounts(
            DiscountFinder discountFinder,
            Consumer<Discount> discountUpdater,
            boolean newDiscountState,
            String discountType
    ) {
        Timestamp[] timestamps = getTimestampRangeForToday();
        List<Discount> discounts = discountFinder.findDiscounts(timestamps[0], timestamps[1]);

        if (!discounts.isEmpty()) {
            discounts.forEach(discountUpdater);

            List<Product> productsToUpdate = discounts.stream()
                    .flatMap(discount -> discount.getProducts().stream())
                    .distinct()
                    .peek(product -> product.setDiscount(newDiscountState))
                    .collect(Collectors.toList());

            productRepository.saveAll(productsToUpdate);
            discountRepository.saveAll(discounts);

            logResults(discounts.size(), productsToUpdate.size(), discountType);
        } else {
            logger.info("⏳ No {} discounts found at: {}", discountType, LocalDateTime.now());
        }
    }

    private Timestamp[] getTimestampRangeForToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        return new Timestamp[]{Timestamp.valueOf(startOfDay), Timestamp.valueOf(endOfDay)};
    }

    private void logResults(int discountCount, int productCount, String discountType) {
        logger.info("✅ {} {} discounts processed and {} products updated at: {}",
                discountCount, discountType, productCount, LocalDateTime.now());
    }

    @FunctionalInterface
    private interface DiscountFinder {
        List<Discount> findDiscounts(Timestamp start, Timestamp end);
    }
}