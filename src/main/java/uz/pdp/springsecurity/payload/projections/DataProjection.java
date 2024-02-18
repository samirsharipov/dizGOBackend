package uz.pdp.springsecurity.payload.projections;

import org.springframework.beans.factory.annotation.Value;
import uz.pdp.springsecurity.entity.Branch;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public interface DataProjection {
    @Value(value = "#{target.productname}")
    String getProductName();
    @Value(value = "#{target.branchname}")
    String getBranchName();
    @Value(value = "#{target.createddate}")
    Timestamp getCreatedDate();
    @Value(value = "#{target.treaderquantity}")
    Integer getTreaderQuantity();
    @Value(value = "#{target.kpi}")
    Integer getKpi();
}