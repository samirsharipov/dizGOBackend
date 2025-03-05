package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.entity.Currency;
import uz.dizgo.erp.mapper.CurrencyMapper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.CurrencyDto;
import uz.dizgo.erp.repository.BusinessRepository;
import uz.dizgo.erp.repository.CurrencyRepository;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    private final BusinessRepository businessRepository;
    private final CurrencyMapper currencyMapper;
    private final ProductService productService;

    public ApiResponse get(UUID businessId) {
        Optional<Currency> optionalCurrency = currencyRepository.findByBusinessId(businessId);
        if (optionalCurrency.isPresent()){
            return new ApiResponse("SUCCESS", true, currencyMapper.toDto(optionalCurrency.get()));
        }
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if (optionalBusiness.isEmpty())
            return new ApiResponse("BUSINESS NOT FOUND", false);
        create(optionalBusiness.get());
        return get(businessId);
    }

    public ApiResponse edit(UUID businessId, CurrencyDto currencyDto) {
        Optional<Currency> optionalCurrency = currencyRepository.findByBusinessId(businessId);
        if (optionalCurrency.isPresent()){
            Currency currency = optionalCurrency.get();
            currency.setCourse(currencyDto.getCourse());
            currencyRepository.save(currency);
            productService.editPriceAccordingToDollar(businessId, currency.getCourse());
            return new ApiResponse("SUCCESS", true);
        }
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if (optionalBusiness.isEmpty())
            return new ApiResponse("BUSINESS NOT FOUND", false);
        create(optionalBusiness.get());
        return edit(businessId, currencyDto);
    }

    public void create(Business business) {
        Optional<Currency> optionalCurrency = currencyRepository.findFirstByCourseIsNotNullOrderByUpdateAtDesc();
        if (optionalCurrency.isPresent()){
            currencyRepository.save(new Currency(
                    business,
                    optionalCurrency.get().getCourse()
            ));
        }else {
            currencyRepository.save(new Currency(
                    business,
                    11400
            ));
        }
    }
}
