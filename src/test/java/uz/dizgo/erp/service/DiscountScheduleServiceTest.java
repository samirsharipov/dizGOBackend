package uz.dizgo.erp.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uz.dizgo.erp.service.schedules.DiscountScheduleService;
import uz.dizgo.erp.service.schedules.DiscountTransactionService;

import static org.mockito.Mockito.*;

public class DiscountScheduleServiceTest {

    @InjectMocks
    private DiscountScheduleService discountScheduleService;

    @Mock
    private DiscountTransactionService discountTransactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessDiscounts() {
        // Act: processDiscounts metodini chaqirish
        discountScheduleService.processDiscounts(); // Bu yerda @Scheduled metodini sinovdan o'tkazamiz

        // Assert: expected metodlar chaqirilganligini tekshirish
        verify(discountTransactionService, times(1)).removeExpiredDiscounts();
        verify(discountTransactionService, times(1)).activateScheduledDiscounts();
    }

    @Test
    public void testProcessDiscountsWithException() {
        // Arrange: Exception throw qilish
        doThrow(new RuntimeException("Test exception")).when(discountTransactionService).removeExpiredDiscounts();

        // Act: Exceptionni simulyatsiya qilish
        discountScheduleService.processDiscounts();

        // Assert: exception handling va loggingni tekshirish
        verify(discountTransactionService, times(1)).removeExpiredDiscounts();
        verify(discountTransactionService, never()).activateScheduledDiscounts(); // removeExpiredDiscounts xato bo'lganda activateScheduledDiscounts chaqirilmaydi
    }
}