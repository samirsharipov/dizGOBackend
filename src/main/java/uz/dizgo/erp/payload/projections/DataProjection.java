package uz.dizgo.erp.payload.projections;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;

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