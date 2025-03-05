package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.entity.Navigation;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.NavigationDto;
import uz.dizgo.erp.repository.BranchRepository;
import uz.dizgo.erp.repository.NavigationProcessRepository;
import uz.dizgo.erp.repository.NavigationRepository;
import uz.dizgo.erp.repository.TradeRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NavigationService {
    private final NavigationProcessRepository navigationProcessRepository;
    private final NavigationRepository navigationRepository;
    private final BranchRepository branchRepository;
    private final NavigationProcessService navigationProcessService;
    private final TradeRepository tradeRepository;

    private static final LocalDateTime TODAY = LocalDateTime.now();

    public ApiResponse add(NavigationDto navigationDto) {
        Optional<Branch> optionalBranch = branchRepository.findById(navigationDto.getBranchId());
        if (optionalBranch.isEmpty())
            return new ApiResponse("BRANCH NOT FOUND", false);
        Branch branch = optionalBranch.get();
        Timestamp createdAt = branch.getBusiness().getCreatedAt();
        if (navigationRepository.existsByBranchId(navigationDto.getBranchId()))
            return new ApiResponse("AKTIV REJA MAVJUD", false);
        Navigation navigation = navigationRepository.save(new Navigation(
                branch,
                navigationDto.getInitial(),
                navigationDto.getGoal(),
                Timestamp.valueOf(TODAY.minusDays(10)),
//                new Date(),
                navigationDto.getEndDate(),
                navigationDto.getDescription()
        ));
        navigationProcessService.createGoal(navigation);
        return new ApiResponse("SUCCESS", true);
    }

    public ApiResponse getOne(UUID branchId) {
        Optional<Navigation> optionalNavigation = navigationRepository.findByBranchId(branchId);
        if (optionalNavigation.isPresent()) {
            Navigation navigation = optionalNavigation.get();
            return new ApiResponse("SUCCESS", true, new NavigationDto(
                    branchId,
                    navigation.getInitial(),
                    navigation.getGoal(),
                    navigation.getStartDate(),
                    navigation.getEndDate(),
                    navigation.getDescription()
            ));
        }
        if (!branchRepository.existsById(branchId))
            return new ApiResponse("BRANCH NOT FOUND", false);
        return new ApiResponse("NAVIGATION NOT FOUND", false);
    }

    public ApiResponse getAverageSell(UUID branchId) {
        if (!branchRepository.existsById(branchId))
            return new ApiResponse("BRANCH NOT FOUND", false);

        Double totalSum = tradeRepository.totalSumByCreatedAtBetweenAndBranchId(Timestamp.valueOf(TODAY.minusDays(30)), Timestamp.valueOf(TODAY), branchId);
        double totalSell = totalSum == null? 0: totalSum;
        long averageTrade = Math.round(totalSell / ( 30 * 100)) * 100;
        return new ApiResponse("SUCCESS", true, averageTrade);
    }

    @Transactional
    public ApiResponse delete(UUID branchId) {
        if (!branchRepository.existsById(branchId))
            return new ApiResponse("BRANCH NOT FOUND", false);
        navigationRepository.deleteByBranchId(branchId);
        navigationProcessRepository.deleteByBranchId(branchId);
        return new ApiResponse("SUCCESS", true);
    }
}
