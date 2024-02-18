package uz.pdp.springsecurity.hr.service.generalplan;

import org.springframework.http.HttpEntity;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.hr.payload.Result;
import uz.pdp.springsecurity.payload.GeneralPlanDto;

import java.util.UUID;

public interface GeneralPlanService {
    HttpEntity<Result> addGeneralPlan(GeneralPlanDto generalPlanDto);

    HttpEntity<Result> getGeneralPlan(UUID branch, Integer page, Integer limit, User user);

    HttpEntity<Result> deleteGeneratePlan(UUID id);

    HttpEntity<Result> updateGeneralPlan(UUID id, GeneralPlanDto generalPlanDto);
}
