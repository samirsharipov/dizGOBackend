package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Test;

import java.util.List;
import java.util.UUID;

public interface TestRepository extends JpaRepository<Test, UUID> {
    List<Test> findAllByLessonId(UUID lessonId);
    boolean existsAllByLessonId(UUID lessonId);
    void deleteAllByLessonId(UUID lessonId);
}
