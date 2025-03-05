package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Shablon;
import uz.dizgo.erp.mapper.ShablonMapper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.ShablonDto;
import uz.dizgo.erp.repository.ShablonRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShablonService {
    private final ShablonRepository shablonRepository;
    private final ShablonMapper mapper;

    public ApiResponse add(ShablonDto shablonDto) {
        shablonRepository.save(mapper.toEntity(shablonDto));
        return new ApiResponse("successfully saved", true);
    }

    public ApiResponse getAll(UUID businessId) {
        return new ApiResponse("all", true, mapper.toDto(shablonRepository.findAllByBusiness_Id(businessId)));
    }



    public ApiResponse getById(UUID id) {
        Shablon shablon = shablonRepository.findById(id).orElse(null);
        if (shablon == null) {
            return new ApiResponse("not found", false);
        }
        return new ApiResponse("found", true, mapper.toDto(shablon));
    }

    public ApiResponse edit(ShablonDto shablonDto, UUID id) {
        Shablon shablon = shablonRepository.findById(id).orElse(null);
        if (shablon == null) {
            return new ApiResponse("not found", false);
        }
        mapper.update(shablonDto, shablon);
        shablonRepository.save(shablon);
        return new ApiResponse("successfully edited", true);
    }

    public ApiResponse delete(UUID id) {
        Shablon shablon = shablonRepository.findById(id).orElse(null);
        if (shablon == null) {
            return new ApiResponse("not found", false);
        }
        if (shablon.getOriginalName() != null) {
            return new ApiResponse("o'chirib bo'lmaydi", false);
        }
        shablonRepository.delete(shablon);
        return new ApiResponse("o'chirildi", true);
    }


}
