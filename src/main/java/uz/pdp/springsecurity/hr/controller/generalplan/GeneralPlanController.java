package uz.pdp.springsecurity.hr.controller.generalplan;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CurrentUser;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.hr.payload.Result;
import uz.pdp.springsecurity.hr.service.generalplan.GeneralPlanService;
import uz.pdp.springsecurity.payload.GeneralPlanDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hr/general-plan")
@RequiredArgsConstructor
public class GeneralPlanController {
    private final GeneralPlanService service;

    @PostMapping
    public HttpEntity<Result> addGeneralPlan(@RequestBody GeneralPlanDto generalPlanDto) {
        return service.addGeneralPlan(generalPlanDto);
    }

    @GetMapping
    public HttpEntity<Result> getGeneralPlan(@RequestParam(required = false) UUID branch, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "15") Integer limit, @CurrentUser User user) {
        return service.getGeneralPlan(branch, page, limit, user);
    }
    @DeleteMapping("/{id}")
    public HttpEntity<Result> deleteGeneratePlan(@PathVariable UUID id){
        return service.deleteGeneratePlan(id);
    }
    @PutMapping("/{id}")
    public HttpEntity<Result> updateGeneralPlan(@PathVariable UUID id,@RequestBody GeneralPlanDto generalPlanDto){
        return service.updateGeneralPlan(id,generalPlanDto);
    }
}
