package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Attendance;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Location;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.AttendanceGetDto;
import uz.pdp.springsecurity.payload.EmployeeWorkDurationDto;
import uz.pdp.springsecurity.repository.AttendanceRepository;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.LocationRepository;
import uz.pdp.springsecurity.repository.UserRepository;
import uz.pdp.springsecurity.service.functions.GeoCheck;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final LocationRepository locationRepository;
    private final GeoCheck geoCheck;
    private final BranchRepository branchRepository;
    private final MessageService messageService;

    // QR kod orqali keldi-ketdi tasdiqlash
    public ApiResponse checkInWithQRCode(UUID branchId, UUID employeeId, String qrCodeData, boolean input) {
        // QR koddagi ma'lumotlarni tahlil qilamiz
        String[] qrParts = qrCodeData.split(":");
        if (qrParts.length != 4) {
            return new ApiResponse("QR kod noto'g'ri formatda", false);
        }

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch topilmadi", false);
        }


        UUID qrEmployeeId = UUID.fromString(qrParts[0]);
        long timestampLong = Long.parseLong(qrParts[1]);
        Timestamp qrTime = new Timestamp(timestampLong);
        double qrLatitude = Double.parseDouble(qrParts[2]);
        double qrLongitude = Double.parseDouble(qrParts[3]);

        if (!qrEmployeeId.equals(employeeId)) {
            return new ApiResponse("QR kod va xodim ID mos emas", false);
        }

        // QR time va hozirgi vaqt orasidagi farqni hisoblash
        long diffInMillies = System.currentTimeMillis() - qrTime.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillies);

        if (diffInMinutes > 2) {
            return new ApiResponse("QR kodni yuborish uchun 2 daqiqa vaqt o'tgan", false);
        }

        // Bugungi sana
        Timestamp todayStart = Timestamp.valueOf(Timestamp.valueOf(new Timestamp(System.currentTimeMillis()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0)).toLocalDateTime());
        Timestamp todayEnd = Timestamp.valueOf(Timestamp.valueOf(new Timestamp(System.currentTimeMillis()).toLocalDateTime().withHour(23).withMinute(59).withSecond(59)).toLocalDateTime());

        Optional<Location> locationOpt = locationRepository.findByBranchId(branchId);
        if (locationOpt.isEmpty()) {
            return new ApiResponse("Ishxonangiz joylashuvi topilmadi", false);
        }

        Location location = locationOpt.get();
        double radius = location.getRadius();

        double distance = geoCheck.calculateDistance(qrLatitude, qrLongitude, location.getLatitude(), location.getLongitude());


        if (input) {
            // KIRISH (input = true)
            Optional<Attendance> existingAttendance = attendanceRepository.findByEmployeeIdAndCheckInTimeBetweenAndIsArrived(
                    employeeId, todayStart, todayEnd, true
            );

            if (existingAttendance.isPresent()) {
                return new ApiResponse("Siz allaqachon ishga keldingiz", false);
            }


            if (geoCheck.checkDistance(distance, radius)) {
                Attendance attendance = new Attendance();
                attendance.setEmployeeId(employeeId);
                attendance.setCheckInTime(new Timestamp(System.currentTimeMillis()));
                attendance.setIsArrived(true);
                attendance.setIncomeDistance(distance);
                attendance.setBranchId(branchId);
                attendanceRepository.save(attendance);
                return new ApiResponse("Ishga kelish vaqti tasdiqlandi", true);
            } else {
                return new ApiResponse("Ofis lokatsiyasi sizning geo lokatsiyangiz bilan mos kelmadi", false);
            }
        } else {
            // CHIQISH (input = false)
            Optional<Attendance> existingAttendance = attendanceRepository.findByEmployeeIdAndCheckInTimeBetweenAndIsArrived(
                    employeeId, todayStart, todayEnd, true
            );

            if (existingAttendance.isPresent()) {
                Attendance attendance = existingAttendance.get();
                attendance.setCheckOutTime(new Timestamp(System.currentTimeMillis()));
                attendance.setIsArrived(false);
                attendance.setOutcomeDistance(distance);
                attendanceRepository.save(attendance);
                return new ApiResponse("Ishdan chiqish vaqti tasdiqlandi", true);
            } else {
                return new ApiResponse("Siz hali bugun ishga kelmagansiz", false);
            }
        }
    }

    public ApiResponse getUserId(UUID userId, int size, int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Attendance> all = attendanceRepository.findAllByEmployeeId(userId, pageable);

        if (all.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        List<AttendanceGetDto> attendanceGetDtoList = new ArrayList<>();
        List<Attendance> list = all.stream().toList();

        getDto(list, attendanceGetDtoList);

        Map<String, Object> response = new HashMap<>();
        response.put("data", attendanceGetDtoList);
        response.put("totalElements", all.getTotalElements());
        response.put("totalPages", all.getTotalPages());

        return new ApiResponse("found", true, response);
    }

    private void getDto(List<Attendance> list, List<AttendanceGetDto> attendanceGetDtoList) {
        for (Attendance attendance : list) {
            AttendanceGetDto attendanceGetDto = new AttendanceGetDto();
            attendanceGetDto.setCheckInTime(attendance.getCheckInTime() != null ? attendance.getCheckInTime() : null);
            attendanceGetDto.setCheckOutTime(attendance.getCheckOutTime() != null ? attendance.getCheckOutTime() : null);
            attendanceGetDto.setArrived(attendance.getIsArrived());
            attendanceGetDto.setWorkDuration(attendance.getWorkDuration());
            if (attendance.getBranchId() != null) {
                Optional<Branch> optionalBranch = branchRepository.findById(attendance.getBranchId());
                optionalBranch.ifPresent(branch -> {
                    attendanceGetDto.setBranchName(branch.getName());
                });
            }
            attendanceGetDtoList.add(attendanceGetDto);
        }
    }

    public ApiResponse getByBusinessId(UUID businessId, UUID branchId, Timestamp startDate, Timestamp endDate) {
        List<EmployeeWorkDurationDto> totalWorkDurationList = (branchId != null)
                ? attendanceRepository.findTotalWorkDurationByBranch(branchId, startDate, endDate)
                : attendanceRepository.findTotalWorkDurationByBusiness(businessId, startDate, endDate);

        if (totalWorkDurationList.isEmpty()) {
            return new ApiResponse(messageService.getMessage("not.found"), false);
        }

        // TotalWorkDuration null bo'lganlarni topamiz
        Map<UUID, Long> durationMap = attendanceRepository.findAllById(
                totalWorkDurationList.stream()
                        .filter(dto -> dto.getTotalWorkDuration() == null)
                        .map(EmployeeWorkDurationDto::getId)
                        .toList()
        ).stream().collect(Collectors.toMap(Attendance::getId, Attendance::getDuration));

        // Faqat null bo'lganlarga duration qo‘shamiz
        totalWorkDurationList.forEach(dto ->
                dto.setTotalWorkDuration(dto.getTotalWorkDuration() != null
                        ? dto.getTotalWorkDuration()
                        : durationMap.get(dto.getId()))
        );

        // ID bo‘yicha yagona EmployeeWorkDurationDto yaratish (dublikatsiyani oldini olish)
        List<EmployeeWorkDurationDto> optimizedList = totalWorkDurationList.stream()
                .collect(Collectors.toMap(
                        EmployeeWorkDurationDto::getId,
                        dto -> dto,
                        (existing, newDto) -> {
                            existing.setTotalWorkDuration(
                                    (existing.getTotalWorkDuration() == null ? 0 : existing.getTotalWorkDuration()) +
                                            (newDto.getTotalWorkDuration() == null ? 0 : newDto.getTotalWorkDuration())
                            );
                            return existing;
                        }
                )).values().stream().toList();

        return new ApiResponse(messageService.getMessage("found"), true, optimizedList);
    }

    public ApiResponse getUserIdDiagram(UUID userId, Timestamp startDate, Timestamp endDate) {
        List<Attendance> all =
                attendanceRepository.findAllByEmployeeIdAndCreatedAtBetween(userId, startDate, endDate);
        if (all.isEmpty())
            return new ApiResponse(messageService.getMessage("not.found"), false);

        List<AttendanceGetDto> attendanceGetDtoList = new ArrayList<>();
        getDto(all, attendanceGetDtoList);

        return new ApiResponse(messageService.getMessage("found"), true, attendanceGetDtoList);
    }
}
