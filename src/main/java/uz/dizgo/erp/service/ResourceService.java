package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.entity.Resource;
import uz.dizgo.erp.mapper.ResourceMapper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.ResourceDto;
import uz.dizgo.erp.repository.BranchRepository;
import uz.dizgo.erp.repository.ResourceRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final ResourceMapper mapper;
    private final BranchRepository branchRepository;

    public ApiResponse create(ResourceDto resourceDto) {
        Optional<Branch> optionalBranch = branchRepository.findById(resourceDto.getBranchId());
        if (optionalBranch.isEmpty())
            return new ApiResponse("Branch does not exist");

        Resource resource = mapper.toResource(resourceDto);
        long daysBetween = calculateDaysBetween(resource.getStartDate(), resource.getEndDate());
        double dailyAmount = calculatePercentage(resource.getTotalSum(), resource.getPercentage(), daysBetween);
        resource.setDailyAmount(dailyAmount);

        resourceRepository.save(resource);

        return new ApiResponse("Resource has been created", true);
    }

    public ApiResponse getByBranchId(UUID branchId) {
        List<Resource> all = resourceRepository.findAllByBranchIdAndActiveTrue(branchId);
        if (all.isEmpty())
            return new ApiResponse("Resource not found");

        return new ApiResponse("All Resources found", true, mapper.toResourceDtoList(all));
    }

    public ApiResponse getByBusinessId(UUID businessId) {
        List<Resource> all = resourceRepository.findAllByBranch_BusinessIdAndActiveTrue(businessId);

        if (all.isEmpty())
            return new ApiResponse("Resource not found");

        return new ApiResponse("All Resources found", true, mapper.toResourceDtoList(all));
    }

    public ApiResponse refreshByBranch(UUID branchId) {
        List<Resource> all = resourceRepository.findAllByBranchIdAndActiveTrue(branchId);
        return refreshResponse(all);
    }

    public ApiResponse refreshByBusiness(UUID businessId) {
        List<Resource> all = resourceRepository.findAllByBranch_BusinessIdAndActiveTrue(businessId);
        return refreshResponse(all);
    }

    private @NotNull ApiResponse refreshResponse(List<Resource> all) {
        if (all.isEmpty())
            return new ApiResponse("Resource not found");

        for (Resource resource : all) {
            long daysBetween = calculateDaysBetween(resource.getLastUpdateDate(), new Date());
            double minusSum = resource.getDailyAmount() * daysBetween;
            resource.setTotalSum(resource.getTotalSum() - minusSum);
            resource.setLastUpdateDate(new Date());
            resourceRepository.save(resource);
        }
        return new ApiResponse("Resource has been refreshed",true);
    }

    public static long calculateDaysBetween(Date startDate, Date endDate) {
        // Date obyektlarini LocalDate obyektlariga o'tkazish
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // ChronoUnit.DAYS orqali farqni hisoblash
        return ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
    }

    public static double calculatePercentage(double total, double percentage, long daysBetween) {
        double totalSum = (percentage / 100) * total;
        return totalSum / daysBetween;
    }

}
