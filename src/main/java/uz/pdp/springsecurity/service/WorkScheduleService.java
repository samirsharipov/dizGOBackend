package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.WorkSchedule;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.WorkScheduleDto;
import uz.pdp.springsecurity.repository.WorkScheduleRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;

    // CREATE
    public ApiResponse create(WorkScheduleDto workScheduleDto) {
        if (workScheduleRepository.findByUserId(workScheduleDto.getUserId()).isPresent()) {
            return new ApiResponse("Ushbu hodim uchun ish jadvali allaqachon mavjud", false);
        }
        WorkSchedule workSchedule = new WorkSchedule();
        workSchedule.setUserId(workScheduleDto.getUserId());
        workSchedule.setStartTime(workScheduleDto.getStartTime());
        workSchedule.setEndTime(workScheduleDto.getEndTime());
        workSchedule.setBreakStart(workScheduleDto.getBreakStart());
        workSchedule.setBreakEnd(workScheduleDto.getBreakEnd());
        workSchedule.setWorkDays(workScheduleDto.getWorkDays());

        workScheduleRepository.save(workSchedule);
        return new ApiResponse("Ish jadvali muvaffaqiyatli qo'shildi", true);
    }

    // READ (Single schedule)
    public ApiResponse getById(UUID id) {
        Optional<WorkSchedule> optionalWorkSchedule = workScheduleRepository.findById(id);
        return optionalWorkSchedule
                .map(workSchedule -> new ApiResponse("Ish jadvali muvaffaqiyatli topildi", true, toDto(workSchedule)))
                .orElseGet(() -> new ApiResponse("Ish jadvali topilmadi", false));
    }

    public ApiResponse getByUserId(UUID userId) {
        Optional<WorkSchedule> optionalWorkSchedule = workScheduleRepository.findByUserId(userId);
        return optionalWorkSchedule
                .map(workSchedule -> new ApiResponse("Ish jadvali topildi", true, toDto(workSchedule)))
                .orElseGet(() -> new ApiResponse("Ish jadvali topildi", false));
    }

    // UPDATE
    public ApiResponse update(UUID id, WorkScheduleDto workScheduleDto) {
        Optional<WorkSchedule> optionalWorkSchedule = workScheduleRepository.findById(id);
        if (optionalWorkSchedule.isEmpty()) {
            return new ApiResponse("Ish jadvali topilmadi", false);
        }
        WorkSchedule existingSchedule = optionalWorkSchedule.get();
        existingSchedule.setStartTime(workScheduleDto.getStartTime());
        existingSchedule.setEndTime(workScheduleDto.getEndTime());
        existingSchedule.setBreakStart(workScheduleDto.getBreakStart());
        existingSchedule.setBreakEnd(workScheduleDto.getBreakEnd());
        existingSchedule.setWorkDays(workScheduleDto.getWorkDays());
        workScheduleRepository.save(existingSchedule);
        return new ApiResponse("Ish jadvali muvaffaqiyatli yangilandi", true);
    }

    // DELETE
    public ApiResponse delete(UUID id) {
        Optional<WorkSchedule> optionalWorkSchedule = workScheduleRepository.findById(id);
        if (optionalWorkSchedule.isEmpty()) {
            return new ApiResponse("Ish jadvali topilmadi", false);
        }
        workScheduleRepository.deleteById(id);
        return new ApiResponse("Ish jadvali muvaffaqiyatli o'chirildi", true);
    }

    private WorkScheduleDto toDto(WorkSchedule workSchedule) {
        WorkScheduleDto workScheduleDto = new WorkScheduleDto();
        workScheduleDto.setId(workSchedule.getId());
        workScheduleDto.setUserId(workSchedule.getUserId());
        workScheduleDto.setStartTime(workSchedule.getStartTime());
        workScheduleDto.setEndTime(workSchedule.getEndTime());
        workScheduleDto.setBreakStart(workSchedule.getBreakStart());
        workScheduleDto.setBreakEnd(workSchedule.getBreakEnd());
        workScheduleDto.setWorkDays(workSchedule.getWorkDays());
        return workScheduleDto;
    }

}