package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Navigation;
import uz.pdp.springsecurity.entity.NavigationProcess;
import uz.pdp.springsecurity.enums.Permissions;
import uz.pdp.springsecurity.mapper.NavigationProcessMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NavigationProcessService {
    private final NavigationProcessRepository navigationProcessRepository;
    private final ProductionRepository productionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final FifoCalculationService fifoCalculationService;
    private final WarehouseService warehouseService;
    private final CustomerRepository customerRepository;
    private final LidRepository lidRepository;
    private final SalaryRepository salaryRepository;
    private final OutlayRepository outlayRepository;
    private final TradeRepository tradeRepository;
    private static final LocalDateTime TODAY = LocalDateTime.now();
    private static final LocalDateTime TODAY_START = LocalDate.now().atStartOfDay();
    private final BranchRepository branchRepository;
    private final NavigationProcessMapper navigationProcessMapper;
    private final NavigationRepository navigationRepository;
    private final FormLidHistoryRepository formLidHistoryRepository;

    public ApiResponse get(UUID branchId, Integer year, Integer month) {
        if (!branchRepository.existsById(branchId))
            return new ApiResponse("BRANCH NOT FOUND", false);
        if (!navigationRepository.existsByBranchId(branchId))
            return new ApiResponse("NAVIGATION NOT FOUND", false);
        LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0);
        List<NavigationProcess> goalList = navigationProcessRepository.findAllByBranchIdAndDateBetweenAndRealIsFalse(branchId, Timestamp.valueOf(from), Timestamp.valueOf(from.plusMonths(1)));
        List<NavigationProcess> realList = navigationProcessRepository.findAllByBranchIdAndDateBetweenAndRealIsTrue(branchId, Timestamp.valueOf(from), Timestamp.valueOf(from.plusMonths(1)));
        if (goalList.isEmpty())
            return new ApiResponse("USHBU OY UCHUN MA'LUMOT MAVKUD EMAS", false);
        Map<String, Object> response = new HashMap<>();
        response.put("updatable", checkUpdatable(branchId) != null);
        response.put("goalList", navigationProcessMapper.toDtoList(goalList));
        response.put("realList", navigationProcessMapper.toDtoList(realList));
        return new ApiResponse("SUCCESS", true, response);
    }

    public ApiResponse update(UUID branchId) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty())
            return new ApiResponse("BRANCH NOT FOUND", false);
        LocalDateTime startDate = checkUpdatable(branchId);
        if (startDate == null) {
                return new ApiResponse("ERTAGE YANGILASHINGIZ MUMKIN", false);
        }
        int day = 0;
        while (day != 100) {
            createGoal(optionalBranch.get(), startDate);
            day++;
            if (TODAY.getDayOfYear() == startDate.plusDays(day).getDayOfYear())
                break;
        }
        return get(branchId, TODAY.getYear(), TODAY.getMonthValue());
    }

    private LocalDateTime checkUpdatable(UUID branchId) {
        Optional<NavigationProcess> optionalNavigationProcess = navigationProcessRepository.findFirstByBranchIdAndRealTrueOrderByCreatedAtDesc(branchId);
        if (optionalNavigationProcess.isPresent()) {
            NavigationProcess navigationProcess = optionalNavigationProcess.get();
            LocalDateTime createdAt = navigationProcess.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return createdAt.getDayOfYear() != TODAY.getDayOfYear() ? createdAt.plusDays(1) : null;
        }else {
            Optional<Navigation> optionalNavigation = navigationRepository.findByBranchId(branchId);
            if (optionalNavigation.isEmpty())
                return null;
            LocalDateTime startDate = optionalNavigation.get().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return startDate.getDayOfYear() != TODAY.getDayOfYear() ? startDate : null;
        }
    }

    private void createGoal(Branch branch, LocalDateTime date) {
        LocalDateTime dateStart = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 0, 0).plusDays(1);
        Double totalSum = tradeRepository.totalSumByCreatedAtBetweenAndBranchId(Timestamp.valueOf(dateStart.minusDays(1)), Timestamp.valueOf(dateStart), branch.getId());
        double totalSell = totalSum == null ? 0 : totalSum;
        double seller = userRepository.countAllByBranchesIdAndRole_Permissions(branch.getId(), Permissions.ADD_TRADE);
        double averageSell = totalSell / seller;
        double product = productRepository.countAllByBranchId(branch.getId());
        double productBuyPrice = fifoCalculationService.productBuyPriceByBranch(branch.getId());
        double productSalePrice = warehouseService.getProductSalePriceByBranch(branch.getId());
        Double producedProductAmountNull = productionRepository.amountByCreatedAtBetweenAndBranchId(Timestamp.valueOf(dateStart.minusDays(1)), Timestamp.valueOf(dateStart), branch.getId());
        double producedProductAmount = producedProductAmountNull == null ? 0 : producedProductAmountNull;
        Double producedProductPriceNull = productionRepository.priceByCreatedAtBetweenAndBranchId(Timestamp.valueOf(dateStart.minusDays(1)), Timestamp.valueOf(dateStart), branch.getId());
        double producedProductPrice = producedProductPriceNull == null ? 0 : producedProductPriceNull;
        double customer = customerRepository.countAllByBranchesId(branch.getId());
        double lid = lidRepository.countAllByCreatedAtBetweenAndBusinessId(Timestamp.valueOf(dateStart.minusDays(1)), Timestamp.valueOf(dateStart), branch.getBusiness().getId());

        Double lidPriceNull = formLidHistoryRepository.lidPriceByCreatedAtBetweenAndBusinessId(Timestamp.valueOf(TODAY_START.minusDays(30)), Timestamp.valueOf(TODAY_START), branch.getId());
        double lidPrice = lidPriceNull == null ? 0 : lidPriceNull;
        lidPrice /= 30;

        Double salaryNull = salaryRepository.salaryByCreatedAtBetweenAndBranchId(Timestamp.valueOf(dateStart.minusDays(30)), Timestamp.valueOf(dateStart), branch.getId());
        double salary = salaryNull == null ? 0 : salaryNull;
        salary /= 30;

        double totalUser = userRepository.countAllByBranchesId(branch.getId());
        Double outlayNull = outlayRepository.outlayByCreatedAtBetweenAndBranchId(Timestamp.valueOf(dateStart.minusDays(30)), Timestamp.valueOf(dateStart), branch.getId());
        double outlay = outlayNull == null ? 0 : outlayNull;
        outlay /= 30;
        navigationProcessRepository.save(new NavigationProcess(
                branch,
                true,
                Timestamp.valueOf(date),
                Math.round(totalSell / 100) * 100,
                seller,
                Math.round(averageSell / 100) * 100,
                product,
                Math.round(productBuyPrice / 100) * 100,
                Math.round(productSalePrice / 100) * 100,
                producedProductAmount,
                Math.round(producedProductPrice / 100) * 100,
                customer,
                lid,
                lidPrice,
                Math.round(salary / 100) * 100,
                totalUser,
                Math.round(outlay / 100) * 100
        ));

    }

    public void createGoal(Navigation navigation) {
        Branch branch = navigation.getBranch();
        LocalDateTime startDateLocal = navigation.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDateLocal = navigation.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Date startDate = navigation.getStartDate();
        Date endDate = navigation.getEndDate();
        int day = (int) Math.ceil((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24.));
        double totalSell = navigation.getInitial();
        double totalSellGoal = navigation.getGoal();
        double totalSellStep = (totalSellGoal - totalSell) / day;
        double times = totalSellGoal / totalSell;

        double seller = userRepository.countAllByBranchesIdAndRole_Permissions(branch.getId(), Permissions.ADD_TRADE);
        double sellerGoal = Math.ceil(seller * times / 2);
        double sellerStep = (sellerGoal - seller) / day;

        double averageSell = totalSell / seller;
        double averageSellGoal = Math.round(totalSellGoal / sellerGoal / 100) * 100;
        double averageSellStep = (averageSellGoal - averageSell) / day;

        double product = productRepository.countAllByBranchId(branch.getId());
        double productGoal = Math.round(product * times);
        double productStep = (productGoal - product) / day;

        double productSalePrice = fifoCalculationService.productBuyPriceByBranch(branch.getId());
        double productSalePriceGoal = Math.round(productSalePrice * times / 100) * 100;
        double productSalePriceStep = (productSalePriceGoal - productSalePrice) / day;

        double productBuyPrice = warehouseService.getProductSalePriceByBranch(branch.getId());
        double productBuyPriceGoal = Math.round(productBuyPrice * times / 100) * 100;
        double productBuyPriceStep = (productBuyPriceGoal - productBuyPrice) / day;

        Double producedProductAmountNull = productionRepository.amountByCreatedAtBetweenAndBranchId(Timestamp.valueOf(TODAY_START.minusDays(30)), Timestamp.valueOf(TODAY_START), branch.getId());
        double producedProductAmount = producedProductAmountNull == null ? 0 : producedProductAmountNull;
        producedProductAmount /= 30;
        double producedProductAmountGoal = Math.round(producedProductAmount * times);
        double producedProductAmountStep = (producedProductAmountGoal - producedProductAmount) / day;

        Double producedProductPriceNull = productionRepository.priceByCreatedAtBetweenAndBranchId(Timestamp.valueOf(TODAY_START.minusDays(30)), Timestamp.valueOf(TODAY_START), branch.getId());
        double producedProductPrice = producedProductPriceNull == null ? 0 : producedProductPriceNull;
        producedProductPrice /= 30;
        double producedProductPriceGoal = Math.round(producedProductPrice * times / 100) * 100;
        double producedProductPriceStep = (producedProductPriceGoal - producedProductPrice) / day;

        double customer = customerRepository.countAllByBranchesId(branch.getId());
        double customerGoal = Math.ceil(customer * times);
        double customerStep = (customerGoal - customer) / day;

        double lid = lidRepository.countAllByCreatedAtBetweenAndBusinessId(Timestamp.valueOf(TODAY_START.minusDays(30)), Timestamp.valueOf(TODAY_START), branch.getBusiness().getId());
        lid /= 30;
        double lidGoal = Math.ceil(lid * times);
        double lidStep = (lidGoal - lid) / day;

        Double lidPriceNull = formLidHistoryRepository.lidPriceByCreatedAtBetweenAndBusinessId(Timestamp.valueOf(TODAY_START.minusDays(30)), Timestamp.valueOf(TODAY_START), branch.getId());
        double lidPrice = lidPriceNull == null ? 0 : lidPriceNull;
        lidPrice /= 30;
        double lidPriceGoal = Math.ceil(lidPrice * times);
        double lidPriceStep = (lidPriceGoal - lidPrice) / day;

        Double salaryNull = salaryRepository.salaryByCreatedAtBetweenAndBranchId(Timestamp.valueOf(TODAY_START.minusDays(30)), Timestamp.valueOf(TODAY_START), branch.getId());
        double salary = salaryNull == null ? 0 : salaryNull;
        salary /= 30;
        double salaryGoal = Math.ceil(salary * times / 100) * 100;
        double salaryStep = (salaryGoal - salary) / day;

        double totalUser = userRepository.countAllByBranchesId(branch.getId());
        double totalUserGoal = Math.ceil(totalUser * times);
        double totalUserStep = (totalUserGoal - totalUser) / day;

        Double outlayNull = outlayRepository.outlayByCreatedAtBetweenAndBranchId(Timestamp.valueOf(TODAY_START.minusDays(30)), Timestamp.valueOf(TODAY_START), branch.getId());
        double outlay = outlayNull == null ? 0 : outlayNull;
        outlay /= 30;
        double outlayGoal = Math.ceil(outlay * times / 100) * 100;
        double outlayStep = (outlayGoal - outlay) / day;


        List<NavigationProcess> navigationProcessList = new ArrayList<>();
        for (int i = 0; i < day; i++) {
            NavigationProcess navigationProcess = new NavigationProcess();
            navigationProcess.setBranch(branch);
            navigationProcess.setReal(false);
            navigationProcess.setDate(Timestamp.valueOf(startDateLocal.plusDays(i)));

            navigationProcess.setTotalSell(Math.round((totalSell + i * totalSellStep) / 100) * 100); // 1 totalSell
            navigationProcess.setSeller(Math.round(seller + i * sellerStep)); // 2 seller
            navigationProcess.setAverageSell(Math.round((averageSell + i * averageSellStep) / 100) * 100); // 3 averageSell
            navigationProcess.setProduct(Math.round(product + i * productStep)); // 4 product
            navigationProcess.setProductBuyPrice(Math.round((productBuyPrice + i * productBuyPriceStep) / 100) * 100); // 5 productBuyPrice
            navigationProcess.setProductSalePrice(Math.round((productSalePrice + i * productSalePriceStep) / 100) * 100); // 6 productSalePrice
            navigationProcess.setProducedProductAmount(Math.round(producedProductAmount + i * producedProductAmountStep)); // 7 producedProductAmount
            navigationProcess.setProducedProductPrice(Math.round((producedProductPrice + i * producedProductPriceStep) / 100) * 100); // 8 producedProductPrice
            navigationProcess.setCustomer(Math.round(customer + i * customerStep)); // 9 customer
            navigationProcess.setLid(Math.round(lid + i * lidStep)); // 10 lid
            navigationProcess.setLidPrice(lidPrice + i * lidPriceStep); // 11 lidPrice
            navigationProcess.setSalary(Math.round((salary + i * salaryStep) / 100) * 100); // 12 salary
            navigationProcess.setTotalUser(Math.round(totalUser + i * totalUserStep)); // 13 user
            navigationProcess.setOutlay(Math.round((outlay + i * outlayStep) / 100) * 100); // 14 outlay
            navigationProcessList.add(navigationProcess);
        }
        navigationProcessList.add(new NavigationProcess(
                branch,
                false,
                Timestamp.valueOf(endDateLocal),
                totalSellGoal,
                sellerGoal,
                averageSellGoal,
                productGoal,
                productBuyPriceGoal,
                productSalePriceGoal,
                producedProductAmountGoal,
                producedProductPriceGoal,
                customerGoal,
                lidGoal,
                lidPriceGoal,
                salaryGoal,
                totalUser,
                outlayGoal
        ));
        navigationProcessRepository.saveAll(navigationProcessList);
    }
}
