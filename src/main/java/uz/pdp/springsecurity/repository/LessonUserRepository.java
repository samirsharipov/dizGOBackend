package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.LessonUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonUserRepository extends JpaRepository<LessonUser, UUID> {
    Optional<LessonUser> findByUserIdAndLessonId(UUID userId, UUID lessonId);

    List<LessonUser> findAllByLessonIdOrderByCreatedAtDesc(UUID lessonId);
    List<LessonUser> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}
