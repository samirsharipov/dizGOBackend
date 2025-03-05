package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.History;
import uz.dizgo.erp.enums.HistoryName;
import uz.dizgo.erp.mapper.HistoryMapper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.repository.BranchRepository;
import uz.dizgo.erp.repository.BusinessRepository;
import uz.dizgo.erp.repository.HistoryRepository;

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
