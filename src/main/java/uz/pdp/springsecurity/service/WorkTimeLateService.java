package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.entity.WorkTime;
import uz.pdp.springsecurity.entity.WorkTimeLate;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.WorkTimeLateDto;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.UserRepository;
import uz.pdp.springsecurity.repository.WorkTimeLateRepository;
import uz.pdp.springsecurity.repository.WorkTimeRepository;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkTimeLateService {
    private final WorkTimeLateRepository workTimeLateRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final WorkTimeRepository workTimeRepository;
    private static final LocalDateTime TODAY_START = LocalDate.now().atStartOfDay();

    @SneakyThrows
    public void add(WorkTime workTime) {
        User user = workTime.getUser();
        Optional<WorkTimeLate> optionalWorkTimeLate = workTimeLateRepository.findByUserIdAndBranchId(user.getId(), workTime.getBranch().getId());
        if (optionalWorkTimeLate.isPresent()){
            List<WorkTime> workTimeList = workTimeRepository.findAllByUserIdAndBranchIdAndArrivalTimeIsBetweenOrderByCreatedAt(
                    user.getId(),
                    workTime.getBranch().getId(),
                    Timestamp.valueOf(TODAY_START),
                    Timestamp.valueOf(TODAY_START.plusDays(1))
            );
            long previousMinute = 0;
            List<WorkTime> workTimeListNew = workTimeList.subList(0, workTimeList.size() - 1);
            for (WorkTime time : workTimeListNew) {
                previousMinute += time.getMinute();
            }
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Time arrivalTime = new Time(format.parse(user.getArrivalTime()).getTime());
            Time leaveTime = new Time(format.parse(user.getLeaveTime()).getTime());
            long minute = (leaveTime.getTime() - arrivalTime.getTime()) / (1000 * 60);
            long previousLateMinute = 0;
            if (previousMinute > 0) {
                previousLateMinute = minute - previousMinute;
            }

            minute = previousMinute + workTime.getMinute() - minute;
            WorkTimeLate workTimeLate = optionalWorkTimeLate.get();
            workTimeLate.setMinute(workTimeLate.getMinute() + previousLateMinute + minute);
            workTimeLateRepository.save(workTimeLate);
        }else {
            create(workTime.getUser(), workTime.getBranch());
            add(workTime);
        }
    }

    private void create(User user, Branch branch) {
        workTimeLateRepository.save(new WorkTimeLate(
                user,
                branch
        ));
    }

    public void clear(User user, Branch branch) {
        Optional<WorkTimeLate> optionalWorkTimeLate = workTimeLateRepository.findByUserIdAndBranchId(user.getId(), branch.getId());
        if (optionalWorkTimeLate.isPresent()){
            WorkTimeLate workTimeLate = optionalWorkTimeLate.get();
            workTimeLate.setMinute(0);
            workTimeLateRepository.save(workTimeLate);
        }else {
            create(user, branch);
        }
    }

    public ApiResponse getByUser(UUID userId, UUID branchId) {
        Optional<WorkTimeLate> optionalWorkTimeLate = workTimeLateRepository.findByUserIdAndBranchId(userId, branchId);
        if (optionalWorkTimeLate.isPresent())
            return new ApiResponse("SUCCESS", true, toDto(optionalWorkTimeLate.get()));

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return new ApiResponse("NOT FOUND USER", false);
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) return new ApiResponse("NOT FOUND BRANCH", false);
        create(optionalUser.get(), optionalBranch.get());
        return getByUser(userId, branchId);
    }

    public ApiResponse getByBranch(UUID branchId) {
        List<WorkTimeLate> workTimeLateList = workTimeLateRepository.findAllByBranchId(branchId);
        if (workTimeLateList.isEmpty()) return new ApiResponse("NOT FOUND LIST", false);
        return new ApiResponse("SUCCESS", true, toDtoList(workTimeLateList));
    }

    private WorkTimeLateDto toDto(WorkTimeLate workTimeLate) {
        int hour = (int) workTimeLate.getMinute() / 60;
        return new WorkTimeLateDto(
                workTimeLate.getUser().getFirstName(),
                workTimeLate.getUser().getLastName(),
                hour
        );
    }

    private List<WorkTimeLateDto> toDtoList(List<WorkTimeLate> workTimeLateList) {
        List<WorkTimeLateDto> workTimeLateDtoList = new ArrayList<>();
        for (WorkTimeLate workTimeLate : workTimeLateList) {
            workTimeLateDtoList.add(toDto(workTimeLate));
        }
        return workTimeLateDtoList;
    }
}
