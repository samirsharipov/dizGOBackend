package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.BalanceHistory;
import uz.pdp.springsecurity.mapper.BalanceMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BalanceHistoryDto;
import uz.pdp.springsecurity.repository.BalanceHistoryRepository;
import uz.pdp.springsecurity.repository.BalanceRepository;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BalanceHistoryService {
    private final BalanceHistoryRepository repository;
    private final BalanceMapper mapper;

    public ApiResponse getById(UUID id) {
        BalanceHistory balanceHistory = repository.findById(id).orElse(null);
        if (balanceHistory == null) {
            return new ApiResponse("not found balance history");
        }
        BalanceHistoryDto balanceHistoryDto = mapper.toDto(balanceHistory);
        return new ApiResponse("found", true, balanceHistoryDto);
    }

    public ApiResponse getByBalanceId(UUID balanceId, int page, int size, Date startDate, Date endDate) {
        if (balanceId == null) {
            return new ApiResponse("branch id is null", false);
        }

        Pageable pageable = PageRequest.of(page, size);
        Map<String, Object> response = new HashMap<>();

        Timestamp startTimestamp = null;
        Timestamp endTimestamp = null;

        Page<BalanceHistory> allBalanceHistory = null;

        if (startDate != null && endDate != null) {
            startTimestamp = new Timestamp(startDate.getTime());
            endTimestamp = new Timestamp(endDate.getTime());
            allBalanceHistory = (repository.findAllByBalance_IdAndCreatedAtBetween(balanceId, startTimestamp, endTimestamp, pageable));
        } else {
            allBalanceHistory = repository.findAllByBalance_Id(balanceId, pageable);
        }

        return createMapPageable(response, allBalanceHistory);
    }

    public ApiResponse getByBranchId(UUID branchId, int page, int size, Date startDate, Date endDate) {
        if (branchId == null) {
            return new ApiResponse("branch id is null", false);
        }

        Pageable pageable = PageRequest.of(page, size);
        Map<String, Object> response = new HashMap<>();

        Timestamp startTimestamp = null;
        Timestamp endTimestamp = null;

        Page<BalanceHistory> allBalanceHistory = null;

        if (startDate != null && endDate != null) {
            startTimestamp = new Timestamp(startDate.getTime());
            endTimestamp = new Timestamp(endDate.getTime());
            allBalanceHistory = (repository.findAllByBalance_Branch_IdAndCreatedAtBetween(branchId, startTimestamp, endTimestamp, pageable));
        } else {
            allBalanceHistory = repository.findAllByBalance_Branch_Id(branchId, pageable);
        }

        return createMapPageable(response, allBalanceHistory);
    }

    private ApiResponse createMapPageable(Map<String, Object> response, Page<BalanceHistory> allBalanceHistory) {
        if (allBalanceHistory == null) {
            return new ApiResponse("not found", false);
        }

        List<BalanceHistoryDto> balanceHistoryDtoList = mapper.toDto(allBalanceHistory.toList());
        balanceHistoryDtoList.sort(Comparator.comparing(BalanceHistoryDto::getDate).reversed());

        response.put("allBalanceHistory", balanceHistoryDtoList);
        response.put("currentPage", allBalanceHistory.getNumber());
        response.put("totalItems", allBalanceHistory.getTotalElements());
        response.put("totalPages", allBalanceHistory.getTotalPages());

        return new ApiResponse("found", true, response);
    }
}
