package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Form;
import uz.dizgo.erp.entity.Source;
import uz.dizgo.erp.mapper.SourceMapper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.SourceDto;
import uz.dizgo.erp.repository.FormRepository;
import uz.dizgo.erp.repository.SourceRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SourceService {

    private final SourceRepository repository;

    private final SourceMapper mapper;
    private final FormRepository formRepository;

    public ApiResponse getAll(UUID businessId) {
        List<Source> allByBusinessId =
                repository.findAllByBusiness_Id(businessId);

        List<Source> allByBusinessIsNull =
                repository.findAllByBusinessIsNull();

        allByBusinessIsNull.addAll(allByBusinessId);
        List<SourceDto> sourceDtoList = mapper.toDto(allByBusinessIsNull);

        if (sourceDtoList.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        return new ApiResponse("found", true, sourceDtoList);
    }

    public ApiResponse create(SourceDto sourceDto) {
        repository.save(mapper.toEntity(sourceDto));
        return new ApiResponse("successfully saved", true);
    }

    public ApiResponse edit(UUID id, SourceDto sourceDto) {
        Optional<Source> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        Source source = optional.get();
        mapper.update(sourceDto, source);
        repository.save(source);
        return new ApiResponse("successfully updated", true);
    }

    public ApiResponse delete(UUID id) {
        Optional<Source> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        Source source = optional.get();

        if (source.getBusiness() == null) {
            return new ApiResponse("o'chirib bolmaydi", false);
        }
        Optional<Form> sourceId = formRepository.findBySourceId(source.getId());
        if (sourceId.isPresent()) {
            return new ApiResponse("formaga biriktirilgan o'chirib bo'lmaydi!   ",false);
        }

        repository.delete(source);
        return new ApiResponse("success deleted", true);
    }

    public ApiResponse getById(UUID id) {
        Optional<Source> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        Source source = optional.get();
        return new ApiResponse("found", true, mapper.toDto(source));
    }
}
