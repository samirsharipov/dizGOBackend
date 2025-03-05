package uz.dizgo.erp.payload.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.UUID;

public interface InActiveUserProjection {
    @Value(value = "#{target.id}")
    UUID getId();

    @Value(value = "#{target.name}")
    String getName();

    @Value(value = "#{target.phoneNumber}")
    String getNumber();

    @Value(value = "#{target.payDate}")
    Date getDate();
}
