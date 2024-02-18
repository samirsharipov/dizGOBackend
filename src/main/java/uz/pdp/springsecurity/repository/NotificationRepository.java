package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.springsecurity.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findAllByReadIsFalseAndUserToId(UUID userToId, Pageable pageable);
    Page<Notification> findAllByReadIsTrueAndUserToId(UUID userToId, Pageable pageable);
//    @Query(nativeQuery = true, value = "SELECT * from notification n where n.read = true and user_to_id = :userId")
    List<Notification> findAllByUserToIdAndReadIsTrue(UUID userToId);
}
