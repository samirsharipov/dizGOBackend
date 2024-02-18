package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Form;
import uz.pdp.springsecurity.entity.LidField;
import uz.pdp.springsecurity.enums.ValueType;
import uz.pdp.springsecurity.mapper.LidFieldMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.LidFieldDto;
import uz.pdp.springsecurity.repository.FormRepository;
import uz.pdp.springsecurity.repository.LidFieldRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LidFieldService {

    private final LidFieldRepository repository;

    private final LidFieldMapper mapper;
    private final FormRepository formRepository;


    public ApiResponse getAll(UUID businessId) {
        List<LidField> allByBusinessId =
                repository.findAllByBusiness_Id(businessId);

        List<LidField> allByBusinessIsNull =
                repository.findAllByBusinessIsNull();

        allByBusinessIsNull.addAll(allByBusinessId);
        List<LidFieldDto> lidFieldDtoList = mapper.toDto(allByBusinessIsNull);

        if (lidFieldDtoList.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        return new ApiResponse("found", true, lidFieldDtoList);
    }

    public ApiResponse create(LidFieldDto lidFieldDto) {
        LidField lidField = mapper.toEntity(lidFieldDto);

        repository.save(lidField);
        return new ApiResponse("successfully saved", true);
    }


    public ApiResponse edit(UUID id, LidFieldDto lidFieldDto) {
        Optional<LidField> optional = repository.findById(id);

        if (optional.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        LidField lidField = optional.get();
        mapper.update(lidFieldDto, lidField);
        repository.save(lidField);

        return new ApiResponse("successfully paySalary", true);
    }


    //todo yana korib chqilsin
    public ApiResponse delete(UUID id) {
        Optional<LidField> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        LidField lidField = optional.get();

        List<Form> byLidFieldsId = formRepository.findAllByLidFieldsId(id);
        if (!byLidFieldsId.isEmpty()) {
            return new ApiResponse("Ushbu lid fieldni o'chirib bo'lmaydi formaga bog'langan", false);
        }

        try {
            repository.delete(lidField);
        } catch (Exception e) {
            return new ApiResponse("o'chirib bo'lmaydi!", false);
        }
        return new ApiResponse("successfully deleted", true);
    }

    public ApiResponse getById(UUID id) {
        Optional<LidField> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        LidField lidField = optional.get();
        return new ApiResponse("found", true, mapper.toDto(lidField));
    }
    public ApiResponse getOneById(UUID businessId) {
        List<LidField> lidFieldList = repository.findAllByBusinessIdAndValueType(businessId, ValueType.SELECT);

        return new ApiResponse("found", true, mapper.toDto(lidFieldList));
    }
}
