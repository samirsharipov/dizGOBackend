package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Course;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
}
