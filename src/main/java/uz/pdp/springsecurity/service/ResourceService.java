package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Resource;
import uz.pdp.springsecurity.mapper.ResourceMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ResourceDto;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.ResourceRepository;

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
