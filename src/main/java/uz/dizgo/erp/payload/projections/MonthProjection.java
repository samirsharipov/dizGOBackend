package uz.dizgo.erp.payload.projections;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;

public interface MonthProjection {
    @Value(value = "#{target.arrivaltime}")
    Timestamp getArrivaltime();
}
