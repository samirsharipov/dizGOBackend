package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.springsecurity.entity.Attendance;
import uz.pdp.springsecurity.payload.statistics.AttendanceStat;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    Optional<Attendance> findByEmployeeIdAndCheckInTimeBetweenAndIsArrived(UUID employeeId,
                                                                           Timestamp start,
                                                                           Timestamp end,
                                                                           boolean arrived);


    @Query(value = """
                SELECT 
                    COUNT(CASE WHEN a.is_late = true THEN 1 END) AS late_days_count, -- Kechikib kelgan kunlar soni
                    COALESCE(SUM(CASE WHEN a.is_late = true THEN EXTRACT(EPOCH FROM (a.check_in_time - :expectedStartTime)) / 60 END), 0) AS total_late_minutes, -- Kechikishlarning umumiy vaqti (daqiqa)
                    COUNT(CASE WHEN a.is_left_early = true THEN 1 END) AS early_leave_days_count, -- Erta ketgan kunlar soni
                    COALESCE(SUM(CASE WHEN a.is_left_early = true THEN EXTRACT(EPOCH FROM (:expectedEndTime - a.check_out_time)) / 60 END), 0) AS total_early_leave_minutes, -- Erta ketishlarning umumiy vaqti (daqiqa)
                    COALESCE(SUM(EXTRACT(EPOCH FROM (a.check_out_time - a.check_in_time)) / 3600), 0) AS total_work_hours -- Umumiy ishlagan vaqt (soat)
                FROM attendance a 
                WHERE a.created_at BETWEEN :startDate AND :endDate 
                  AND a.employee_id = :employeeId
            """, nativeQuery = true)
    AttendanceStat getAttendanceStatistics(UUID employeeId,
                                           Timestamp startDate,
                                           Timestamp endDate,
                                           Timestamp expectedStartTime,
                                           Timestamp expectedEndTime);


    Page<Attendance> findAllByEmployeeId(UUID userId, Pageable pageable);
}
