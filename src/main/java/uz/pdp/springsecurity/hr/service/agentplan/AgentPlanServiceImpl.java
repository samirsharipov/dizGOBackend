package uz.pdp.springsecurity.hr.service.agentplan;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.Type;
import uz.pdp.springsecurity.hr.exception.HRException;
import uz.pdp.springsecurity.hr.payload.*;
import uz.pdp.springsecurity.payload.projections.DataProjection;
import uz.pdp.springsecurity.repository.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AgentPlanServiceImpl implements AgentPlanService {
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final ProductTypePriceRepository productTypePriceRepository;
    private final AgentPlanRepository agentPlanRepository;
    private final UserRepository userRepository;
    private final TradeRepository tradeRepository;

    @Override
    public HttpEntity<Result> searchProduct(String branch, String q) {
        if (branch.isEmpty()) {
            throw new HRException("Filialni tanlang!");
        }
        Branch branch1 = branchRepository.findById(UUID.fromString(branch)).orElseThrow(() -> new HRException("Filial topilmadi!"));
        List<Product> products = productRepository.findAllByBranchIdAndActiveIsTrueAndNameContainingIgnoreCaseOrBarcodeContainingIgnoreCase(branch1.getId(), q, q);
        List<ProductSelectSearchResult> results = new LinkedList<>();
        for (Product product : products) {
            if (product.getType().equals(Type.SINGLE) || product.getType().equals(Type.COMBO)) {
                results.add(new ProductSelectSearchResult(
                        product.getId(),
                        product.getName(),
                        product.getType()
                ));
            } else if (product.getType().equals(Type.MANY)) {
                for (ProductTypePrice productTypePrice : productTypePriceRepository.findAllByProductIdAndActiveTrue(product.getId())) {
                    results.add(new ProductSelectSearchResult(
                            productTypePrice.getId(),
                            productTypePrice.getName(),
                            product.getType()
                    ));
                }
            }
        }
        return ResponseEntity.ok(new Result(true, "Mahsulot topildi", results));
    }

    @Override
    public HttpEntity<Result> addAgentPlan(AgentPlanDto[] agentPlanDto) {
        List<AgentPlan> plans = new LinkedList<>();
        for (AgentPlanDto planDto : agentPlanDto) {
            Branch branch = branchRepository.findById(planDto.getBranch()).orElseThrow(() -> new HRException("Filial topilmadi!"));
            User user = userRepository.findById(planDto.getUser()).orElseThrow(() -> new HRException("Xodim topilmadi!"));
            Product product = null;
            ProductTypePrice productTypePrice = null;
            if (planDto.getProduct().getType().equals(Type.MANY)) {
                productTypePrice = productTypePriceRepository.findById(planDto.getProduct().getValue()).orElseThrow(() -> new HRException("Mahsulot topilmadi!"));
            } else {
                product = productRepository.findById(planDto.getProduct().getValue()).orElseThrow(() -> new HRException("Mahsulot topilmadi!"));
            }
            plans.add(AgentPlan.builder()
                    .plan(planDto.getPlan())
                    .startDate(planDto.getStartDate())
                    .endDate(planDto.getEndDate())
                    .branch(branch)
                    .user(user)
                    .type(planDto.getProduct().getType())
                    .productTypePrice(productTypePrice)
                    .product(product)
                    .active(true)
                    .build());
        }
        try {
            agentPlanRepository.saveAll(plans);
            return ResponseEntity.ok(new Result(true, "Agent reja qo'shildi"));
        } catch (Exception e) {
            throw new HRException("Agent reja qo'shishda xatolik yuzaga keldi!");
        }
    }

    @Override
    public HttpEntity<Result> getPlan(String branch, Integer page, Integer limit, User user) {
        Map<String, Object> data = new HashMap<>();
        if (branch == null || branch.equalsIgnoreCase("ALL") || branch.isEmpty()) {
            Page<AgentPlan> agentPlanPage = agentPlanRepository.findAllByBranch_Business_IdAndActiveTrue(user.getBusiness().getId(), PageRequest.of(page - 1, limit));
            getAgentPlans(data, agentPlanPage);
        } else {
            Branch branch1 = branchRepository.findById(UUID.fromString(branch)).orElseThrow(() -> new HRException("Filial topilmadi!"));
            Page<AgentPlan> agentPlanPage = agentPlanRepository.findAllByBranch_IdAndActiveTrue(branch1.getId(), PageRequest.of(page - 1, limit));
            getAgentPlans(data, agentPlanPage);
        }
        return ResponseEntity.ok(new Result(true, "Agent reja ro'yxati", data));
    }

    @Override
    public HttpEntity<Result> deleteAgentPlan(UUID id) {
        AgentPlan agentPlan = agentPlanRepository.findById(id).orElseThrow(() -> new HRException("Agent reja topilmadi!"));
        agentPlan.setActive(false);
        try {
            agentPlanRepository.save(agentPlan);
            return ResponseEntity.ok(new Result(true, "Agent reja o'chirildi!"));
        } catch (Exception e) {
            throw new HRException("Agent reja o'chirishda muammo yuzaga keldi!");
        }
    }

    @Override
    public HttpEntity<Result> updateAgentPlan(UUID id, AgentPlanUpdateDto agentPlanUpdateDto) {
        AgentPlan agentPlan = agentPlanRepository.findById(id).orElseThrow(() -> new HRException("Agent reja topilmadi!"));
        User user = userRepository.findById(agentPlanUpdateDto.getUser()).orElseThrow(() -> new HRException("Xodim topilmadi!"));
        agentPlan.setPlan(agentPlanUpdateDto.getPlan());
        agentPlan.setStartDate(agentPlanUpdateDto.getStartDate());
        agentPlan.setEndDate(agentPlanUpdateDto.getEndDate());
        agentPlan.setUser(user);
        try {
            agentPlanRepository.save(agentPlan);
            return ResponseEntity.ok(new Result(true, "Tahrirlandi"));
        } catch (Exception e) {
            throw new HRException("Tahrirlashda xatolik yuzaga keldi!");
        }
    }

    private void getAgentPlans(Map<String, Object> data, Page<AgentPlan> agentPlanPage) {
        List<AgentPlanResult> results = new LinkedList<>();
        for (AgentPlan agentPlan : agentPlanPage.getContent()) {
            if (agentPlan.getType().equals(Type.MANY)) {
                int sale = 0;
                for (DataProjection dataProjection : tradeRepository.findAllTradeTypeManyProductByUserIdAndProductIdAndDateRange(agentPlan.getUser().getId(), agentPlan.getProductTypePrice().getId(), agentPlan.getStartDate(), agentPlan.getEndDate())) {
                    sale = sale + dataProjection.getTreaderQuantity();
                }
                results.add(new AgentPlanResult(
                        agentPlan.getId(),
                        agentPlan.getPlan(),
                        agentPlan.getStartDate(),
                        agentPlan.getEndDate(),
                        new AgentPlanProductResult(
                                agentPlan.getProductTypePrice().getId(),
                                agentPlan.getProductTypePrice().getName(),
                                agentPlan.getType()
                        ),
                        new AgentPlanUserResult(
                                agentPlan.getUser().getId(),
                                agentPlan.getUser().getFirstName(),
                                agentPlan.getUser().getLastName()
                        ),
                        sale,
                        agentPlan.getBranch().getId()
                ));
            } else {
                int sale = 0;
                for (DataProjection dataProjection : tradeRepository.findAllByUserIdAndProductIdAndDateRange(agentPlan.getUser().getId(), agentPlan.getProduct().getId(), agentPlan.getStartDate(), agentPlan.getEndDate())) {
                    sale = sale + dataProjection.getTreaderQuantity();
                }
                results.add(new AgentPlanResult(
                        agentPlan.getId(),
                        agentPlan.getPlan(),
                        agentPlan.getStartDate(),
                        agentPlan.getEndDate(),
                        new AgentPlanProductResult(
                                agentPlan.getProduct().getId(),
                                agentPlan.getProduct().getName(),
                                agentPlan.getType()
                        ),
                        new AgentPlanUserResult(
                                agentPlan.getUser().getId(),
                                agentPlan.getUser().getFirstName(),
                                agentPlan.getUser().getLastName()
                        ),
                        sale,
                        agentPlan.getBranch().getId()
                ));
            }
        }
        data.put("list", results);
        data.put("totalPages", agentPlanPage.getTotalPages());
    }
}
