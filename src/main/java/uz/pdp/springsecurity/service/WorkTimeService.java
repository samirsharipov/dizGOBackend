package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.SalaryStatus;
import uz.pdp.springsecurity.mapper.WorkTimeMapper;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.payload.projections.MonthProjection;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.utils.Constants;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkTimeService {
    private final WorkTimeRepository workTimeRepository;
    private final UserRepository userRepository;
    private final WorkTimeMapper workTimeMapper;
    private final BranchRepository branchRepository;
    private final RoleRepository roleRepository;
    private final AgreementRepository agreementRepository;
    private final SalaryCountService salaryCountService;
    private final WorkTimeLateService workTimeLateService;
    private static final LocalDateTime TODAY_START = LocalDate.now().atStartOfDay();

    public ApiResponse arrive(WorkTimePostDto workTimePostDto) {
        Optional<User> optionalUser = userRepository.findById(workTimePostDto.getUserID());
        if (optionalUser.isEmpty()) return new ApiResponse("USER NO FOUND", false);
        Branch branch = null;
        User user = optionalUser.get();
        for (Branch userBranch : user.getBranches()) {
            if (userBranch.getId().equals(workTimePostDto.getBranchID())) {
                branch = userBranch;
                break;
            }
        }
        if (branch == null) return new ApiResponse("BRANCH NOT FOUND", false);
        if (workTimeRepository.existsByUserIdAndBranchIdAndActiveTrue(user.getId(), branch.getId()))
            return new ApiResponse("USER ON WORK", false);
        WorkTime workTime = workTimeRepository.save(
                new WorkTime(
                        branch,
                        user,
                        new Timestamp(System.currentTimeMillis()),
                        new Timestamp(System.currentTimeMillis()),
                        true
                )
        );

        countSalaryDay(workTime);
        return new ApiResponse("SUCCESS", true);
    }

    private void countSalaryDay(WorkTime workTime) {
        Optional<Agreement> optionalAgreement = agreementRepository.findByUserIdAndSalaryStatusAndActiveTrue(workTime.getUser().getId(), SalaryStatus.DAY);
        if (optionalAgreement.isPresent()) {
            Agreement agreement = optionalAgreement.get();

            int count = workTimeRepository.countAllByUserIdAndBranchIdAndArrivalTimeIsBetween(workTime.getUser().getId(), workTime.getBranch().getId(), Timestamp.valueOf(TODAY_START), Timestamp.valueOf(TODAY_START.plusDays(1)));
            if (count == 1 && agreement.getPrice() > 0) {
                salaryCountService.add(new SalaryCountDto(
                        1,
                        agreement.getPrice(),
                        agreement.getId(),
                        workTime.getBranch().getId(),
                        new Date(),
                        workTime.getArrivalTime() + " kuni"
                ));
            }
        }
    }

    public ApiResponse leave(WorkTimePostDto workTimePostDto) {
        if (!userRepository.existsById(workTimePostDto.getUserID())) return new ApiResponse("USER NO FOUND", false);
        Optional<WorkTime> optionalWorkTime = workTimeRepository.findByUserIdAndBranchIdAndActiveTrue(workTimePostDto.getUserID(), workTimePostDto.getBranchID());
        if (optionalWorkTime.isEmpty()) return new ApiResponse("USER DOES NOT COME", false);
        WorkTime workTime = optionalWorkTime.get();
        workTime.setLeaveTime(new Timestamp(System.currentTimeMillis()));
        long minute = (workTime.getLeaveTime().getTime() - workTime.getArrivalTime().getTime()) / (1000 * 60);
        workTime.setMinute(minute);
        workTime.setDescription(workTimePostDto.getDescription() == null ? "" : workTimePostDto.getDescription());
        workTime.setActive(false);
        // DO NOT TOUCH
        workTimeRepository.save(workTime);
        workTimeLateService.add(workTime);
        countSalaryHour(workTime);
        return new ApiResponse("SUCCESS", true);
    }

    private void countSalaryHour(WorkTime workTime) {
        Optional<Agreement> optionalAgreement = agreementRepository.findByUserIdAndSalaryStatusAndActiveTrue(workTime.getUser().getId(), SalaryStatus.HOUR);
        if (optionalAgreement.isPresent()) {
            Agreement agreement = optionalAgreement.get();
            double hour = (double) (workTime.getMinute() / 6) / 10;
            if (hour > 0 && agreement.getPrice() > 0) {
                salaryCountService.add(new SalaryCountDto(
                        hour,
                        hour * agreement.getPrice(),
                        agreement.getId(),
                        workTime.getBranch().getId(),
                        new Date(),
                        workTime.getArrivalTime() + " kuni " + hour + " soat"
                ));
            }
        }
    }

    public ApiResponse getByUserLastMonth(UUID userId, UUID branchId) {
        if (!userRepository.existsById(userId)) return new ApiResponse("USER NO FOUND", false);
        if (!branchRepository.existsById(branchId)) return new ApiResponse("BRANCH NO FOUND", false);
        List<WorkTime> workTimeList = workTimeRepository.findAllByUserIdAndBranchId(userId, branchId);
        if (workTimeList.isEmpty()) return new ApiResponse("NOT FOUND WORK TIME", false);
        return new ApiResponse(true, workTimeMapper.toDtoList(workTimeList));
    }

    public ApiResponse getOnWork(UUID branchId) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) return new ApiResponse("BRANCH NOT FOUND", false);
        Optional<Role> optionalRole = roleRepository.findByName(Constants.SUPERADMIN);
        if (optionalRole.isEmpty()) return new ApiResponse("ERROR", false);
        List<User> userList = userRepository.findAllByBranchesIdAndRoleIsNotAndActiveIsTrue(branchId, optionalRole.get());
        if (userList.isEmpty()) return new ApiResponse("USERS NOT FOUND", false);
        List<WorkTimeGetDto> workTimeGetDtoList = new ArrayList<>();
        for (User user : userList) {
            Optional<WorkTime> optionalWorkTime = workTimeRepository.findByUserIdAndBranchIdAndActiveTrue(user.getId(), branchId);
            if (optionalWorkTime.isEmpty()) {
                workTimeGetDtoList.add(new WorkTimeGetDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        new Timestamp(System.currentTimeMillis()),
                        false
                ));
            } else {
                WorkTime workTime = optionalWorkTime.get();
                workTimeGetDtoList.add(new WorkTimeGetDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        workTime.getArrivalTime(),
                        workTime.isActive()
                ));
            }
        }

        return new ApiResponse(true, workTimeGetDtoList);
    }

    public ApiResponse getComeWork(UUID branchId, Month selectedMonth, Integer selectedYear) {
        LocalDateTime selectedLocalDateTime = LocalDateTime.of(selectedYear, selectedMonth, 1, 0, 0);
        YearMonth yearMonth = YearMonth.of(selectedYear, selectedMonth);
        int thisDay = LocalDate.of(selectedYear, selectedMonth, yearMonth.lengthOfMonth()).getDayOfMonth();
        if (!branchRepository.existsById(branchId)) return new ApiResponse("BRANCH NOT FOUND", false);
        Optional<Role> optionalRole = roleRepository.findByName(Constants.SUPERADMIN);
        if (optionalRole.isEmpty()) return new ApiResponse("ERROR", false);
        List<User> userList = userRepository.findAllByBranchesIdAndRoleIsNotAndActiveIsTrue(branchId, optionalRole.get());
        if (userList.isEmpty()) return new ApiResponse("USERS NOT FOUND", false);
        List<WorkTimeDayDto> workTimeDayDtoList = new ArrayList<>();
        double minute;
        for (User user : userList) {
            List<WorkTime> workTimeList = new ArrayList<>();
            minute = 0;
            List<TimeStampDto> timestampList = new ArrayList<>();
            List<Timestamp> timestampList2 = new ArrayList<>();
            for (int day = 0; day < thisDay; day++) {
                 workTimeList = workTimeRepository.findAllByUserIdAndBranchIdAndArrivalTimeIsBetweenOrderByCreatedAt(
                        user.getId(),
                        branchId,
                        Timestamp.valueOf(selectedLocalDateTime.plusDays(day)),
                        Timestamp.valueOf(selectedLocalDateTime.plusDays(day + 1))
                );
                if (!workTimeList.isEmpty()) {
                    if (selectedMonth.equals(formatDate(workTimeList.get(0).getArrivalTime()).getMonth()) &&
                            selectedYear.equals(formatDate(workTimeList.get(0).getArrivalTime()).getYear())) {
                        timestampList.add(new TimeStampDto(workTimeList.get(0).getArrivalTime(), workTimeList.get(0).getDescription()));
                        timestampList2.add(workTimeList.get(0).getLeaveTime());
                    }
                    for (WorkTime workTime : workTimeList) {
                        minute += workTime.getMinute();
                    }
                }


            }workTimeDayDtoList.add(new WorkTimeDayDto(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    timestampList,
                    timestampList2,
                    user.getArrivalTime(),
                    Math.floor(minute / 6) / 10
            ));
        }
        return new ApiResponse(true, workTimeDayDtoList);
    }

    public ApiResponse getMonthsByYear(UUID branchId) {
        List<MonthProjection> allMonths = workTimeRepository.getAllMonths(branchId);
        List<MonthProjection> filteredMonths = new ArrayList<>();

        Set<String> uniqueMonthYearSet = new HashSet<>();

        for (MonthProjection monthProjection : allMonths) {
            // Vaqtni olamiz (timestamp)
            Long timestamp = monthProjection.getArrivaltime().getTime();

            // Vaqtni Oy va Yil obyektiga aylantiramiz
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            int monthOfYear = calendar.get(Calendar.MONTH);
            int yearValue = calendar.get(Calendar.YEAR);

            // Oy va yilni birlikda kalit sifatida qo'shamiz
            String monthYearKey = monthOfYear + "-" + yearValue;

            // Bir xil oy va yil kombinatsiyalarini tekshiramiz
            if (!uniqueMonthYearSet.contains(monthYearKey)) {
                filteredMonths.add(monthProjection);
                uniqueMonthYearSet.add(monthYearKey);
            }
        }

        return new ApiResponse(true, filteredMonths);
    }

    private LocalDate formatDate(Timestamp selectedDate) {
        Date date = new Date(selectedDate.getTime());

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public ApiResponse testPreviousAttendances() {
        List<WorkTime> workTimes = workTimeRepository.findAll();
        for (WorkTime workTime : workTimes) {
            if ((!formatDate(workTime.getArrivalTime()).getDayOfWeek().equals(LocalDate.now().getDayOfWeek())) &&
                    workTime.getArrivalTime().equals(workTime.getLeaveTime())) {
                workTime.setBranch(workTime.getBranch());
                workTime.setUser(workTime.getUser());
                workTime.setArrivalTime(workTime.getArrivalTime());
                workTime.setLeaveTime(findTime(workTime));
                workTime.setActive(false);
                workTimeRepository.save(workTime);
            }
        }
        return new ApiResponse(true, true);
    }

    private Timestamp findTime(WorkTime workTime) {
        Timestamp sqlTimestamp = null;
        String timeString = workTime.getUser().getLeaveTime();
        Timestamp timestamp = workTime.getArrivalTime();
        String patternYMD = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormatYMD = new SimpleDateFormat(patternYMD);
        String formattedDate = simpleDateFormatYMD.format(timestamp);

        String dateTimeString = formattedDate + " " + timeString + ":00";
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        try {
            java.util.Date date = simpleDateFormat.parse(dateTimeString);
            long resultTimestamp = date.getTime();
            sqlTimestamp = new Timestamp(resultTimestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlTimestamp;
    }
}
