package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.Attendance;
import uz.pdp.springsecurity.payload.AttendanceGetDto;
import uz.pdp.springsecurity.payload.EmployeeWorkDurationDto;
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
        COUNT(CASE WHEN a.is_late = true THEN 1 END) AS lateDaysCount, 
        COALESCE(SUM(CASE WHEN a.is_late = true THEN EXTRACT(EPOCH FROM (a.check_in_time - :expectedStartTime)) / 60 END), 0) AS totalLateMinutes, 
        COUNT(CASE WHEN a.is_left_early = true THEN 1 END) AS earlyLeaveDaysCount, 
        COALESCE(SUM(CASE WHEN a.is_left_early = true THEN EXTRACT(EPOCH FROM (:expectedEndTime - a.check_out_time)) / 60 END), 0) AS totalEarlyLeaveMinutes, 
        COALESCE(SUM(EXTRACT(EPOCH FROM (a.check_out_time - a.check_in_time)) / 3600), 0) AS totalWorkHours 
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

    @Query(value = "SELECT e.id, CONCAT(e.first_name, ' ', e.last_name) AS full_name, " +
            "SUM(COALESCE(a.work_duration, EXTRACT(EPOCH FROM (COALESCE(a.check_out_time, now()) - a.check_in_time)))) AS total_work_duration " +
            "FROM attendance a " +
            "JOIN users e ON a.employee_id = e.id " +
            "WHERE a.branch_id = :branchId AND a.created_at BETWEEN :start AND :end " +
            "GROUP BY e.id, e.first_name, e.last_name",
            nativeQuery = true)
    List<EmployeeWorkDurationDto> findTotalWorkDurationByBranch(UUID branchId, Timestamp start, Timestamp end);

    // Business bo‘yicha hodimlarning umumiy ishlagan vaqtini hisoblash
    @Query(value = "SELECT e.id, CONCAT(e.first_name, ' ', e.last_name) AS full_name, " +
            "SUM(COALESCE(a.work_duration, EXTRACT(EPOCH FROM (COALESCE(a.check_out_time, now()) - a.check_in_time)))) AS total_work_duration " +
            "FROM attendance a " +
            "JOIN users e ON a.employee_id = e.id " +
            "WHERE e.business_id = :businessId AND a.created_at BETWEEN :start AND :end " +
            "GROUP BY e.id, e.first_name, e.last_name",
            nativeQuery = true)
    List<EmployeeWorkDurationDto> findTotalWorkDurationByBusiness(UUID businessId, Timestamp start, Timestamp end);


    List<Attendance> findAllByEmployeeIdAndCreatedAtBetween(UUID employeeId, Timestamp createdAtAfter, Timestamp createdAtBefore);


}
