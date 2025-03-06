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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QRDataService {

    private final QRDataRepository repository;
    private final BranchRepository branchRepository;
    private final MessageService messageService;
    private final UserRepository userRepository;

    public ApiResponse add(@Valid QRDataDto qrDataDto) {
        QRData qrData = new QRData();
        qrData.setBranchId(qrDataDto.getBranchId());
        qrData.setBranchName(qrDataDto.getBranchName());
        qrData.setUserId(qrDataDto.getUserId());
        qrData.setUserName(qrDataDto.getUserName());
        qrData.setEPosCode(qrDataDto.getEPosCode());
        repository.save(qrData);
        return new ApiResponse("QR ma'lumotlari muvaffaqiyatli qo'shildi", true);
    }

    public ApiResponse edit(Long id, QRDataDto qrDataDto) {
        Optional<QRData> optionalQRData = repository.findById(id);
        if (optionalQRData.isEmpty())
            return new ApiResponse("QR ma'lumotlari topilmadi", false);

        Optional<Branch> optionalBranch = branchRepository.findById(qrDataDto.getBranchId());
        if (optionalBranch.isEmpty())
            return new ApiResponse(messageService.getMessage("not.found"), false);

        QRData qrData = optionalQRData.get();
        Branch branch = optionalBranch.get();

        if (qrDataDto.getUserId() != null) {
            Optional<User> optionalUser = userRepository.findById(qrDataDto.getUserId());
            if (optionalUser.isEmpty())
                return new ApiResponse(messageService.getMessage("not.found"), false);

            User user = optionalUser.get();
            qrData.setUserId(user.getId());
            qrData.setUserName(user.getFirstName() + " " + user.getLastName());
        }

        qrData.setBranchId(branch.getId());
        qrData.setBranchName(branch.getName());
        qrData.setEPosCode(qrDataDto.getEPosCode());
        repository.save(qrData);
        return new ApiResponse("QR ma'lumotlari muvaffaqiyatli yangilandi", true);
    }

    public ApiResponse get(Long id) {
        Optional<QRData> optionalQRData = repository.findById(id);
        return optionalQRData.map(qrData -> new ApiResponse("QR ma'lumotlari", true, qrData))
                .orElseGet(() -> new ApiResponse("QR ma'lumotlari topilmadi", false));
    }

    public ApiResponse getAll() {
        List<QRData> qrDataList = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return new ApiResponse("Barcha QR ma'lumotlari", true, qrDataList);
    }

    public ApiResponse delete(Long id) {
        Optional<QRData> optionalQRData = repository.findById(id);
        if (optionalQRData.isEmpty()) {
            return new ApiResponse("QR ma'lumotlari topilmadi", false);
        }
        repository.deleteById(id);
        return new ApiResponse("QR ma'lumotlari muvaffaqiyatli o'chirildi", true);
    }
}
