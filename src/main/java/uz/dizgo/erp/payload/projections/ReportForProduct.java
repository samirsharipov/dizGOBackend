package uz.dizgo.erp.payload.projections;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;
import java.util.UUID;

public interface ReportForProduct {
    @Value(value = "#{target.productname}")
    String getProductName();
    @Value(value = "#{target.branchname}")
    String getBranchName();
    @Value(value = "#{target.quantity}")
    Integer getQuantity();
    @Value(value = "#{target.categoryname}")
    String getCategoryName();
    @Value(value = "#{target.kpi}")
    Integer getKpi();
    @Value(value = "#{target.createddate}")
    Timestamp getCreatedDate();
    @Value(value = "#{target.tsprice}")
    Double getTsPrice();
}
