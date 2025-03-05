package uz.dizgo.erp.hr.service.generalplan;

import org.springframework.http.HttpEntity;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.payload.Result;
import uz.dizgo.erp.payload.GeneralPlanDto;

import java.util.UUID;

public interface GeneralPlanService {
    HttpEntity<Result> addGeneralPlan(GeneralPlanDto generalPlanDto);

    HttpEntity<Result> getGeneralPlan(UUID branch, Integer page, Integer limit, User user);

    HttpEntity<Result> deleteGeneratePlan(UUID id);

    HttpEntity<Result> updateGeneralPlan(UUID id, GeneralPlanDto generalPlanDto);
}
