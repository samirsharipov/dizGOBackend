package uz.dizgo.erp.hr.controller.agentplan;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CurrentUser;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.payload.AgentPlanDto;
import uz.dizgo.erp.hr.payload.AgentPlanUpdateDto;
import uz.dizgo.erp.hr.payload.Result;
import uz.dizgo.erp.hr.service.agentplan.AgentPlanService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hr/agent-plan")
@RequiredArgsConstructor
public class AgentPlanController {
    private final AgentPlanService service;

    @GetMapping("/search-product")
    public HttpEntity<Result> searchProduct(@RequestParam String branch, @RequestParam String q) {
        return service.searchProduct(branch, q);
    }

    @PostMapping
    public HttpEntity<Result> addAgentPlan(@RequestBody AgentPlanDto[] agentPlanDto) {
        return service.addAgentPlan(agentPlanDto);
    }

    @GetMapping
    public HttpEntity<Result> getPlan(@RequestParam(required = false) String branch, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "15") Integer limit, @CurrentUser User user) {
        return service.getPlan(branch, page, limit, user);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<Result> deleteAgentPlan(@PathVariable UUID id) {
        return service.deleteAgentPlan(id);
    }

    @PutMapping("/{id}")
    public HttpEntity<Result> updateAgentPlan(@PathVariable UUID id, @RequestBody AgentPlanUpdateDto agentPlanUpdateDto) {
        return service.updateAgentPlan(id,agentPlanUpdateDto);
    }
}
