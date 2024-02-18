package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.History;
import uz.pdp.springsecurity.enums.HistoryName;
import uz.pdp.springsecurity.mapper.HistoryMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.BusinessRepository;
import uz.pdp.springsecurity.repository.HistoryRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final BusinessRepository businessRepository;
    private final BranchRepository branchRepository;
    private final HistoryMapper historyMapper;

    public ApiResponse get(UUID id, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<History> historyPage;
        if (name != null)
            if (businessRepository.existsById(id))
                historyPage = historyRepository.findAllByBranch_BusinessIdAndName(id, HistoryName.valueOf(name), pageable);
            else if (branchRepository.existsById(id))
                historyPage = historyRepository.findAllByBranchIdAndName(id, HistoryName.valueOf(name), pageable);
            else
                historyPage = historyRepository.findAllByUserIdAndName(id, HistoryName.valueOf(name), pageable);
        else
            historyPage = historyRepository.findAllByBranch_BusinessIdOrBranchIdOrUserId(id, id, id, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("getLessProduct", historyMapper.toDtoList(historyPage.getContent()));
        response.put("currentPage", historyPage.getNumber());
        response.put("totalPage", historyPage.getTotalPages());
        response.put("totalItem", historyPage.getTotalElements());
        return new ApiResponse(true, response);
    }
}
