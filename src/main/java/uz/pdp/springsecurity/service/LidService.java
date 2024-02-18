package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.NotificationType;
import uz.pdp.springsecurity.mapper.LidMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.LidDto;
import uz.pdp.springsecurity.payload.LidGetDto;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.util.Constants;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LidService {
    private final LidRepository repository;
    private final LidStatusRepository lidStatusRepository;
    private final LidMapper mapper;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final LidFieldRepository lidFieldRepository;
    private final BusinessRepository businessRepository;
    private final FormRepository formRepository;
    private final SourceRepository sourceRepository;
    private final PrizeService prizeService;
    private final FormLidHistoryRepository formLidHistoryRepository;

    public ApiResponse getAll(UUID businessId, int page, int size, UUID sourceId, UUID statusId, Date startDate, Date endDate) {
        Pageable pageable = PageRequest.of(page, size);

        Timestamp startTimestamp = null;
        Timestamp endTimestamp = null;
        Boolean checkingSourceId = null;
        Boolean checkingDate = null;
        Boolean checkingStatus = null;

        if (sourceId != null) {
            checkingSourceId = true;
        }
        if (statusId != null) {
            checkingStatus = true;
        }
        if (startDate != null && endDate != null) {
            startTimestamp = new Timestamp(startDate.getTime());
            endTimestamp = new Timestamp(endDate.getTime());
            checkingDate = true;
        }
        Page<Lid> allLid = null;

        if (Boolean.TRUE.equals(checkingSourceId) && Boolean.TRUE.equals(checkingDate) && Boolean.TRUE.equals(checkingStatus)) {
            allLid = repository.findAllByLidStatusIdAndSourceIdAndCreatedAtBetweenAndDeleteIsFalse(statusId, sourceId, startTimestamp, endTimestamp, pageable);
        } else if (Boolean.TRUE.equals(checkingDate) && Boolean.TRUE.equals(checkingSourceId)) {
            allLid = repository.findAllByBusinessIdAndSourceIdAndCreatedAtBetweenAndDeleteIsFalse(businessId, sourceId, startTimestamp, endTimestamp, pageable);
        } else if (Boolean.TRUE.equals(checkingDate) && Boolean.TRUE.equals(checkingStatus)) {
            allLid = repository.findAllByLidStatusIdAndCreatedAtBetweenAndDeleteIsFalse(statusId, startTimestamp, endTimestamp, pageable);
        } else if (Boolean.TRUE.equals(checkingSourceId) && Boolean.TRUE.equals(checkingStatus)) {
            allLid = repository.findAllByLidStatusIdAndSourceIdAndDeleteIsFalse(statusId, sourceId, pageable);
        } else if (Boolean.TRUE.equals(checkingStatus)) {
            allLid = repository.findAllByLidStatus_IdAndDeleteIsFalse(statusId, pageable);
        } else if (Boolean.TRUE.equals(checkingSourceId)) {
            allLid = repository.findAllByBusinessIdAndSourceIdAndDeleteIsFalse(businessId, sourceId, pageable);
        } else if (Boolean.TRUE.equals(checkingDate)) {
            allLid = repository.findAllByBusinessIdAndCreatedAtBetweenAndDeleteIsFalse(businessId, startTimestamp, endTimestamp, pageable);
        } else {
            allLid = repository.findAllByBusinessIdAndDeleteIsFalse(businessId, pageable);
        }

        List<LidGetDto> dtoList = getDtoList(allLid.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("getLessProduct", dtoList);
        response.put("currentPage", allLid.getNumber());
        response.put("totalItems", allLid.getTotalElements());
        response.put("totalPages", allLid.getTotalPages());

        return new ApiResponse("found", true, response);
    }

    public ApiResponse getById(UUID id) {
        Lid lid = repository.findById(id).orElse(null);
        if (lid == null) {
            return new ApiResponse("not found", false);
        }
        LidGetDto dto = getDto(lid);
        dto.setDescription(lid.getDescription());
        Map<String, String> values = dto.getValues();
        values.put("description", lid.getDescription());
        List<Map<String, String>> valueList = new ArrayList<>();
        valueList.add(values);
        return new ApiResponse("found", true, valueList);
    }

    public ApiResponse create(LidDto lidDto) {
        User admin = userRepository.findByBusinessIdAndRoleName(lidDto.getBusinessId(), Constants.ADMIN).orElse(null);
        List<User> users = userRepository.findAllByBusiness_IdAndRoleName(lidDto.getBusinessId(), Constants.OPERATOR);
        users.add(admin);
        Map<UUID, String> values = lidDto.getValues();

        Map<LidField, String> value = new HashMap<>();
        for (Map.Entry<UUID, String> uuidStringEntry : values.entrySet()) {
            lidFieldRepository.findById(uuidStringEntry.getKey()).ifPresent(lidField -> value.put(lidField, uuidStringEntry.getValue()));
        }
        Optional<LidStatus> optional = lidStatusRepository.findBySortAndBusinessId(1, lidDto.getBusinessId());
        optional.ifPresent(lidStatus -> lidDto.setLidStatusId(lidStatus.getId()));

        Lid lid = mapper.toEntity(lidDto);
        lid.setValues(value);
        Form form = formRepository.findById(lidDto.getFormId()).orElse(null);
        if (form != null) {
            Optional<Source> optionalSource = sourceRepository.findById(form.getSource().getId());
            if (optionalSource.isPresent()) {
                Source source = optionalSource.get();
                lid.setSource(source);
            }
        }
        lid.setDelete(false);
        repository.save(lid);

        for (User user : users) {
            Notification notification = new Notification();
            notification.setRead(false);
            notification.setName("Yangi lid qo'shildi!");
            notification.setMessage("Yangi lid qo'shildi kirib ko'rishingiz statuslarga olib o'tishingiz mumkin!");
            notification.setType(NotificationType.NEW_LID);
            notification.setObjectId(lid.getId());
            notification.setUserTo(user);
            notificationRepository.save(notification);
        }
        Optional<FormLidHistory> historyOptional = formLidHistoryRepository.findByActiveIsTrue();
        if (historyOptional.isPresent()) {
            FormLidHistory history = historyOptional.get();
            history.setTotalLid(history.getTotalLid() + 1);
            history.setAverage(history.getTotalSumma() / history.getTotalLid());
            formLidHistoryRepository.save(history);
        }

        return new ApiResponse("successfully saved", true);
    }

    public ApiResponse editStatus(UUID id, UUID statusId, UUID userId) {
        Lid lid = repository.findById(id).orElse(null);
        if (lid == null) {
            return new ApiResponse("not found lid", false);
        }
        LidStatus lidStatus = lidStatusRepository.findById(statusId).orElse(null);
        if (lidStatus == null) {
            return new ApiResponse("Not found lid status", false);
        }
        if (lid.getLidStatus().getOrginalName() != null) {
            if (lid.getLidStatus().getOrginalName().equals("Done")) {
                return new ApiResponse("You can't change this lid", false);
            }
        }

        if (lidStatus.getOrginalName() != null && lidStatus.getOrginalName().equalsIgnoreCase("Done")) {
            prizeService.addPrizeForLid();
        }

        lid.setLidStatus(lidStatus);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            lid.setUserName(user.getFirstName() + " " + user.getLastName());
        }
        repository.save(lid);
        return new ApiResponse("Successfully edited", true);
    }

    public ApiResponse delete(UUID id) {
        Lid lid = repository.findById(id).orElse(null);
        if (lid == null) {
            return new ApiResponse("not found", false);
        }
        lid.setDelete(true);
        repository.save(lid);
        return new ApiResponse("successfully saved", true);
    }

    private LidGetDto getDto(Lid lid) {
        if (lid == null) {
            return null;
        }

        LidGetDto lidGetDto = new LidGetDto();
        LidStatus lidStatus = lidStatusRepository.findById(lid.getLidStatus().getId()).orElse(null);
        Business business = businessRepository.findById(lid.getBusiness().getId()).orElse(null);

        lidGetDto.setId(lid.getId());
        lidGetDto.setTimestamp(lid.getCreatedAt());
        lidGetDto.setLidStatusId(lidStatus != null ? lidStatus.getId() : null);
        lidGetDto.setLidStatusName(lidStatus != null ? lidStatus.getName() : null);
        lidGetDto.setBusinessId(business != null ? business.getId() : null);
        lidGetDto.setUserName(lid.getUserName() != null ? lid.getUserName() : "");

        Map<LidField, String> lidValues = lid.getValues();
        Map<String, String> values = new HashMap<>();
        for (Map.Entry<LidField, String> entry : lidValues.entrySet()) {
            values.put(entry.getKey().getName(), entry.getValue());
        }
        lidGetDto.setValues(values);
        return lidGetDto;
    }

    public ApiResponse getByBusinessIdPageable(UUID id, Map<String, String> params, UUID sourceId, Date startDate, Date endDate) {

        List<LidStatus> all = lidStatusRepository.findAllByBusiness_IdOrderBySortAsc(id);

        Map<UUID, Integer> value = new HashMap<>();

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    value.put(UUID.fromString(entry.getKey()), Integer.valueOf(entry.getValue()));
                } catch (Exception e) {
                    continue;
                }
            }
        }

        Boolean checkingSourceId = null;
        Boolean checkingDate = null;
        Timestamp startTimestamp = null;
        Timestamp endTimestamp = null;

        if (sourceId != null) {
            checkingSourceId = true;
        }
        if (startDate != null && endDate != null) {
            startTimestamp = new Timestamp(startDate.getTime());
            endTimestamp = new Timestamp(endDate.getTime());
            checkingDate = true;
        }

        List<Map<String, Object>> responses = new ArrayList<>();

        for (LidStatus status : all) {

            Integer integer = null;
            Page<Lid> allLid = null;

            integer = value.get(status.getId());
            Pageable pageable = PageRequest.of(0, Objects.requireNonNullElse(integer, 5));

            if (Boolean.TRUE.equals(checkingSourceId) && Boolean.TRUE.equals(checkingDate)) {
                allLid = repository.findAllByLidStatusIdAndSourceIdAndCreatedAtBetweenAndDeleteIsFalse(status.getId(), sourceId, startTimestamp, endTimestamp, pageable);
            } else if (Boolean.TRUE.equals(checkingDate)) {
                allLid = repository.findAllByLidStatusIdAndCreatedAtBetweenAndDeleteIsFalse(status.getId(), startTimestamp, endTimestamp, pageable);
            } else if (Boolean.TRUE.equals(checkingSourceId)) {
                allLid = repository.findAllByLidStatusIdAndSourceIdAndDeleteIsFalse(status.getId(), sourceId, pageable);
            } else {
                allLid = repository.findAllByLidStatus_IdAndDeleteIsFalse(status.getId(), pageable);
            }

            List<LidGetDto> lidGetDtoList = getDtoList(allLid.toList());
            Collections.reverse(lidGetDtoList);

            Map<String, Object> response = new HashMap<>();
            response.put("statusId", status.getId());
            response.put("getAllLid", lidGetDtoList);
            response.put("currentPage", allLid.getNumber());
            response.put("totalItems", allLid.getTotalElements());
            response.put("totalPages", allLid.getTotalPages());

            responses.add(response);
        }

        return new ApiResponse("found", true, responses);
    }

    private List<LidGetDto> getDtoList(List<Lid> lidList) {
        if (lidList == null) {
            return null;
        }

        List<LidGetDto> list = new ArrayList<LidGetDto>(lidList.size());
        for (Lid lid : lidList) {
            list.add(getDto(lid));
        }
        return list;
    }

    public ApiResponse editDescription(UUID id, String description) {
        Optional<Lid> optionalLid = repository.findById(id);
        if (optionalLid.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        Lid lid = optionalLid.get();
        lid.setDescription(description);
        repository.save(lid);

        return new ApiResponse("successfully edited", true);
    }
}
