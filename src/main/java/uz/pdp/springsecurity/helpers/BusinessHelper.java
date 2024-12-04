package uz.pdp.springsecurity.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.LidField;
import uz.pdp.springsecurity.entity.PaymentMethod;
import uz.pdp.springsecurity.entity.Source;
import uz.pdp.springsecurity.enums.ValueType;
import uz.pdp.springsecurity.payload.BusinessDto;
import uz.pdp.springsecurity.repository.LidFieldRepository;
import uz.pdp.springsecurity.repository.PayMethodRepository;
import uz.pdp.springsecurity.repository.SourceRepository;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BusinessHelper {
    private final LidFieldRepository lidFieldRepository;
    private final SourceRepository sourceRepository;


    public Business createNewBusiness(BusinessDto businessDto) {
        Business business = new Business();
        business.setName(businessDto.getName());
        business.setBusinessNumber(businessDto.getBusinessNumber());
        business.setActive(businessDto.isActive());
        business.setAddressHome(businessDto.getAddressHome());
        business.setDelete(false);
        return business;
    }

    public void savePaymentMethods(Business business, PayMethodRepository payMethodRepository) {
        List<PaymentMethod> paymentMethods = Arrays.asList(
                new PaymentMethod("NAQD", business),
                new PaymentMethod("PLASTIK KARTA", business),
                new PaymentMethod("BANK ORQALI", business),
                new PaymentMethod("MIJOZ BALANSI", business)
        );
        payMethodRepository.saveAll(paymentMethods);
    }

    public void createStatusAndOther(Business business) {
        createLidField("FIO", ValueType.STRING, business, false);
        createLidField("Phone number", ValueType.INTEGER, business, false);

        List<String> sourceNames = List.of("Telegram", "Facebook", "Instagram", "HandleWrite");
        sourceNames.forEach(name -> createSource(name, business));
    }

    private void createLidField(String name, ValueType valueType, Business business, boolean isTanlangan) {
        LidField lidField = new LidField();
        lidField.setName(name);
        lidField.setBusiness(business);
        lidField.setValueType(valueType);
        lidField.setTanlangan(isTanlangan);
        lidFieldRepository.save(lidField);
    }

    private void createSource(String name, Business business) {
        Source source = new Source();
        source.setName(name);
        source.setBusiness(business);
        sourceRepository.save(source);
    }
}
