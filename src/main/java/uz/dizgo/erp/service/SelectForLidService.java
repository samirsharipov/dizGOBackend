package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.LidField;
import uz.dizgo.erp.entity.SelectForLid;
import uz.dizgo.erp.mapper.SelectForLidMapper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.SelectForLidDto;
import uz.dizgo.erp.payload.SelectForLidPostDto;
import uz.dizgo.erp.repository.LidFieldRepository;
import uz.dizgo.erp.repository.SelectForLidRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SelectForLidService {
    private final SelectForLidRepository repository;
    private final LidFieldRepository lidFieldRepository;

    private final SelectForLidMapper mapper;

    public ApiResponse getAll(UUID businessId) {
        List<SelectForLid> all = repository.findAllByLidField_BusinessId(businessId);
        if (all.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        List<SelectForLidDto> allDto = mapper.toDto(all);
        return new ApiResponse("found", true, allDto);
    }

    public ApiResponse getById(UUID id) {
        List<SelectForLid> all = repository.findAllByLidFieldId(id);
        if (all == null) {
            return new ApiResponse("not found", false);
        }
        return new ApiResponse("found", true, mapper.toDto(all));
    }

    public ApiResponse create(SelectForLidPostDto lidPostDto) {
        List<String> names = lidPostDto.getNames();
        for (String name : names) {
            repository.save(mapper.toEntity(new SelectForLidDto(name, lidPostDto.getLidFieldId())));
        }
        return new ApiResponse("successfully saved", true);
    }

    public ApiResponse edit(UUID id, SelectForLidPostDto dto) {
        Optional<LidField> optionalLidField =
                lidFieldRepository.findById(id);
        if (optionalLidField.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        List<SelectForLid> all = repository.findAllByLidFieldId(id);
        repository.deleteAll(all);

        return create(dto);
    }

    public ApiResponse delete(UUID id) {
        SelectForLid selectForLid = repository.findById(id).orElse(null);
        if (selectForLid == null) {
            return new ApiResponse("not found", false);
        }
        repository.delete(selectForLid);
        return new ApiResponse("successfully deleted", true);
    }
}
