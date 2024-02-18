package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.mapper.PrizeMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.PrizeDto;
import uz.pdp.springsecurity.repository.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PrizeService {
    private final BranchRepository branchRepository;
    private final BonusRepository bonusRepository;
    private final PrizeRepository prizeRepository;
    private final UserRepository userRepository;
    private final SalaryService salaryService;
    private final PrizeMapper prizeMapper;
    public ApiResponse add(PrizeDto prizeDto) {
        Optional<Branch> optionalBranch = branchRepository.findById(prizeDto.getBranchId());
        if (optionalBranch.isEmpty()) return new ApiResponse("BRANCH NOT FOUND", false);
        Optional<Bonus> optionalBonus = bonusRepository.findByDeleteFalseAndId(prizeDto.getBonusId());
        if (optionalBonus.isEmpty()) return new ApiResponse("BONUS NOT FOUND", false);
        Optional<User> optionalUser = userRepository.findById(prizeDto.getUserId());
        if (optionalUser.isEmpty()) return new ApiResponse("USER NOT FOUND", false);
        Bonus bonus = optionalBonus.get();
        Prize prize = new Prize(
                optionalBranch.get(),
                optionalUser.get(),
                bonus,
                prizeDto.getDate(),
                prizeDto.getDescription(),
                prizeDto.isGiven()
            );
        if (prizeDto.isGiven()){
            salaryService.add(optionalUser.get(), optionalBranch.get(), bonus.getSumma());
        }else if (prizeDto.isTask()){
            Optional<Prize> optionalPrize = prizeRepository.findByUserIdAndBranchIdAndTaskTrueAndGivenFalse(prizeDto.getUserId(), prizeDto.getBranchId());
            if (optionalPrize.isPresent()) {
                Prize oldPrize = optionalPrize.get();
                if (oldPrize.getDeadline().after(new Date()))
                    return new ApiResponse("HODIMDA TUGALLANMAGAN BONUS BOR", false);
                else {
                    boolean delete = delete(oldPrize);
                    if (!delete) return new ApiResponse("DELETE ERROR", false);
                }
            }
            prize.setTask(true);
            prize.setDeadline(prizeDto.getDeadline());
            prize.setCount(prizeDto.getCount());
        }else if (prizeDto.isLid()){
            Optional<Prize> optionalPrize = prizeRepository.findByUserIdAndBranchIdAndLidTrueAndGivenFalse(prizeDto.getUserId(), prizeDto.getBranchId());
            if (optionalPrize.isPresent()) {
                Prize oldPrize = optionalPrize.get();
                if (oldPrize.getDeadline().after(new Date()))
                    return new ApiResponse("HODIMDA TUGALLANMAGAN BONUS BOR", false);
                else {
                    boolean delete = delete(oldPrize);
                    if (!delete) return new ApiResponse("DELETE ERROR", false);
                }
            }
            prize.setLid(true);
            prize.setDeadline(prizeDto.getDeadline());
            prize.setCount(prizeDto.getCount());
        }else {
            return new ApiResponse("BOOLEAN ERROR", false);
        }
        prizeRepository.save(prize);
        return new ApiResponse("SUCCESS", true);
    }

//    public ApiResponse edit(UUID prizeId, PrizeDto prizeDto) {
//        return new ApiResponse("SUCCESS", true);
//    }

    public ApiResponse getAll(UUID branchId) {
        if (!branchRepository.existsById(branchId)) return new ApiResponse("BRANCH NOT FOUND", false);
        List<Prize> prizeList = prizeRepository.findAllByBranchId(branchId);
        if (prizeList.isEmpty())return new ApiResponse("PRIZE NOT FOUND", false);
        return new ApiResponse(true, prizeMapper.toDtoList(prizeList));
    }

    public ApiResponse getOne(UUID prizeId) {
        Optional<Prize> optionalPrize = prizeRepository.findById(prizeId);
        return optionalPrize.map(prize -> new ApiResponse(true, prizeMapper.toDto(prize))).orElse(new ApiResponse("PRIZE NOT FOUND", false));
    }

    public ApiResponse getByUserAll(UUID userId, UUID branchId) {
        List<Prize> prizeList = prizeRepository.findAllByBranchIdAndUserIdAndGivenTrueOrderByDateDesc(branchId, userId);
        if (prizeList.isEmpty()) return new ApiResponse("PRIZE NOT FOUND", false);
        return new ApiResponse(true, prizeMapper.toDtoList(prizeList));
    }

    public ApiResponse getByUserLastMonth(UUID userId, UUID branchId) {
        List<Prize> prizeList = prizeRepository.findAllByBranchIdAndUserIdAndActiveTrueAndGivenTrue(branchId, userId);
        if (prizeList.isEmpty()) return new ApiResponse("PRIZE NOT FOUND", false);
        Map<String, Object> response = new HashMap<>();
        double totalSum = 0;
        for (Prize prize : prizeList) {
            totalSum += prize.getBonus().getSumma();
        }
        response.put("totalSum", totalSum);
        response.put("prizeGetDtoList", prizeMapper.toDtoList(prizeList));
        return new ApiResponse(true, response);
    }

    public boolean delete(Prize prize) {
        try {
            prizeRepository.deleteById(prize.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void addForTask(Task task) {
        Set<User> userSet = new HashSet<>();
        for (TaskPrice taskPrice : task.getTaskPriceList()) {
            userSet.addAll(taskPrice.getUserList());
        }
        addForTaskHelper(userSet, task.getBranch());
    }

    private void addForTaskHelper(Set<User> userSet, Branch branch) {
        for (User user : userSet) {
            Optional<Prize> optionalPrize = prizeRepository.findByUserIdAndBranchIdAndTaskTrueAndGivenFalse(user.getId(), branch.getId());
            if (optionalPrize.isEmpty()) return;
            Prize prize = optionalPrize.get();
            if (prize.getDeadline().before(new Date())) {
                delete(prize);
                return;
            }
            prize.setCounter(prize.getCounter() + 1);
            if (prize.getCount() <= prize.getCounter()) {
                prize.setGiven(true);
                salaryService.add(user, branch, prize.getBonus().getSumma());
            }
            prizeRepository.save(prize);
        }
    }

    public void addPrizeForLid() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (Branch branch : user.getBranches()) {
            addForLidHelper(user, branch);
        }
    }

    private void addForLidHelper(User user, Branch branch) {
        Optional<Prize> optionalPrize = prizeRepository.findByUserIdAndBranchIdAndLidTrueAndGivenFalse(user.getId(), branch.getId());
        if (optionalPrize.isEmpty()) return;
        Prize prize = optionalPrize.get();
        if (prize.getDeadline().before(new Date())) {
            delete(prize);
            return;
        }
        prize.setCounter(prize.getCounter() + 1);
        if (prize.getCount() <= prize.getCounter()) {
            prize.setGiven(true);
            salaryService.add(user, branch, prize.getBonus().getSumma());
        }
        prizeRepository.save(prize);
    }

    public void deActive(UUID userId, UUID branchId) {
        List<Prize> prizeList = prizeRepository.findAllByBranchIdAndUserIdAndActiveTrueAndGivenTrue(branchId, userId);
        for (Prize prize : prizeList) {
            prize.setActive(false);
        }
        prizeRepository.saveAll(prizeList);
    }
}