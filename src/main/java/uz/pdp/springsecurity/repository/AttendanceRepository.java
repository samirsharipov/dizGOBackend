package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Attendance;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    Optional<Attendance> findByEmployeeIdAndCheckInTimeBetweenAndIsArrived(UUID employeeId, Timestamp start, Timestamp end, boolean arrived);
}
