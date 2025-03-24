package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Agreement;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.enums.SalaryStatus;
import uz.dizgo.erp.payload.AgreementDto;
import uz.dizgo.erp.payload.AgreementGetDto;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.repository.AgreementRepository;

import java.util.*;

import static uz.dizgo.erp.enums.SalaryStatus.*;

@Service
@RequiredArgsConstructor
public class AgreementService {
    private final AgreementRepository agreementRepository;
    private final MessageService messageService;

    /**
     * Foydalanuvchi uchun standart kelishuvlarni yaratadi va saqlaydi.
     * @param user - kelishuvlar yaratiladigan foydalanuvchi
     */
    public void add(User user) {
        List<Agreement> agreementList = List.of(
                new Agreement(user, HOUR, 0d, false),
                new Agreement(user, DAY, 0d, false),
                new Agreement(user, MONTH, 0d, false),
                new Agreement(user, KPI, 0d, true)
        );
        agreementRepository.saveAll(agreementList);
    }

    /**
     * Foydalanuvchining kelishuvlarini yangilaydi.
     * @param userId - foydalanuvchining ID si
     * @param agreementGetDto - yangilangan kelishuvlar ma'lumotlari
     * @return - operatsiya natijasi bo‘yicha xabar
     */
    public ApiResponse edit(UUID userId, AgreementGetDto agreementGetDto) {
        // Foydalanuvchining kelishuvlar sonini tekshiramiz
        int agreementCount = agreementRepository.countAllByUserId(userId);
        if (agreementCount != SalaryStatus.values().length || agreementCount != agreementGetDto.getAgreementDtoList().size()) {
            return new ApiResponse(messageService.getMessage("invalid.agreement.count"), false);
        }

        // Kelishuvlarni yangilash
        List<Agreement> agreementList = agreementGetDto.getAgreementDtoList().stream()
                .map(dto -> agreementRepository.findByUserIdAndSalaryStatus(userId, SalaryStatus.valueOf(dto.getSalaryStatus()))
                        .map(agreement -> {
                            agreement.setActive(dto.isActive());
                            agreement.setPrice(dto.getPrice());
                            agreement.setStartDate(agreementGetDto.getStartDate());
                            agreement.setEndDate(agreementGetDto.getEndDate());
                            return agreement;
                        }).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        if (agreementList.size() != agreementGetDto.getAgreementDtoList().size()) {
            return new ApiResponse(messageService.getMessage("not.found"), false);
        }

        agreementRepository.saveAll(agreementList);
        return new ApiResponse(messageService.getMessage("success"), true);
    }

    /**
     * Berilgan foydalanuvchining kelishuvlarini olish.
     * @param userId - foydalanuvchining ID si
     * @return - kelishuvlar haqidagi ma'lumotlar
     */
    public ApiResponse getOne(UUID userId) {
        // Barcha kelishuvlarni olish
        List<Agreement> agreementList = agreementRepository.findAllByUserId(userId);
        if (agreementList.size() != SalaryStatus.values().length) {
            return new ApiResponse(messageService.getMessage("error"), false);
        }

        // AgreementGetDto yaratish
        AgreementGetDto agreementGetDto = new AgreementGetDto(
                agreementList.get(0).getStartDate(),
                agreementList.get(0).getEndDate(),
                agreementList.stream()
                        .map(agreement -> new AgreementDto(
                                agreement.getId(),
                                agreement.getSalaryStatus().name(),
                                agreement.getPrice(),
                                agreement.isActive()))
                        .toList()
        );

        return new ApiResponse(messageService.getMessage("success"), true, agreementGetDto);
    }
}
