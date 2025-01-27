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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountTransactionService.class);

    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void removeExpiredDiscounts() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<Discount> expiredDiscounts = discountRepository.findExpiredDiscounts(startOfDay, endOfDay);

        if (!expiredDiscounts.isEmpty()) {
            expiredDiscounts.forEach(discount -> {
                discount.setActive(false);
                discount.setDeleted(true);
            });

            // ❌ Chegirmasi tugagan mahsulotlar uchun discount ni false qilish
            List<Product> productsToUpdate = expiredDiscounts.stream()
                    .flatMap(discount -> discount.getProducts().stream())
                    .distinct()
                    .peek(product -> product.setDiscount(false))
                    .collect(Collectors.toList());

            productRepository.saveAll(productsToUpdate); // ✅ Mahsulotlar saqlanadi
            discountRepository.saveAll(expiredDiscounts); // ✅ Chegirmalar o‘chiriladi

            logger.info("✅ {} expired discounts removed and {} products updated at: {}",
                    expiredDiscounts.size(), productsToUpdate.size(), LocalDateTime.now());
        } else {
            logger.info("⏳ No expired discounts found at: {}", LocalDateTime.now());
        }
    }

    @Transactional
    public void activateScheduledDiscounts() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<Discount> discountsToActivate = discountRepository.findScheduledDiscounts(startOfDay, endOfDay);

        if (!discountsToActivate.isEmpty()) {
            discountsToActivate.forEach(discount -> discount.setActive(true));

            // ✅ Faollashgan chegirmalar uchun mahsulotlarni yangilash
            List<Product> productsToUpdate = discountsToActivate.stream()
                    .flatMap(discount -> discount.getProducts().stream())
                    .distinct()
                    .peek(product -> product.setDiscount(true))
                    .collect(Collectors.toList());

            productRepository.saveAll(productsToUpdate); // ✅ Mahsulotlar saqlanadi
            discountRepository.saveAll(discountsToActivate); // ✅ Chegirmalar yangilanadi

            logger.info("✅ {} scheduled discounts activated and {} products updated at: {}",
                    discountsToActivate.size(), productsToUpdate.size(), LocalDateTime.now());
        } else {
            logger.info("⏳ No scheduled discounts found at: {}", LocalDateTime.now());
        }
    }
}