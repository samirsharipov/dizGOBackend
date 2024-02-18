package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Tariff;
import uz.pdp.springsecurity.mapper.TariffMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.TariffDto;
import uz.pdp.springsecurity.repository.TariffRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TariffService {
    private final TariffRepository repository;
    private final TariffMapper mapper;

    public ApiResponse getAll() {
        List<Tariff> all = repository.findAll();
        List<Tariff> tariffList = new ArrayList<>();
        for (Tariff tariff : all) {
            if (!tariff.isDelete()) {
                tariffList.add(tariff);
            }
        }
        tariffList.sort(Comparator.comparing(Tariff::getPrice));
        return new ApiResponse("all tariff", true, mapper.toDtoList(tariffList));
    }

    public ApiResponse getToChooseATariff() {
        List<Tariff> all = repository.findAll();
        List<Tariff> tariffList = new ArrayList<>();
        for (Tariff tariff : all) {
            if (!tariff.isDelete() && tariff.isActive()) {
                tariffList.add(tariff);
            }
        }
        tariffList.sort(Comparator.comparing(Tariff::getPrice));
        return new ApiResponse("all tariff", true, mapper.toDtoList(tariffList));
    }

    public ApiResponse getById(UUID id) {
        Optional<Tariff> optionalTariff = repository.findById(id);
        if (optionalTariff.isEmpty()) {
            return new ApiResponse("not found tariff", false);
        }
        Tariff tariff = optionalTariff.get();
        TariffDto dto = mapper.toDto(tariff);
        dto.setPermissionsList(tariff.getPermissions());
        return new ApiResponse("found", true, dto);
    }


    public ApiResponse create(TariffDto tariffDto) {
        Tariff tariff = mapper.toEntity(tariffDto);
        tariff.setPermissions(tariffDto.getPermissionsList());
        repository.save(tariff);
        return new ApiResponse("SUCCESS", true);
    }


    public ApiResponse edit(UUID id, TariffDto tariffDto) {
        Optional<Tariff> optionalTariff = repository.findById(id);
        if (optionalTariff.isEmpty()) {
            return new ApiResponse("not found tariff", false);
        }

        Tariff tariff = optionalTariff.get();
        mapper.update(tariffDto, tariff);
        tariff.setPermissions(tariffDto.getPermissionsList());
        repository.save(tariff);

        return new ApiResponse("successfully edited", true);
    }

    public ApiResponse delete(UUID id) {
        Optional<Tariff> optionalTariff = repository.findById(id);
        if (optionalTariff.isEmpty()) {
            return new ApiResponse("not found tariff", false);
        }

        Tariff tariff = optionalTariff.get();
        tariff.setDelete(true);
        repository.save(tariff);
        return new ApiResponse("successfully deleted", true);
    }
}
