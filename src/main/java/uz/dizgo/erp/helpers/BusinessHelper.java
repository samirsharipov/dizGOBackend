package uz.dizgo.erp.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.entity.LidField;
import uz.dizgo.erp.entity.Source;
import uz.dizgo.erp.enums.ValueType;
import uz.dizgo.erp.payload.BusinessDto;
import uz.dizgo.erp.repository.LidFieldRepository;
import uz.dizgo.erp.repository.SourceRepository;

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
        business.setContractStartDate(businessDto.getContractStartDate());
        business.setContractEndDate(businessDto.getContractEndDate());
        business.setDeleted(false);
        business.setActive(false);
        return business;
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
