package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.mapper.FormLidHistoryMapper;
import uz.pdp.springsecurity.mapper.LidFieldMapper;
import uz.pdp.springsecurity.mapper.SourceMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.FormDto;
import uz.pdp.springsecurity.payload.FormGetDto;
import uz.pdp.springsecurity.payload.FormLidHistoryDto;
import uz.pdp.springsecurity.repository.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository repository;
    private final LidFieldMapper fieldMapper;
    private final SourceMapper sourceMapper;
    private final LidFieldRepository fieldRepository;
    private final SourceRepository sourceRepository;
    private final FormLidHistoryRepository formLidHistoryRepository;
    private final BusinessRepository businessRepository;
    private final FormLidHistoryMapper formLidHistoryMapper;

    public ApiResponse getAll(UUID businessId) {
        List<Form> allByBusinessId =
                repository.findAllByBusiness_Id(businessId);

        if (allByBusinessId.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        List<FormGetDto> formGetDtoList = new ArrayList<>();

        for (Form form : allByBusinessId) {
            FormGetDto formGetDto = new FormGetDto();
            formGetDto.setSourceDto(sourceMapper.toDto(form.getSource()));
            formGetDto.setLidFieldDtos(fieldMapper.toDto(form.getLidFields()));
            formGetDto.setId(form.getId());
            formGetDto.setBusinessId(form.getBusiness().getId());
            formGetDtoList.add(formGetDto);
        }
        return new ApiResponse("found", true, formGetDtoList);
    }

    public ApiResponse getById(UUID id) {
        Optional<Form> optionalForm = repository.findById(id);
        if (optionalForm.isEmpty()) {
            return new ApiResponse("Not found ", false);
        }

        Form form = optionalForm.get();
        FormGetDto formGetDto = new FormGetDto();
        formGetDto.setId(form.getId());
        formGetDto.setBusinessId(form.getBusiness().getId());
        formGetDto.setLidFieldDtos(fieldMapper.toDto(form.getLidFields()));
        formGetDto.setSourceDto(sourceMapper.toDto(form.getSource()));

        return new ApiResponse("found", true, formGetDto);
    }

    public ApiResponse create(FormDto formDto) {
        List<Form> formList = new ArrayList<>();
        Optional<Business> optionalBusiness = businessRepository.findById(formDto.getBusinessId());

        Optional<Source> optionalHandleWrite = sourceRepository.findByNameAndBusinessId("HandleWrite",formDto.getBusinessId());
        List<UUID> sourceIdList = formDto.getSourceId();
        boolean b = true;
        if (optionalHandleWrite.isPresent()) {
            if (!sourceIdList.isEmpty()) {
                for (UUID uuid : sourceIdList) {
                    if (uuid.equals(optionalHandleWrite.get().getId())) {
                        b = false;
                    }
                }
            }
            if (b) {
                optionalHandleWrite.ifPresent(source -> formDto.getSourceId().add(source.getId()));
            }
        }

        for (UUID uuid : formDto.getSourceId()) {
            Form form = new Form();
            List<LidField> lidFields = new ArrayList<>();
            Optional<Source> optionalSource = sourceRepository.findById(uuid);
            if (optionalSource.isPresent()) {
                Source source = optionalSource.get();
                form.setSource(source);
            }
            for (UUID lidFieldId : formDto.getLidFieldIds()) {
                Optional<LidField> optional = fieldRepository.findById(lidFieldId);
                if (optional.isPresent()) {
                    LidField lidField = optional.get();
                    lidFields.add(lidField);
                }
            }
            optionalBusiness.ifPresent(form::setBusiness);
            form.setLidFields(lidFields);
            formList.add(form);
        }

        List<Form> forms = repository.findAll();
        Optional<FormLidHistory> optional = formLidHistoryRepository.findByActiveIsTrue();
        if (formDto.getTotalSumma() != null) {
            if (optional.isPresent()) {
                FormLidHistory history = optional.get();
                history.setActive(false);
                formLidHistoryRepository.save(history);
            }
            FormLidHistory newHistory = new FormLidHistory();
            Date date = new Date();
            String newDate = new SimpleDateFormat("dd.MM.yyyy HH:ss").format(date);
            newHistory.setTotalSumma(formDto.getTotalSumma());
            newHistory.setName(newDate);
            newHistory.setActive(true);
            optionalBusiness.ifPresent(newHistory::setBusiness);
            formLidHistoryRepository.save(newHistory);
        } else {
            if (optional.isPresent()) {
                FormLidHistory history = optional.get();
                history.setActive(false);
                formLidHistoryRepository.save(history);
            }
        }

        repository.deleteAll(forms);

        if (formList.isEmpty()) {
            return new ApiResponse("not save ", false);
        }


        repository.saveAll(formList);


        return new ApiResponse("successfully saved", true);
    }


    public ApiResponse delete(UUID id) {
        Optional<Form> optionalForm = repository.findById(id);
        if (optionalForm.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        Form form = optionalForm.get();
        repository.delete(form);
        return new ApiResponse("successfully deleted", true);
    }

    public ApiResponse getFormLidHistory(UUID businessId) {
        List<FormLidHistory> all = formLidHistoryRepository.findAllByBusinessIdOrderByCreatedAtAsc(businessId);
        if (all.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        List<FormLidHistoryDto> dto = formLidHistoryMapper.toDto(all);
        return new ApiResponse("found", true, dto);
    }
}
