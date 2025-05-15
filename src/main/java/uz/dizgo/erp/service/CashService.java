package uz.dizgo.erp.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Cash;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.repository.BranchRepository;
import uz.dizgo.erp.repository.CashRepository;

@Service
@RequiredArgsConstructor
public class CashService {

	private final CashRepository cashRepository;
	private final MessageService messageService;

	public ApiResponse addCash(UUID branchId) {

		List<Cash> lastCreatedCash = cashRepository.findAllByBranchId(branchId);

		Cash cash = new Cash();
		cash.setBranchId(branchId);
		cash.setName("Kassa " + (lastCreatedCash.size() + 1));
		cashRepository.save(cash);
		return new ApiResponse(messageService.getMessage("added.successfully"), true);
	}

	public ApiResponse getAllCashes(UUID branchId) {

		List<Cash> cashList = cashRepository.findAllByBranchId(branchId);
		if (cashList.isEmpty()) {
			return new ApiResponse(messageService.getMessage("not.found"), false);
		}
		return new ApiResponse(messageService.getMessage("found"),true, cashList);
	}

}