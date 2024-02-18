package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Bonus;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.mapper.BonusMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BonusDto;
import uz.pdp.springsecurity.repository.BonusRepository;
import uz.pdp.springsecurity.repository.BusinessRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BonusService {
    private final BonusRepository bonusRepository;
    private final BusinessRepository businessRepository;
    private final BonusMapper bonusMapper;
    private static final String NOT_FOUND = "BONUS_NOT_FOUND";
    private static final String SUCCESS = "SUCCESS";

    public ApiResponse add(BonusDto bonusDto) {
        Optional<Business> optionalBusiness = businessRepository.findById(bonusDto.getBusinessId());
        if (optionalBusiness.isEmpty()) return new ApiResponse("BUSINESS NOT FOUND", false);
        if (bonusRepository.existsByNameIgnoreCaseAndBusinessIdAndDeleteFalse(bonusDto.getName(), bonusDto.getBusinessId()))
            return new ApiResponse("BONUS NAME EXIST", false);
        Business business = optionalBusiness.get();
        Bonus bonus = new Bonus();
        bonus.setBusiness(business);
        return createEdit(bonus, bonusDto);
    }

    public ApiResponse edit(UUID bonusId, BonusDto bonusDto) {
        Optional<Bonus> optionalBonus = bonusRepository.findByDeleteFalseAndId(bonusId);
        if (optionalBonus.isEmpty()) return new ApiResponse(NOT_FOUND
                , false);
        if (bonusRepository.existsByNameIgnoreCaseAndBusinessIdAndIdIsNotAndDeleteFalse(bonusDto.getName(), bonusDto.getBusinessId(), bonusId))
            return new ApiResponse("BONUS NAME EXIST", false);
        Bonus bonus = optionalBonus.get();
        return createEdit(bonus, bonusDto);
    }

    private ApiResponse createEdit(Bonus bonus, BonusDto bonusDto) {
        bonus.setName(bonusDto.getName());
        bonus.setIcon(bonusDto.getIcon());
        bonus.setColor(bonusDto.getColor());
        bonus.setSumma(bonusDto.getSumma());
        bonusRepository.save(bonus);
        return new ApiResponse(SUCCESS, true);
    }

    public ApiResponse getAll(UUID businessId) {
        if (!businessRepository.existsById(businessId)) return new ApiResponse("BUSINESS NOT FOUND", false);
        List<Bonus> bonusList = bonusRepository.findAllByBusinessIdAndDeleteFalse(businessId);
        if (bonusList.isEmpty()) return new ApiResponse(NOT_FOUND, false);
        return new ApiResponse(true, bonusMapper.toDtoList(bonusList));
    }

    public ApiResponse getOne(UUID bonusId) {
        Optional<Bonus> optionalBonus = bonusRepository.findByDeleteFalseAndId(bonusId);
        return optionalBonus.map(bonus -> new ApiResponse(true, bonusMapper.toDto(bonus))).orElseGet(() -> new ApiResponse(NOT_FOUND, false));
    }

    public ApiResponse delete(UUID bonusId) {
        Optional<Bonus> optionalBonus = bonusRepository.findByDeleteFalseAndId(bonusId);
        if (optionalBonus.isEmpty()) return new ApiResponse(NOT_FOUND, false);
        Bonus bonus = optionalBonus.get();
        bonus.setDelete(true);
        bonusRepository.save(bonus);
        return new ApiResponse(SUCCESS, true);
    }

    public ApiResponse setActive(UUID bonusId) {
        Optional<Bonus> optionalBonus = bonusRepository.findByDeleteFalseAndId(bonusId);
        if (optionalBonus.isEmpty()) return new ApiResponse(NOT_FOUND, false);
        Bonus bonus = optionalBonus.get();
        bonus.setActive(!bonus.isActive());
        bonusRepository.save(bonus);
        return new ApiResponse(SUCCESS, true);
    }
}
