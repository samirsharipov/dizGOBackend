package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.entity.QRData;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.QRDataDto;
import uz.dizgo.erp.repository.BranchRepository;
import uz.dizgo.erp.repository.QRDataRepository;
import uz.dizgo.erp.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QRDataService {

    private final QRDataRepository repository;
    private final BranchRepository branchRepository;
    private final MessageService messageService;
    private final UserRepository userRepository;

    public ApiResponse add(@Valid QRDataDto dto) {
        if (dto != null) {
            repository.save(mapToEntity(new QRData(), dto));
        } else {
            repository.save(new QRData());
        }
        return new ApiResponse("QR ma'lumotlari muvaffaqiyatli qo'shildi", true);
    }

    public ApiResponse edit(Long id, QRDataDto qrDataDto) {
        return repository.findById(id)
                .map(qrData -> {
                    if (!branchRepository.existsById(qrDataDto.getBranchId()))
                        return new ApiResponse(messageService.getMessage("not.found"), false);

                    if (qrDataDto.getUserId() != null && !userRepository.existsById(qrDataDto.getUserId()))
                        return new ApiResponse(messageService.getMessage("not.found"), false);

                    repository.save(mapToEntity(qrData, qrDataDto));
                    return new ApiResponse("QR ma'lumotlari muvaffaqiyatli yangilandi", true);
                })
                .orElseGet(() -> new ApiResponse("QR ma'lumotlari topilmadi", false));
    }

    public ApiResponse get(Long id) {
        return repository.findById(id)
                .map(qrData -> new ApiResponse("QR ma'lumotlari", true, qrData))
                .orElseGet(() -> new ApiResponse("QR ma'lumotlari topilmadi", false));
    }

    public ApiResponse getAll() {
        List<QRData> qrDataList = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return new ApiResponse("Barcha QR ma'lumotlari", true, qrDataList);
    }

    public ApiResponse delete(Long id) {
        if (!repository.existsById(id))
            return new ApiResponse("QR ma'lumotlari topilmadi", false);

        repository.deleteById(id);
        return new ApiResponse("QR ma'lumotlari muvaffaqiyatli o'chirildi", true);
    }

    public ApiResponse getByBranch(UUID id) {
        List<QRData> all = repository.findAllByBranchId(id);
        return all.isEmpty()
                ? new ApiResponse(messageService.getMessage("not.found"), false)
                : new ApiResponse(messageService.getMessage("found"), true, all);
    }

    private QRData mapToEntity(QRData qrData, QRDataDto dto) {
        qrData.setBranchId(dto.getBranchId());
        qrData.setBranchName(branchRepository.findById(dto.getBranchId()).map(Branch::getName).orElse(null));
        qrData.setEPosCode(dto.getEPosCode());

        if (dto.getUserId() != null) {
            userRepository.findById(dto.getUserId()).ifPresent(user -> {
                qrData.setUserId(user.getId());
                qrData.setUserName(user.getFirstName() + " " + user.getLastName());
            });
        }
        return qrData;
    }
}
