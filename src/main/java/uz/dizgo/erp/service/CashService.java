package uz.dizgo.erp.service;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Cash;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.repository.CashRepository;

@Service
@RequiredArgsConstructor
public class CashService {

	private final CashRepository cashRepository;

	public ApiResponse addCash(UUID branchId) {

		Optional<Cash> lastCreatedCash = cashRepository.findTopByBranchIdOrderByCreatedAtDesc(branchId);

		int cashAsc = 1;

		if (lastCreatedCash.isPresent()) {
			String lastname = lastCreatedCash.get().getName();
			try {
				String parts[] = lastname.split(" ");
				int lastNumber = Integer.parseInt(parts[2]);
				cashAsc = lastNumber + cashAsc;
			} catch (NumberFormatException e) {
				return new ApiResponse("Error occurred while adding new cash " + lastname, false);
			}
		}

		Cash cash = new Cash();
		cash.setBranchId(branchId);
		cash.setName("Cash desk " + cashAsc);
		cashRepository.save(cash);
		return new ApiResponse("Successfully added new cash ", true);
	}

}