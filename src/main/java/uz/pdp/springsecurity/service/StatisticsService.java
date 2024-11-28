package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BusinessStat;
import uz.pdp.springsecurity.repository.BusinessRepository;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final BusinessRepository businessRepository;

    public ApiResponse businessStatistics() {
        BusinessStat businessStat = getBusinessStat();
        return new ApiResponse("found", true, businessStat);
    }

    public BusinessStat getBusinessStat() {
        long active = businessRepository.countActive();
        long blocked = businessRepository.countBlocked();
        long archived = businessRepository.countArchived();
        long nonActive = businessRepository.countNonActive();

        return new BusinessStat(active, blocked, archived, nonActive);
    }
}