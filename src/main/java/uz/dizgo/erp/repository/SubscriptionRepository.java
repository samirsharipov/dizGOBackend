package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Subscription;
import uz.dizgo.erp.enums.StatusTariff;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    Optional<Subscription> findByBusinessIdAndActiveTrue(UUID BusinessId);
    Optional<Subscription> findByBusinessIdAndActiveFalse(UUID BusinessId);

    List<Subscription> findAllByDeleteIsFalse();

    List<Subscription> findAllByBusiness_Id(UUID business_id);

    List<Subscription> findAllByBusiness_IdAndDeleteIsFalse(UUID business_id);

    List<Subscription> findAllByCreatedAtAfterAndStatusTariff(Timestamp startTime, StatusTariff statusTariff);

    Integer countAllByStatusTariff(StatusTariff statusTariff);

    List<Subscription> findAllByEndDayBetweenAndDeleteIsFalse(Timestamp endDay, Timestamp endDay2);

    Optional<Subscription> findByStartDayBetweenAndBusinessIdAndDeleteIsFalse(Timestamp endDay, Timestamp endDay2, UUID business_id);

    List<Subscription> findAllByActiveFalseAndDeleteIsFalse();

    List<Subscription> findAllByCheckTestDayIsTrueAndTestDayFinishIsFalse();

}
