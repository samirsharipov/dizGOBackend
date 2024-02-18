package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Shablon;
import uz.pdp.springsecurity.mapper.ShablonMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ShablonDto;
import uz.pdp.springsecurity.repository.ShablonRepository;

import java.util.List;
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
