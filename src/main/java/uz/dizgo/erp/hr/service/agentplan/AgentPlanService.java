package uz.dizgo.erp.hr.service.agentplan;

import org.springframework.http.HttpEntity;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.payload.AgentPlanDto;
import uz.dizgo.erp.hr.payload.AgentPlanUpdateDto;
import uz.dizgo.erp.hr.payload.Result;

import java.util.UUID;

public interface AgentPlanService {
    HttpEntity<Result> searchProduct(String branch, String q);

    HttpEntity<Result> addAgentPlan(AgentPlanDto[] agentPlanDto);

    HttpEntity<Result> getPlan(String branch, Integer page, Integer limit, User user);

    HttpEntity<Result> deleteAgentPlan(UUID id);

    HttpEntity<Result> updateAgentPlan(UUID id, AgentPlanUpdateDto agentPlanUpdateDto);
}
