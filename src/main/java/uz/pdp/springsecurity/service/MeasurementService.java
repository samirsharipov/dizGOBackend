package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.Measurement;
import uz.pdp.springsecurity.entity.MeasurementTranslate;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.BusinessRepository;
import uz.pdp.springsecurity.repository.LanguageRepository;
import uz.pdp.springsecurity.repository.MeasurementRepository;
import uz.pdp.springsecurity.repository.MeasurementTranslateRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final BusinessRepository businessRepository;
    private final MeasurementTranslateRepository measurementTranslateRepository;
    private final LanguageRepository languageRepository;

    public ApiResponse add(MeasurementDto measurementDto) {
        Optional<Business> optionalBusiness = businessRepository.findById(measurementDto.getBusinessId());

        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("BUSINESS NOT FOUND", false);
        }

        Measurement measurement = new Measurement();
        measurement.setName(measurementDto.getName());
        measurement.setValue(measurementDto.getValue());
        measurement.setBusiness(optionalBusiness.get());

        if (measurementDto.getParentId() != null) {
            Optional<Measurement> optionalMeasurement = measurementRepository.findById(measurementDto.getParentId());
            optionalMeasurement.ifPresent(measurement::setParentMeasurement);
        }


        // O'lchovlarni saqlash
        measurementRepository.save(measurement);

        // Tarjimalarni qo'shish
        addTranslations(measurementDto.getTranslations(), measurement);

        return new ApiResponse("ADDED", true);
    }

    @Transactional
    public ApiResponse edit(UUID id, MeasurementDto measurementDto) {
        if (!measurementRepository.existsById(id)) {
            return new ApiResponse("NOT FOUND", false);
        }

        Measurement measurement = measurementRepository.getById(id);
        measurement.setName(measurementDto.getName());
        measurement.setValue(measurementDto.getValue());

        // O'lchovning sub-o'lchovini yangilash
        if (measurementDto.getParentId() != null) {
            Optional<Measurement> optionalSubMeasurement = measurementRepository.findById(measurementDto.getParentId());
            optionalSubMeasurement.ifPresent(measurement::setParentMeasurement);
        }

        // Tarjimalarni yangilash
        updateTranslations(measurementDto.getTranslations(), measurement);

        measurementRepository.save(measurement);
        return new ApiResponse("EDITED", true);
    }

    public ApiResponse get(UUID id, String languageCode) {

        Optional<Measurement> optionalMeasurement = measurementRepository.findById(id);
        if (optionalMeasurement.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }

        Measurement measurement = optionalMeasurement.get();

        Optional<MeasurementTranslate> optionalMeasurementTranslate =
                measurementTranslateRepository.findByMeasurement_idAndLanguage_Code(measurement.getId(), languageCode);


        return new ApiResponse("FOUND", true, toGetDto(optionalMeasurementTranslate, measurement));
    }

    @NotNull
    private static MeasurementGetDto toGetDto(Optional<MeasurementTranslate> optionalMeasurementTranslate, Measurement measurement) {
        MeasurementGetDto measurementGetDto = new MeasurementGetDto();
        if (optionalMeasurementTranslate.isPresent()) {

            MeasurementTranslate measurementTranslate = optionalMeasurementTranslate.get();

            measurementGetDto.setId(measurement.getId());
            measurementGetDto.setValue(measurement.getValue());
            measurementGetDto.setBusinessId(measurement.getBusiness().getId());

            measurementGetDto.setName(measurementTranslate.getName());
            measurementGetDto.setDescription(measurementTranslate.getDescription());

        } else {
            measurementGetDto.setId(measurement.getId());
            measurementGetDto.setValue(measurement.getValue());
            measurementGetDto.setBusinessId(measurement.getBusiness().getId());
            measurementGetDto.setName(measurement.getName());
        }
        if (measurement.getParentMeasurement() != null) {
            measurementGetDto.setParentMeasurementId(measurement.getParentMeasurement().getId());
            measurementGetDto.setParentMeasurementName(measurement.getParentMeasurement().getName());
        }
        return measurementGetDto;
    }

    public ApiResponse delete(UUID id) {

        Optional<Measurement> optionalMeasurement = measurementRepository.findById(id);
        if (optionalMeasurement.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }
        Measurement measurement = optionalMeasurement.get();
        measurement.setDeleted(true);
        measurement.setActive(false);
        measurementRepository.save(measurement);

        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getByBusiness(UUID businessId, String languageCode) {
        List<Measurement> measurements = measurementRepository.findAllByBusiness_Id(businessId);
        if (measurements.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }

        List<MeasurementGetDto> getDtoList = new ArrayList<>();

        for (Measurement measurement : measurements) {
            Optional<MeasurementTranslate> optionalMeasurementTranslate =
                    measurementTranslateRepository.findByMeasurement_idAndLanguage_Code(measurement.getId(), languageCode);
            getDtoList.add(toGetDto(optionalMeasurementTranslate, measurement));
        }


        return new ApiResponse("FOUND", true, getDtoList);
    }

    private void addTranslations(List<MeasurementTranslateDto> translations, Measurement measurement) {
        if (translations != null) {
            for (MeasurementTranslateDto translation : translations) {
                MeasurementTranslate measurementTranslate = new MeasurementTranslate();
                measurementTranslate.setLanguage(languageRepository.getById(translation.getLanguageId()));
                measurementTranslate.setName(translation.getName());
                measurementTranslate.setDescription(translation.getDescription());
                measurementTranslate.setMeasurement(measurement);
                measurementTranslateRepository.save(measurementTranslate);
            }
        }
    }

    @Transactional
    public void updateTranslations(List<MeasurementTranslateDto> translations, Measurement measurement) {
        // Eski tarjimalarni o'chirish
        measurementTranslateRepository.deleteByMeasurement_id(measurement.getId());

        // Yangi tarjimalarni qo'shish
        if (translations != null && !translations.isEmpty()) {
            addTranslations(translations, measurement);
        }
    }

    public ApiResponse getAllTranslate(UUID measurementId) {
        List<MeasurementTranslate> all = measurementTranslateRepository.findByMeasurement_id(measurementId);
        if (all.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }
        List<MeasurementTranslateGetDto> getDtoList = new ArrayList<>();
        for (MeasurementTranslate measurementTranslate : all) {
            MeasurementTranslateGetDto getDto = new MeasurementTranslateGetDto();
            getDto.setId(measurementTranslate.getId());
            getDto.setName(measurementTranslate.getName());
            getDto.setDescription(measurementTranslate.getDescription());
            getDto.setLanguageId(measurementTranslate.getLanguage().getId());
            getDto.setCode(measurementTranslate.getLanguage().getCode());
            getDtoList.add(getDto);
        }
        return new ApiResponse("FOUND", true, getDtoList);
    }
}