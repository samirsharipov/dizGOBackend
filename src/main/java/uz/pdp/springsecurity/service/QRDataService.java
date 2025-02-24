package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.QRData;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.QRDataDto;
import uz.pdp.springsecurity.repository.QRDataRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QRDataService {

    private final QRDataRepository repository;

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
        if (optionalQRData.isEmpty()) {
            return new ApiResponse("QR ma'lumotlari topilmadi", false);
        }
        QRData qrData = optionalQRData.get();
        qrData.setBranchId(qrDataDto.getBranchId());
        qrData.setBranchName(qrDataDto.getBranchName());
        qrData.setUserId(qrDataDto.getUserId());
        qrData.setUserName(qrDataDto.getUserName());
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
