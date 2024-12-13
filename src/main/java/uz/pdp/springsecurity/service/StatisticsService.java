package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BusinessStat;
import uz.pdp.springsecurity.payload.statistics.*;
import uz.pdp.springsecurity.repository.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final BusinessRepository businessRepository;
    private final ProductRepository productRepository;
    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;

    public ApiResponse businessStatistics(String type) {
        LocalDate now = LocalDate.now();
        Timestamp startDate = calculateStartDate(now, type);
        Timestamp endDate = calculateEndDate(now, type);

        BusinessStat businessStat = getBusinessStat(startDate, endDate);
        return new ApiResponse("found", true, businessStat);
    }

    public BusinessStat getBusinessStat(Timestamp startDate, Timestamp endDate) {
        long total = businessRepository.countTotalBetween(startDate, endDate);
        long active = businessRepository.countActiveBetween(startDate, endDate);
        long blocked = businessRepository.countBlockedBetween(startDate, endDate);
        long archived = businessRepository.countArchivedBetween(startDate, endDate);
        long nonActive = businessRepository.countNonActiveBetween(startDate, endDate);
        long freemium = businessRepository.countBusinessesByTariffName("Freemium", startDate, endDate);
        long premium = businessRepository.countBusinessesByTariffName("Premium", startDate, endDate);

        return new BusinessStat(total, active, blocked, archived, nonActive, freemium, premium);
    }

    public ApiResponse productStatistics(String type) {
        LocalDate now = LocalDate.now();
        Timestamp startDate = calculateStartDate(now, type);
        Timestamp endDate = calculateEndDate(now, type);

        ProductStat productStat = getProductStat(startDate, endDate);
        return new ApiResponse("found", true, productStat);
    }

    public ProductStat getProductStat(Timestamp startDate, Timestamp endDate) {
        long total = productRepository.countTotalBetween(startDate, endDate);
        long clone = productRepository.countCloneBetween(startDate, endDate);
        long globalIsFalse = productRepository.countGlobalBetween(startDate, endDate);
        return new ProductStat(total, clone, globalIsFalse);
    }

    public ApiResponse tradeStatistics(String type) {
        LocalDate now = LocalDate.now();
        Timestamp startDate = calculateStartDate(now, type);
        Timestamp endDate = calculateEndDate(now, type);
        TradeStat tradeStat = getTradeStat(startDate, endDate);
        return new ApiResponse("found", true, tradeStat);
    }

    public TradeStat getTradeStat(Timestamp startDate, Timestamp endDate) {
        long totalTrade = tradeRepository.countTotalBetween(startDate, endDate);
        return new TradeStat(totalTrade);
    }


    public ApiResponse userStatistics(String type) {
        LocalDate now = LocalDate.now();
        Timestamp startDate = calculateStartDate(now, type);
        Timestamp endDate = calculateEndDate(now, type);
        UserStat userStat = getUserStat(startDate, endDate);
        return new ApiResponse("found", true, userStat);
    }

    public UserStat getUserStat(Timestamp startDate, Timestamp endDate) {
        long total = userRepository.countTotalBetween(startDate, endDate);
        return new UserStat(total);
    }


    public ApiResponse customerStatistics(String type) {
        LocalDate now = LocalDate.now();
        Timestamp startDate = calculateStartDate(now, type);
        Timestamp endDate = calculateEndDate(now, type);
        CustomerStat customerStat = getCustomerStat(startDate, endDate);
        return new ApiResponse("found", true, customerStat);
    }

    public CustomerStat getCustomerStat(Timestamp startDate, Timestamp endDate) {
        long total = customerRepository.countTotalBetween(startDate, endDate);
        return new CustomerStat(total);
    }


    public ApiResponse supplierStatistics(String type) {
        LocalDate now = LocalDate.now();
        Timestamp startDate = calculateStartDate(now, type);
        Timestamp endDate = calculateEndDate(now, type);
        SupplierStat supplierStat = getSupplierStat(startDate, endDate);
        return new ApiResponse("found", true, supplierStat);
    }

    public SupplierStat getSupplierStat(Timestamp startDate, Timestamp endDate) {
        long total = supplierRepository.countTotalBetween(startDate, endDate);
        return new SupplierStat(total);
    }


    // Vaqt oralig‘ini hisoblash uchun yordamchi metodlar
    private Timestamp calculateStartDate(LocalDate now, String type) {
        LocalDateTime startDateTime;
        switch (type.toLowerCase()) {
            case "daily": // Kundalik
                startDateTime = now.atStartOfDay();
                break;
            case "weekly": // Haftalik
                startDateTime = now.minusDays(now.getDayOfWeek().getValue() - 1).atStartOfDay();
                break;
            case "monthly": // Oylik
                startDateTime = now.withDayOfMonth(1).atStartOfDay();
                break;
            case "yearly": // Yillik
                startDateTime = now.withDayOfYear(1).atStartOfDay();
                break;
            default:
                throw new IllegalArgumentException("Noto'g'ri filter turi: " + type);
        }
        return Timestamp.valueOf(startDateTime);
    }

    private Timestamp calculateEndDate(LocalDate now, String type) {
        LocalDateTime endDateTime;
        switch (type.toLowerCase()) {
            case "daily": // Kundalik
                endDateTime = now.atTime(23, 59, 59);
                break;
            case "weekly": // Haftalik
                endDateTime = now.plusDays(7 - now.getDayOfWeek().getValue()).atTime(23, 59, 59);
                break;
            case "monthly": // Oylik
                endDateTime = now.withDayOfMonth(now.lengthOfMonth()).atTime(23, 59, 59);
                break;
            case "yearly": // Yillik
                endDateTime = now.withDayOfYear(now.lengthOfYear()).atTime(23, 59, 59);
                break;
            default:
                throw new IllegalArgumentException("Noto'g'ri filter turi: " + type);
        }
        return Timestamp.valueOf(endDateTime);
    }


}