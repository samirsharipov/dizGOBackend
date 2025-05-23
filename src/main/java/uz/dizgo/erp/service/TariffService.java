package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Subscription;
import uz.dizgo.erp.entity.Tariff;
import uz.dizgo.erp.enums.Lifetime;
import uz.dizgo.erp.mapper.TariffMapper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.TariffBusinessInfoDto;
import uz.dizgo.erp.payload.TariffDto;
import uz.dizgo.erp.repository.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TariffService {
    private final TariffRepository repository;
    private final TariffMapper mapper;
    private final SubscriptionRepository subscriptionRepository;
    private final TradeRepository tradeRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    public ApiResponse getAll() {
        List<Tariff> all = repository.findAll();
        List<Tariff> tariffList = new ArrayList<>();
        for (Tariff tariff : all) {
            if (!tariff.isDelete()) {
                tariffList.add(tariff);
            }
        }
        tariffList.sort(Comparator.comparing(Tariff::getPrice));
        return new ApiResponse("all tariff", true, mapper.toDtoList(tariffList));
    }

    public ApiResponse getToChooseATariff() {
        List<Tariff> all = repository.findAll();
        List<Tariff> tariffList = new ArrayList<>();
        for (Tariff tariff : all) {
            if (!tariff.isDelete() && tariff.isActive()) {
                tariffList.add(tariff);
            }
        }
        tariffList.sort(Comparator.comparing(Tariff::getPrice));
        return new ApiResponse("all tariff", true, mapper.toDtoList(tariffList));
    }

    public ApiResponse getById(UUID id) {
        Optional<Tariff> optionalTariff = repository.findById(id);
        if (optionalTariff.isEmpty()) {
            return new ApiResponse("not found tariff", false);
        }
        Tariff tariff = optionalTariff.get();
        TariffDto dto = mapper.toDto(tariff);
        dto.setPermissions(tariff.getPermissions());
        return new ApiResponse("found", true, dto);
    }


    public ApiResponse create(TariffDto tariffDto) {
        Tariff tariff = mapper.toEntity(tariffDto);
        tariff.setPermissions(tariffDto.getPermissions());
        repository.save(tariff);
        return new ApiResponse("SUCCESS", true);
    }


    public ApiResponse edit(UUID id, TariffDto tariffDto) {
        Optional<Tariff> optionalTariff = repository.findById(id);
        if (optionalTariff.isEmpty()) {
            return new ApiResponse("not found tariff", false);
        }

        Tariff tariff = optionalTariff.get();
        mapper.update(tariffDto, tariff);
        tariff.setPermissions(tariffDto.getPermissions());
        repository.save(tariff);

        return new ApiResponse("successfully edited", true);
    }

    public ApiResponse delete(UUID id) {
        Optional<Tariff> optionalTariff = repository.findById(id);
        if (optionalTariff.isEmpty()) {
            return new ApiResponse("not found tariff", false);
        }

        Tariff tariff = optionalTariff.get();
        tariff.setDelete(true);
        repository.save(tariff);
        return new ApiResponse("successfully deleted", true);
    }

    public ApiResponse businessInfo(UUID businessId) {
        Optional<Subscription> optionalSubscription = subscriptionRepository
                .findByBusinessIdAndActiveTrue(businessId);

        if (optionalSubscription.isEmpty())
            return new ApiResponse("No subscription found for this business", false);

        TariffBusinessInfoDto businessInfoDto = new TariffBusinessInfoDto();
        Subscription subscription = optionalSubscription.get();
        Timestamp subscriptionDate = subscription.getCreatedAt();
        Tariff tariff = subscription.getTariff();  // Tarif obyektini olish
        String tariffName = tariff.getName();  // Tarif nomini olish

        // Tariff ma'lumotlarini DTO ga qo'shish
        businessInfoDto.setTariffName(tariffName);  // Tarif nomini qo'shish

        // ... boshqa ma'lumotlarni hisoblash (testDay, tradeAmount va boshqalar)
        int testDay = tariff.getTestDay();
        LocalDate today = LocalDate.now();

        double tradeAmount = tradeRepository
                .countAllByBranch_BusinessIdAndCreatedAtBetween(businessId,
                        subscriptionDate, new Timestamp(System.currentTimeMillis()));

        int branchAmount = branchRepository.countAllByBusiness_Id(businessId);
        int employeeAmount = userRepository.countAllByBranches_Business_IdAndActiveIsTrue(businessId);

        LocalDate targetDate = new Date(subscriptionDate.getTime())
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        long resultTestDay = testDay - ChronoUnit.DAYS.between(today, targetDate);

        LocalDate nextMonth = today.plusMonths(tariff.getLifetime().equals(Lifetime.YEAR)
                ? tariff.getInterval() * 12L : tariff.getInterval());

        long daysBetween = ChronoUnit.DAYS.between(today, nextMonth);
        long allDays = ChronoUnit.DAYS.between(targetDate, nextMonth);

        businessInfoDto.setTestDay(tariff.getTestDay());
        businessInfoDto.setYourTestDay(resultTestDay > 0 ? resultTestDay : 0);
        businessInfoDto.setTradeAmount(tariff.getTradeAmount());
        businessInfoDto.setYourTradeAmount((long) tradeAmount);
        businessInfoDto.setBranchAmount(tariff.getBranchAmount());
        businessInfoDto.setYourBranchAmount(branchAmount);
        businessInfoDto.setEmployeeAmount(tariff.getEmployeeAmount());
        businessInfoDto.setYourEmployeeAmount(employeeAmount);
        businessInfoDto.setIntervalDay(allDays);
        businessInfoDto.setRemainingDays(daysBetween);

        return new ApiResponse("success", true, businessInfoDto);
    }



}
