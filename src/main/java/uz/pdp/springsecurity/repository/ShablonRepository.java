package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Shablon;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShablonRepository extends JpaRepository<Shablon, UUID> {
    List<Shablon> findAllByBusiness_Id(UUID business_id);

    Optional<Shablon> findByOriginalNameAndBusiness_Id(String originalName, UUID business_id);
}