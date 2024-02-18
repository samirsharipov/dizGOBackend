package uz.pdp.springsecurity.hr.service.agentplan;

import org.springframework.http.HttpEntity;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.hr.payload.AgentPlanDto;
import uz.pdp.springsecurity.hr.payload.AgentPlanUpdateDto;
import uz.pdp.springsecurity.hr.payload.Result;

import java.util.UUID;

public interface AgentPlanService {
    HttpEntity<Result> searchProduct(String branch, String q);

    HttpEntity<Result> addAgentPlan(AgentPlanDto[] agentPlanDto);

    HttpEntity<Result> getPlan(String branch, Integer page, Integer limit, User user);

    HttpEntity<Result> deleteAgentPlan(UUID id);

    HttpEntity<Result> updateAgentPlan(UUID id, AgentPlanUpdateDto agentPlanUpdateDto);
}
