package uz.dizgo.erp.hr.service.generalplan;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.entity.GeneralPlan;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.exception.HRException;
import uz.dizgo.erp.hr.payload.GeneralPlanResult;
import uz.dizgo.erp.hr.payload.Result;
import uz.dizgo.erp.payload.GeneralPlanDto;
import uz.dizgo.erp.repository.BranchRepository;
import uz.dizgo.erp.repository.GeneralPlanRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GeneralPlanServiceImpl implements GeneralPlanService {
    private final BranchRepository branchRepository;
    private final GeneralPlanRepository generalPlanRepository;

    @Override
    public HttpEntity<Result> addGeneralPlan(GeneralPlanDto generalPlanDto) {
        Branch branch = branchRepository.findById(generalPlanDto.getBranch()).orElseThrow(() -> new HRException("Filial topilmadi"));
        try {
            generalPlanRepository.save(GeneralPlan.builder()
                    .tam(generalPlanDto.getTam())
                    .sam(generalPlanDto.getSam())
                    .som(generalPlanDto.getSom())
                    .active(true)
                    .agentsCount(generalPlanDto.getAgentsCount())
                    .startDate(generalPlanDto.getStartDate())
                    .endDate(generalPlanDto.getEndDate())
                    .branch(branch)
                    .build());
            return ResponseEntity.ok(new Result(true, "Umumiy reja saqlandi!"));
        } catch (Exception e) {
            throw new HRException("Umumiy reja saqlashda xatolik yuzaga keldi!");
        }
    }

    @Override
    public HttpEntity<Result> getGeneralPlan(UUID branch, Integer page, Integer limit, User user) {
        if (branch == null) {
            Page<GeneralPlan> generalPlanPage = generalPlanRepository.findAllByBranch_Business_IdAndActiveTrue(user.getBusiness().getId(), PageRequest.of(page - 1, limit));
            List<GeneralPlanResult> results = new LinkedList<>();
            for (GeneralPlan generalPlan : generalPlanPage.getContent()) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", generalPlan.getBranch().getId());
                data.put("name", generalPlan.getBranch().getName());
                results.add(new GeneralPlanResult(
                        generalPlan.getId(),
                        generalPlan.getTam(),
                        generalPlan.getSam(),
                        generalPlan.getSom(),
                        generalPlan.getAgentsCount(),
                        generalPlan.getStartDate(),
                        generalPlan.getEndDate(),
                        generalPlan.isActive(),
                        generalPlan.getCreatedAt(),
                        data
                ));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("list", results);
            data.put("totalPages", generalPlanPage.getTotalPages());
            return ResponseEntity.ok(new Result(true, "Umumiy rejalar", data));
        } else {
            Branch branch1 = branchRepository.findById(branch).orElseThrow(() -> new HRException("Filial topilmadi!"));
            Page<GeneralPlan> generalPlanPage = generalPlanRepository.findAllByBranch_IdAndActiveTrue(branch1.getId(), PageRequest.of(page - 1, limit));
            List<GeneralPlanResult> results = new LinkedList<>();
            for (GeneralPlan generalPlan : generalPlanPage.getContent()) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", generalPlan.getBranch().getId());
                data.put("name", generalPlan.getBranch().getName());
                results.add(new GeneralPlanResult(
                        generalPlan.getId(),
                        generalPlan.getTam(),
                        generalPlan.getSam(),
                        generalPlan.getSom(),
                        generalPlan.getAgentsCount(),
                        generalPlan.getStartDate(),
                        generalPlan.getEndDate(),
                        generalPlan.isActive(),
                        generalPlan.getCreatedAt(),
                        data
                ));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("list", results);
            data.put("totalPages", generalPlanPage.getTotalPages());
            return ResponseEntity.ok(new Result(true, "Umumiy rejalar", data));
        }
    }

    @Override
    public HttpEntity<Result> deleteGeneratePlan(UUID id) {
        try {
            GeneralPlan generalPlan = generalPlanRepository.findById(id).orElseThrow(() -> new HRException("Reja topilmadi!"));
            generalPlan.setActive(false);
            generalPlanRepository.save(generalPlan);
            return ResponseEntity.ok(new Result(true, "Reja o'chirildi"));
        } catch (Exception e) {
            throw new HRException("Rejani o'chirishda xatolik yuzaga keldi!");
        }
    }

    @Override
    public HttpEntity<Result> updateGeneralPlan(UUID id, GeneralPlanDto generalPlanDto) {
        GeneralPlan generalPlan = generalPlanRepository.findById(id).orElseThrow(() -> new HRException("Reja topilmadi!"));
        Branch branch = branchRepository.findById(generalPlanDto.getBranch()).orElseThrow(() -> new HRException("Filial topilmadi!"));
        try {
            generalPlan.setTam(generalPlanDto.getTam());
            generalPlan.setSam(generalPlanDto.getSam());
            generalPlan.setSom(generalPlanDto.getSom());
            generalPlan.setAgentsCount(generalPlanDto.getAgentsCount());
            generalPlan.setStartDate(generalPlanDto.getStartDate());
            generalPlan.setEndDate(generalPlanDto.getEndDate());
            generalPlan.setBranch(branch);
            generalPlanRepository.save(generalPlan);
            return ResponseEntity.ok(new Result(true, "Reja tahrirlandi!"));
        } catch (Exception e) {
            throw new HRException("Rejani tahrirlashda xatolik yuzaga keldi!");
        }
    }
}
