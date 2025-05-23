package uz.dizgo.erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.entity.Tax;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.TaxDto;
import uz.dizgo.erp.repository.BusinessRepository;
import uz.dizgo.erp.repository.TaxRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaxService {

    @Autowired
    TaxRepository taxRepository;

    @Autowired
    BusinessRepository businessRepository;

    public ApiResponse add(TaxDto taxDto){
        Optional<Business> optionalBusiness = businessRepository.findById(taxDto.getBusinessId());
        if (optionalBusiness.isEmpty()){
            return new ApiResponse("BUSINESS NOT FOUND",false);
        }
        Tax tax=new Tax(
                taxDto.getName(),
                taxDto.getPercent(),
                taxDto.getActive(),
                optionalBusiness.get()
        );
        tax = taxRepository.save(tax);
        return new ApiResponse("ADDED",true, tax);
    }

    public ApiResponse edit(UUID id, TaxDto taxDto){
        if (!taxRepository.existsById(id))
            return new ApiResponse("NOT FOUND",false);
        boolean existsByBusinessId = taxRepository.existsByBusinessId(taxDto.getBusinessId());
        if (!existsByBusinessId){
            return new ApiResponse("BRANCH NOT FOUND",false);
        }
        Tax tax = taxRepository.getById(id);
        tax.setName(taxDto.getName());
        tax.setPercent(taxDto.getPercent());

        taxRepository.save(tax);
        return new ApiResponse("EDITED",true);
    }

    public ApiResponse get(UUID id) {
        if (!taxRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, taxRepository.findById(id).get());
    }

    public ApiResponse delete(UUID id) {
        if (!taxRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);
        taxRepository.deleteById(id);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getAllByBusinessId(UUID businessId) {
        List<Tax> allByBusinessId = taxRepository.findAllByBusiness_Id(businessId);
        if (allByBusinessId.isEmpty()) return new ApiResponse("NOT FOUND",false);
        return new ApiResponse("FOUND",true,allByBusinessId);
    }
}
