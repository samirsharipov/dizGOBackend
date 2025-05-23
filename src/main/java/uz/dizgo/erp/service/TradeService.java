package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.dizgo.erp.entity.*;
import uz.dizgo.erp.entity.Currency;
import uz.dizgo.erp.payload.*;
import uz.dizgo.erp.repository.*;
import uz.dizgo.erp.enums.HistoryName;
import uz.dizgo.erp.enums.SalaryStatus;
import uz.dizgo.erp.enums.StatusName;
import uz.dizgo.erp.mapper.PaymentMapper;
import uz.dizgo.erp.payload.projections.DataProjection;
import uz.dizgo.erp.service.logger.ProductActivityLogger;
import uz.dizgo.erp.utils.Constants;
import uz.dizgo.erp.utils.AppConstant;
import uz.dizgo.erp.utils.ConstantProduct;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradeService {
    private final TradeRepository tradeRepository;
    private final ProductAboutRepository productAboutRepository;
    private final RepaymentDebtRepository repaymentDebtRepository;
    private final CustomerRepository customerRepository;
    private final DebtCanculsRepository debtCanculsRepository;
    private final CustomerDebtRepository customerDebtRepository;
    private final BranchRepository branchRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final PayMethodRepository payMethodRepository;
    private final UserRepository userRepository;
    private final TradeProductRepository tradeProductRepository;
    private final CurrencyRepository currencyRepository;
    private final WarehouseService warehouseService;
    private final FifoCalculationService fifoCalculationService;
    private final WarehouseRepository warehouseRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final SalaryCountService salaryCountService;
    private final AgreementRepository agreementRepository;
    private final BalanceService balanceService;
    private final BusinessRepository businessRepository;
    private final HistoryRepository historyRepository;
    private final BusinessService businessService;
    private final CustomerSupplierRepository customerSupplierRepository;
    private final CustomerSupplierService customerSupplierService;
    private final CustomerCreditRepository customerCreditRepository;
    private final ProductActivityLogger productActivityLogger;
    private final MessageService messageService;


    @SneakyThrows
    public ApiResponse create(TradeDTO tradeDTO) {
        Optional<Branch> optionalBranch = branchRepository.findById(tradeDTO.getBranchId());
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("not found branch", false);
        }


        Optional<Trade> optionalTrade = tradeRepository.findFirstByBranchIdOrderByCreatedAtDesc(tradeDTO.getBranchId());
        int invoice = 0;
        if (optionalTrade.isPresent()) {
            String invoiceStr = optionalTrade.get().getInvoice();
            invoice = invoiceStr != null ? Integer.parseInt(invoiceStr) : 0;
        }
        int inc = invoice + 1;

        Trade trade = new Trade();
        trade.setBranch(optionalBranch.get());
        trade.setLid(tradeDTO.isLid());
        trade.setInvoice(String.valueOf(inc));
        return createOrEditTrade(trade, tradeDTO, false);
    }

    public ApiResponse edit(UUID tradeId, TradeDTO tradeDTO) {
        Optional<Trade> optionalTrade = tradeRepository.findById(tradeId);
        if (optionalTrade.isEmpty()) {
            return new ApiResponse("NOT FOUND TRADE", false);
        }
        Trade trade = optionalTrade.get();

        UUID businessId = trade.getBranch().getBusiness().getId(); // Business id ni olish
        int editDays = businessService.getEditDays(businessId);

        // createdAt ni LocalDateTime ga o'tkazish
        Timestamp createdAt = trade.getCreatedAt();
        LocalDateTime createdAtLocalDateTime = createdAt.toLocalDateTime();

        int days = (int) ChronoUnit.DAYS.between(createdAtLocalDateTime.toLocalDate(), LocalDateTime.now().toLocalDate());
        if (!trade.isEditable()) {
            return new ApiResponse("SIZNING SAVDONI TAXRIRLASH MUDDATINGIZ O'TGAN MUDDAT: " + editDays + " KUN", false);
        }
        if (days > editDays) {
            trade.setEditable(false);
            return new ApiResponse("SIZNING SAVDONI TAXRIRLASH MUDDATINGIZ O'TGAN MUDDAT: " + editDays + " KUN", false);
        }
        return createOrEditTrade(trade, tradeDTO, true);
    }


    @Transactional
    public ApiResponse createOrEditTrade(Trade trade, TradeDTO tradeDTO, boolean isEdit) {
        Branch branch = trade.getBranch();
        CustomerDebt customerDebt = new CustomerDebt();

        Optional<User> optionalUser = userRepository.findById(tradeDTO.getUserId());
        if (optionalUser.isEmpty()) {
            return new ApiResponse("TRADER NOT FOUND", false);
        }
        trade.setTrader(optionalUser.get());

        Optional<PaymentStatus> optionalPaymentStatus = paymentStatusRepository.findById(tradeDTO.getPaymentStatusId());
        if (optionalPaymentStatus.isEmpty()) {
            return new ApiResponse("PAYMENT STATUS NOT FOUND", false);
        }
        trade.setPaymentStatus(optionalPaymentStatus.get());

        Optional<Agreement> optionalAgreementKpi = agreementRepository.findByUserIdAndSalaryStatus(trade.getTrader().getId(), SalaryStatus.KPI);
        if (optionalAgreementKpi.isEmpty()) {
            return new ApiResponse("AGREEMENT NOT FOUND", false);
        }

        if (tradeDTO.getPaymentDtoList().isEmpty()) {
            return new ApiResponse("PAYMENT METHOD NOT FOUND", false);
        }

        if (tradeDTO.getProductTraderDto().isEmpty()) {
            return new ApiResponse("PRODUCT LIST NOT FOUND", false);
        }

        try {
            if (!branch.getBusiness().isSaleMinus()) {
                HashMap<UUID, Double> map = new HashMap<>();
                for (TradeProductDto dto : tradeDTO.getProductTraderDto()) {
                    double tradedQuantity = dto.getTradedQuantity();
                    if (dto.getTradeProductId() != null) {
                        Optional<TradeProduct> optionalTradeProduct = tradeProductRepository.findById(dto.getTradeProductId());
                        if (optionalTradeProduct.isPresent()) {
                            tradedQuantity -= optionalTradeProduct.get().getTradedQuantity();
                            if (tradedQuantity < 0) tradedQuantity = 0d;
                        }
                    }
                    UUID productId = dto.getProductId();
                    map.put(productId, map.getOrDefault(productId, 0d) + tradedQuantity);
                }

                if (!warehouseService.checkBeforeTrade(branch, map))
                    return new ApiResponse("NOT ENOUGH PRODUCT", false);
            }
        } catch (Exception e) {
            return new ApiResponse("CHECKING ERROR", false);
        }

        double unFrontPayment = 0;
        try {
            double debtSum = trade.getDebtSum();
            if (tradeDTO.getDebtSum() > 0 || debtSum != tradeDTO.getDebtSum()) {
                if (tradeDTO.getCustomerId() == null) return new ApiResponse("CUSTOMER NOT FOUND", false);
                Optional<Customer> optionalCustomer = customerRepository.findById(tradeDTO.getCustomerId());
                if (optionalCustomer.isEmpty()) return new ApiResponse("CUSTOMER NOT FOUND", false);
                double newDebt = tradeDTO.getDebtSum() - debtSum;
                Customer customer = optionalCustomer.get();
                double debt = -customer.getDebt() + trade.getPaidSum();
                if (customer.getDebt() < 0 && newDebt > 0) {
                    unFrontPayment = Math.min(debt, newDebt);
                }
                trade.setCustomer(customer);
                customer.setDebt(newDebt - debt);
                customer.setPayDate(tradeDTO.getPayDate());

                //customerga savdo qilgan dukonini idsini biriktirish
                customer.addBranchId(branch.getId());

                customerRepository.save(customer);


                if (isEdit) {
                    Optional<CustomerDebt> optionalCustomerDebt = customerDebtRepository.findByTrade_Id(trade.getId());
                    if (optionalCustomerDebt.isPresent()) {
                        customerDebt = optionalCustomerDebt.get();
                        customerDebt.setCustomer(customer);
                        customerDebt.setDebtSum(newDebt - debt);
                    }
                } else {
                    customerDebt.setCustomer(customer);
                    customerDebt.setDebtSum(newDebt - debt);
                }

            } else if (tradeDTO.getCustomerId() != null) {

                Optional<Customer> optionalCustomer = customerRepository.findById(tradeDTO.getCustomerId());
                if (optionalCustomer.isEmpty()) return new ApiResponse("CUSTOMER NOT FOUND", false);
                Customer customer = optionalCustomer.get();
                trade.setCustomer(customer);
                //customerga savdo qilgan dukonini idsini biriktirish
                customer.addBranchId(branch.getId());
                customerRepository.save(customer);
            }
        } catch (Exception e) {
            return new ApiResponse("CUSTOMER ERROR", false);
        }

        if (tradeDTO.isBacking()) {
            trade.setBacking(true);
        } else if (trade.getBacking() == null) {
            trade.setBacking(false);
        }
        trade.setDollar(tradeDTO.getDollar());
        trade.setGross(tradeDTO.getGross());
        trade.setPayDate(tradeDTO.getPayDate());
        trade.setTotalSum(tradeDTO.getTotalSum());
        trade.setDollarTrade(tradeDTO.isDollarTrade());
        trade.setDifferentPayment(tradeDTO.getDifferentPayment() != null && tradeDTO.getDifferentPayment());
        trade.setTradeDescription(tradeDTO.getTradeDescription() != null ? tradeDTO.getTradeDescription() : "");
        trade.setPaidSum(tradeDTO.getPaidSum() + unFrontPayment);
        trade.setDebtSum(tradeDTO.getDebtSum() - unFrontPayment);
        Optional<Currency> optionalCurrency = currencyRepository.findByBusinessId(branch.getBusiness().getId());
        if (optionalCurrency.isPresent()) {
            trade.setDebtSumDollar(Math.round(trade.getDebtSum() / optionalCurrency.get().getCourse() * 100) / 100.);
            trade.setPaidSumDollar(Math.round(trade.getPaidSum() / optionalCurrency.get().getCourse() * 100) / 100.);
            trade.setTotalSumDollar(Math.round(trade.getTotalSum() / optionalCurrency.get().getCourse() * 100) / 100.);
        }

        tradeRepository.save(trade);

        String status = paymentStatusRepository.findById(tradeDTO.getPaymentStatusId()).get().getStatus();

        if (status.equals("TOLANMAGAN") || status.equals("QISMAN_TOLANGAN") && tradeDTO.getCustomerId() != null) {
            for (PaymentDto paymentDto : tradeDTO.getPaymentDtoList()) {
                repaymentDebtRepository.save(
                        new RepaymentDebt(
                                customerRepository.findById(tradeDTO.getCustomerId()).get(),
                                sumOrDollar(paymentDto),
                                sumOrDollar2(paymentDto, tradeDTO),
                                repaymentIsDollar(tradeDTO, paymentDto),
                                payMethodRepository.findById(paymentDto.getPaymentMethodId()).get(),
                                "",
                                false,
                                Timestamp.from(Instant.now())
                        ));
            }
        }
//        HISTORY
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!isEdit) {
            historyRepository.save(new History(
                    HistoryName.SAVDO,
                    user,
                    branch,
                    trade.getInvoice() + AppConstant.ADD_TRADE
            ));

        } else {
            if (tradeDTO.isBacking()) {
                historyRepository.save(new History(
                        HistoryName.SAVDO,
                        user,
                        branch,
                        trade.getInvoice() + AppConstant.BACKING_TRADE
                ));
            } else {
                historyRepository.save(new History(
                        HistoryName.SAVDO,
                        user,
                        branch,
                        trade.getInvoice() + AppConstant.EDIT_TRADE
                ));
            }
        }

        if (paymentRepository.existsByTradeId(trade.getId())) {
            List<Payment> paymentList = paymentRepository.findAllByTradeId(trade.getId());
            if (!paymentList.isEmpty()) {
                for (Payment payment : paymentList) {
                    paymentRepository.deleteById(payment.getId());
                }
            }
        }

        List<Payment> paymentList = new ArrayList<>();
        for (PaymentDto paymentDto : tradeDTO.getPaymentDtoList()) {
            Optional<PaymentMethod> optionalPaymentMethod = payMethodRepository.findById(paymentDto.getPaymentMethodId());
            if (optionalPaymentMethod.isEmpty()) return new ApiResponse("PAYMENT METHOD NOT FOUND", false);
            paymentList.add(new Payment(
                    trade,
                    optionalPaymentMethod.get(),
                    paymentDto.getIsDollar() != null && paymentDto.getIsDollar() ? paymentDto.getPaidSumDollar() : paymentDto.getPaidSum(),
                    paymentDto.getIsDollar() != null && paymentDto.getIsDollar() ? paymentDto.getPaidSum() : paymentDto.getPaidSumDollar(),
                    paymentDto.getIsDollar() != null ? paymentDto.getIsDollar() : false
            ));
        }

        if (paymentList.isEmpty()) {
            return new ApiResponse("PAYMENT METHOD NOT FOUND", false);
        }
        paymentList.get(0).setPaidSum(paymentList.get(0).getPaidSum() + unFrontPayment);
        paymentRepository.saveAll(paymentList);
        trade.setPayMethod(paymentList.get(0).getPayMethod());

        List<TradeProduct> tradeProductList = new ArrayList<>();

        if (Boolean.TRUE.equals(tradeDTO.getIsSuccess())) {
            DebtCanculs debtCanculs = new DebtCanculs();
            debtCanculs.setTrade(trade);
            debtCanculs.setDollarPrice(tradeDTO.getDollarPrice());
            debtCanculs.setDebtPrice(tradeDTO.getDebdSum());
            debtCanculsRepository.save(debtCanculs);
            Optional<PaymentMethod> optional = payMethodRepository.findByType("Naqd");
            optional.ifPresent(paymentMethod -> balanceService.edit(tradeDTO.getBranchId(), tradeDTO.getDollarPrice(), true, paymentMethod.getId(), true, "Savdo so'mda bo'ldi tulov dollarda!"));

            if (tradeDTO.getDebdSum() > 0)
                optional.ifPresent(paymentMethod -> balanceService.edit(tradeDTO.getBranchId(), Double.valueOf(tradeDTO.getDebdSum()), false, paymentMethod.getId(), false, "Savdo so'mda bo'ldi tulov dollarda " + tradeDTO.getDebdSum() + " so'm qaytim sifatida berildi!"));
        } else {
            try {
                balanceService.edit(branch.getId(), true, tradeDTO.getPaymentDtoList(), tradeDTO.getDollar(), "trade");
            } catch (Exception e) {
                return new ApiResponse("BALANCE SERVICE ERROR", false);
            }
        }

        try {
            double profit = 0;
            for (TradeProductDto tradeProductDto : tradeDTO.getProductTraderDto()) {
                if (tradeProductDto.isDelete() && tradeProductDto.getTradeProductId() != null) {
                    Optional<TradeProduct> optionalTradeProduct = tradeProductRepository.findById(tradeProductDto.getTradeProductId());
                    if (optionalTradeProduct.isPresent()) {
                        TradeProduct tp = optionalTradeProduct.get();

                        if (tradeDTO.isBacking()) {
                            if (tp.getBacking() != null) {
                                tp.setBacking(tp.getBacking() + tp.getTradedQuantity());
                            } else {
                                tp.setBacking(tp.getTradedQuantity());
                            }
                        }
                        double tradedQuantity = tradeProductDto.getTradedQuantity(); // to send fifo calculation
                        tradeProductDto.setTradedQuantity(0);//  to make sold quantity 0
                        TradeProduct savedTradeProduct = warehouseService.createOrEditTrade(tp.getTrade().getBranch(), tp, tradeProductDto, trade.getId());
                        fifoCalculationService.returnedTrade(branch, savedTradeProduct, tradedQuantity);
                        tradeProductRepository.deleteById(tradeProductDto.getTradeProductId());
                    }
                } else if (tradeProductDto.getTradeProductId() == null) {
                    TradeProduct tradeProduct;
                    try {
                        tradeProduct = warehouseService.createOrEditTrade(branch, new TradeProduct(), tradeProductDto, trade.getId());
                    } catch (Exception e) {
                        return new ApiResponse("WAREHOUSE ERROR", false);
                    }
                    if (tradeProduct != null) {
                        tradeProduct.setTrade(trade);
                        try {
                            fifoCalculationService.createOrEditTradeProduct(branch, tradeProduct, tradeProduct.getTradedQuantity());
                        } catch (Exception e) {
                            return new ApiResponse("FIFO ERROR", false);
                        }
                        tradeProductList.add(tradeProduct);
                        profit += tradeProduct.getProfit();
                    }
                } else {
                    Optional<TradeProduct> optionalTradeProduct = tradeProductRepository.findById(tradeProductDto.getTradeProductId());
                    if (optionalTradeProduct.isEmpty()) continue;
                    TradeProduct tradeProduct = optionalTradeProduct.get();
                    if (tradeProduct.getTradedQuantity() == tradeProductDto.getTradedQuantity()) {
                        profit += tradeProduct.getProfit();
                        continue;
                    }
                    double difference = tradeProductDto.getTradedQuantity() - tradeProduct.getTradedQuantity();
                    tradeProduct = warehouseService.createOrEditTrade(branch, tradeProduct, tradeProductDto, trade.getId());
                    if (tradeProduct != null) {
                        if (difference > 0) {
                            fifoCalculationService.createOrEditTradeProduct(branch, tradeProduct, difference);
                        } else if (difference < 0) {
                            fifoCalculationService.returnedTrade(branch, tradeProduct, -difference);
                            if (tradeDTO.isBacking()) {
                                if (tradeProduct.getBacking() != null) {
                                    tradeProduct.setBacking(tradeProduct.getBacking() - difference);
                                } else {
                                    tradeProduct.setBacking(-difference);
                                }
                            }
                        }
                        tradeProductList.add(tradeProduct);
                        profit += tradeProduct.getProfit();
                    }
                }
            }
            trade.setTotalProfit(profit);
        } catch (Exception e) {
            return new ApiResponse("TRADE PRODUCT ERROR", false);
        }

        try {
            countKPI(optionalAgreementKpi.get(), trade, tradeProductList);
        } catch (Exception e) {
            return new ApiResponse("KPI ERROR", false);
        }
        tradeRepository.save(trade);
        tradeProductRepository.saveAll(tradeProductList);

        tradeProductList.forEach(tradeProduct -> {
            Map<String, Object> extra = Map.of(
                    "quantity", tradeProduct.getTradedQuantity(),
                    "total sale price", tradeProduct.getTotalSalePrice(),
                    "customer ", tradeProduct.getTrade().getCustomer() != null ? tradeProduct.getTrade().getCustomer().getName() : "null");
            productActivityLogger.logTrade(tradeProduct.getProduct().getId(), extra);
        });


        if (customerDebt.getDebtSum() != null) {
            customerDebt.setTrade(trade);
            customerDebtRepository.save(customerDebt);
            Optional<CustomerSupplier> optionalCustomerSupplier = customerSupplierRepository
                    .findByCustomerId(customerDebt.getCustomer().getId());

            optionalCustomerSupplier.ifPresent(customerSupplierService::calculation);

            if (tradeDTO.getCustomerCreditDto() != null) {
                setCustomerCredit(tradeDTO);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("invoice", trade.getInvoice());
        response.put("tradeId", trade.getId());
        response.put("message", "Savdo muvaffaqiyatli amalga oshirildi");

        if (trade.getCustomer() != null)
            response.put("customerDebt", trade.getCustomer().getDebt());

        return new ApiResponse("SUCCESS", true, response);
    }

    private boolean repaymentIsDollar(TradeDTO tradeDTO, PaymentDto paymentDto) {
        if (paymentDto.getIsDollar() != null && paymentDto.getIsDollar()) {
            return true;
        } else if (paymentDto.getIsDollar() == null) {
            return tradeDTO.getFirstPaymentIsDollar();
        }
        return false;
    }

    private Double sumOrDollar2(PaymentDto paymentDto, TradeDTO tradeDTO) {
        if (paymentDto.getIsDollar() != null && paymentDto.getIsDollar()) {
            return paymentDto.getPaidSum();
        } else if (paymentDto.getIsDollar() == null) {
            return tradeDTO.getPaidSumDollar();
        }
        return paymentDto.getPaidSumDollar();
    }

    private Double sumOrDollar(PaymentDto paymentDto) {
        if (paymentDto.getIsDollar() != null && paymentDto.getIsDollar()) {
            return paymentDto.getPaidSumDollar();
        }
        return paymentDto.getPaidSum();
    }

    @NotNull
    private static String telegramSendText(Trade trade, StringBuilder products) {
        String sendText = "";
        if (trade.getCustomer() != null) {
            sendText = "<b>#YANGI_SAVDO \uD83D\uDECD \n\n</b>" +
                    "<b>Filial: </b>" + trade.getBranch().getName() + "\n" +
                    "<b>Mijoz: </b>" + trade.getCustomer().getName() + "\n" +
                    "<b>Sotuvchi: </b>" + trade.getTrader().getFirstName() + "\n" +
                    "<b>To'lov usuli: </b>" + trade.getPayMethod().getType() + "\n\n" +
                    "<b>To'lov statusi: </b>" + trade.getPaymentStatus().getStatus() + "\n\n" +
                    "<b><i>MAHSULOTLAR \uD83D\uDCD1</i></b>\n\n" + products + "\n\n" +
                    "<b>Qolgan " + (trade.getCustomer().getDebt() < 0 ? "haqingiz" : "qarzingiz") + ": </b>" + Math.abs(trade.getCustomer().getDebt()) + "\n\n" +
                    "SAVDO SANASI: " + trade.getCreatedAt();
        } else {
            sendText = "<b>#YANGI_SAVDO \uD83D\uDECD \n\n</b>" +
                    "<b>Filial: </b>" + trade.getBranch().getName() + "\n" +
                    "<b>Mijoz: </b> Nomalum\n" +
                    "<b>Sotuvchi: </b>" + trade.getTrader().getFirstName() + "\n" +
                    "<b>To'lov usuli: </b>" + trade.getPayMethod().getType() + "\n\n" +
                    "<b>To'lov statusi: </b>" + trade.getPaymentStatus().getStatus() + "\n\n" +
                    "<b><i>MAHSULOTLAR \uD83D\uDCD1</i></b>\n\n" + products + "\n\n" +
                    "SAVDO SANASI: " + trade.getCreatedAt();
        }
        return sendText;
    }

    private void countKPI(Agreement agreementKpi, Trade trade, List<TradeProduct> tradeProductList) {
        double kpiProduct = countKPIProduct(tradeProductList);
        if (agreementKpi.getPrice() > 0 || kpiProduct > 0) {
            Double kpiD = trade.getKpi();
            double kpi = kpiD == null ? 0 : kpiD;
            double salarySum = trade.getTotalSum() * agreementKpi.getPrice() / 100;
            salaryCountService.add(new SalaryCountDto(
                    1,
                    salarySum + kpiProduct - kpi,
                    agreementKpi.getId(),
                    trade.getBranch().getId(),
                    new Date(),
                    "Savdo ulushi"
            ));
            trade.setKpi(salarySum + kpiProduct);
        }
    }

    private double countKPIProduct(List<TradeProduct> tradeProductList) {
        double kpi = 0;
        for (TradeProduct tradeProduct : tradeProductList) {
            kpi += countKPIProductHelper(tradeProduct.getProduct(), tradeProduct.getTradedQuantity(), tradeProduct.getTotalSalePrice());

        }
        return kpi;
    }

    private double countKPIProductHelper(Product product, double quantity, double totalPrice) {
        if (product.getKpi() != null & product.getKpiPercent() != null) {
            if (product.getKpiPercent()) {
                return totalPrice * product.getKpi() / 100;
            } else {
                return product.getKpi() * quantity;
            }
        }
        return 0;
    }

    public ApiResponse getOne(UUID id) {
        Trade trade = tradeRepository.findByIdWithDetails(id)
                .orElse(null);
        if (trade == null) return new ApiResponse(messageService.getMessage("not.found"), false);

        List<TradeProduct> tradeProducts = tradeProductRepository.findAllByTradeId(id);
        if (tradeProducts.isEmpty()) return new ApiResponse(messageService.getMessage("not.found"), false);

        Set<UUID> productIds = tradeProducts.stream()
                .map(tp -> tp.getProduct().getId())
                .collect(Collectors.toSet());

        List<Warehouse> warehouses = warehouseRepository.findAllByBranchIdAndProductIdIn(
                trade.getBranch().getId(), productIds
        );

        Map<UUID, Double> remainMap = warehouses.stream()
                .collect(Collectors.toMap(
                        w -> w.getProduct().getId(),
                        Warehouse::getAmount
                ));

        List<TradeProductDto> tradeProductDtoList = tradeProducts.stream().map(tp -> {
            TradeProductDto dto = new TradeProductDto();
            UUID productId = tp.getProduct().getId();
            dto.setTradeProductId(tp.getId());
            dto.setProductId(productId);
            dto.setProductName(tp.getProduct().getName());
            dto.setTradedQuantity(tp.getTradedQuantity());
            dto.setTotalSalePrice(tp.getTotalSalePrice());
            tp.setRemainQuantity(remainMap.getOrDefault(productId, 0d));
            return dto;
        }).collect(Collectors.toList());


        List<PaymentDto> paymentDtos = paymentMapper
                .toDtoList(paymentRepository.findAllByTradeId(id));


        TradeDTO dto = new TradeDTO();
        dto.setBacking(trade.getBacking());

        if (trade.getCustomer() != null) {
            dto.setCustomerId(trade.getCustomer().getId());
            dto.setCustomerName(trade.getCustomer().getName());
        }

        if (trade.getTrader() != null) {
            dto.setUserId(trade.getTrader().getId());
            dto.setUserName(trade.getTrader().getFirstName());
        }

        dto.setBranchId(trade.getBranch().getId());
        dto.setBranchName(trade.getBranch().getName());

        dto.setPaymentStatusId(trade.getPaymentStatus().getId());
        dto.setPaymentStatusName(trade.getPaymentStatus().getStatus());

        dto.setPayDate(trade.getPayDate());
        dto.setTotalSum(trade.getTotalSum());
        dto.setPaidSum(trade.getPaidSum());
        dto.setDebtSum(trade.getDebtSum());
        dto.setPaymentDtoList(paymentDtos);
        dto.setProductTraderDto(tradeProductDtoList);
        dto.setInvoice(trade.getInvoice());


        return new ApiResponse(messageService.getMessage("found"), true, dto);
    }

    @Transactional
    public ApiResponse delete(UUID tradeId) {
        Optional<Trade> optionalTrade = tradeRepository.findById(tradeId);
        if (optionalTrade.isEmpty()) return new ApiResponse("NOT FOUND", false);
        Trade trade = optionalTrade.get();
        if (!trade.isEditable()) return new ApiResponse("YOU CAN NOT DELETE AFTER 30 DAYS", false);
        int days = LocalDateTime.now().getDayOfYear() - trade.getCreatedAt().toLocalDateTime().getDayOfYear();
        if (days > 30) {
            trade.setEditable(false);
            return new ApiResponse("YOU CAN NOT DELETE AFTER 30 DAYS", false);
        }

        for (TradeProduct tradeProduct : tradeProductRepository.findAllByTradeId(tradeId)) {
            double amount = warehouseService.createOrEditWareHouseHelper(trade.getBranch(), tradeProduct.getProduct(), tradeProduct.getTradedQuantity());
            if (amount < tradeProduct.getTradedQuantity())
                fifoCalculationService.returnedTrade(trade.getBranch(), tradeProduct, tradeProduct.getTradedQuantity() - amount);
        }

        if (trade.getCustomer() != null) {
            Customer customer = trade.getCustomer();
            customer.setDebt(customer.getDebt() - trade.getTotalSum());
            customerRepository.save(customer);
            Optional<CustomerSupplier> optionalCustomerSupplier = customerSupplierRepository.findByCustomerId(customer.getId());
            optionalCustomerSupplier.ifPresent(customerSupplierService::calculation);
        }

        if (trade.getKpi() != null) {
            Optional<Agreement> optionalAgreementKpi = agreementRepository.findByUserIdAndSalaryStatus(trade.getTrader().getId(), SalaryStatus.KPI);
            if (optionalAgreementKpi.isPresent()) {
                double kpi = trade.getKpi();
                salaryCountService.add(new SalaryCountDto(
                        1,
                        -kpi,
                        optionalAgreementKpi.get().getId(),
                        trade.getBranch().getId(),
                        new Date(),
                        "deleted trade"
                ));
            }
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        historyRepository.save(new History(
                HistoryName.SAVDO,
                user,
                trade.getBranch(),
                trade.getInvoice() + AppConstant.DELETE_TRADE
        ));

        for (Payment payment : paymentRepository.findAllByTradeId(tradeId)) {
            balanceService.edit(trade.getBranch().getId(), payment.getPaidSum(), Boolean.FALSE, payment.getPayMethod().getId(), trade.getDollar().equals("DOLLAR"), "trade");
        }
        List<ProductAbout> productAbouts = productAboutRepository.findAllByTradeId(tradeId);
        for (ProductAbout productAbout : productAbouts) {
            productAbout.setUpdateAt(new Timestamp(System.currentTimeMillis()));
            productAbout.setDescription(ConstantProduct.D_TRADE);
            productAboutRepository.save(productAbout);
        }

        customerDebtRepository.deleteAllByTradeId(tradeId);
        tradeRepository.deleteById(tradeId);

        return new ApiResponse("SUCCESS", true);
    }

    public ApiResponse getAllByFilter(UUID id, String invoice, Boolean backing, int page, int size, Date startDate, Date endDate) {
        if (invoice != null && invoice.isBlank())
            invoice = null;
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Trade> tradePage;
        if (startDate == null || endDate == null) {
            if (businessRepository.existsById(id)) {
                if (invoice != null && backing != null) {
                    tradePage = tradeRepository.findAllByBranch_BusinessIdAndInvoiceContainingAndBackingOrBranch_BusinessIdAndCustomer_NameContainingIgnoreCaseAndBacking(id, invoice, backing, id, invoice, backing, pageable);
                } else if (invoice != null) {
                    tradePage = tradeRepository.findAllByBranch_BusinessIdAndInvoiceContainingOrBranch_BusinessIdAndCustomer_NameContainingIgnoreCase(id, invoice, id, invoice, pageable);
                } else if (backing != null) {
                    tradePage = tradeRepository.findAllByBranch_BusinessIdAndBacking(id, backing, pageable);
                } else {
                    tradePage = tradeRepository.findAllByBranch_BusinessId(id, pageable);
                }
            } else if (branchRepository.existsById(id)) {
                if (invoice != null && backing != null) {
                    tradePage = tradeRepository.findAllByBranchIdAndInvoiceContainingAndBackingOrBranchIdAndCustomer_NameContainingIgnoreCaseAndBacking(id, invoice, backing, id, invoice, backing, pageable);
                } else if (invoice != null) {
                    tradePage = tradeRepository.findAllByBranchIdAndInvoiceContainingOrBranchIdAndCustomer_NameContainingIgnoreCase(id, invoice, id, invoice, pageable);
                } else if (backing != null) {
                    tradePage = tradeRepository.findAllByBranchIdAndBacking(id, backing, pageable);
                } else {
                    tradePage = tradeRepository.findAllByBranchId(id, pageable);
                }
            } else {
                return new ApiResponse("ID ERROR", false);
            }
        } else {
            if (businessRepository.existsById(id)) {
                if (invoice != null && backing != null) {
                    tradePage = tradeRepository.findAllByBranch_BusinessIdAndInvoiceContainingAndBackingOrBranch_BusinessIdAndCustomer_NameContainingIgnoreCaseAndBackingAndCreatedAtBetween(id, invoice, backing, id, invoice, backing, startDate, endDate, pageable);
                } else if (invoice != null) {
                    tradePage = tradeRepository.findAllByBranch_BusinessIdAndInvoiceContainingOrBranch_BusinessIdAndCustomer_NameContainingIgnoreCaseAndCreatedAtBetween(id, invoice, id, invoice, startDate, endDate, pageable);
                } else if (backing != null) {
                    tradePage = tradeRepository.findAllByBranch_BusinessIdAndBackingAndCreatedAtBetween(id, backing, startDate, endDate, pageable);
                } else {
                    tradePage = tradeRepository.findAllByBranch_BusinessIdAndCreatedAtBetween(id, startDate, endDate, pageable);
                }
            } else if (branchRepository.existsById(id)) {
                if (invoice != null && backing != null) {
                    tradePage = tradeRepository.findAllByBranchIdAndInvoiceContainingAndBackingOrBranchIdAndCustomer_NameContainingIgnoreCaseAndBackingAndCreatedAtBetween(id, invoice, backing, id, invoice, backing, startDate, endDate, pageable);
                } else if (invoice != null) {
                    tradePage = tradeRepository.findAllByBranchIdAndInvoiceContainingOrBranchIdAndCustomer_NameContainingIgnoreCaseAndCreatedAtBetween(id, invoice, id, invoice, startDate, endDate, pageable);
                } else if (backing != null) {
                    tradePage = tradeRepository.findAllByBranchIdAndBackingAndCreatedAtBetween(id, backing, startDate, endDate, pageable);
                } else {
                    tradePage = tradeRepository.findAllByBranchIdAndCreatedAtBetween(id, startDate, endDate, pageable);
                }
            } else {
                return new ApiResponse("ID ERROR", false);
            }
        }
        return new ApiResponse("SUCCESS", true, tradePage);
    }

    public ApiResponse getAllByBusinessId(UUID businessId) {
        List<Trade> allByBusinessId = tradeRepository.findAllByBranch_Business_IdOrderByCreatedAtDesc(businessId);
        if (allByBusinessId.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, allByBusinessId);
    }

    public ApiResponse getAllByBranchId(UUID branchId) {
        List<Trade> allByBranchId = tradeRepository.findAllByBranch_IdOrderByCreatedAtDesc(branchId);
        if (allByBranchId.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        return new ApiResponse("found", true, allByBranchId);
    }

    public ApiResponse getTradeByTrader(UUID branchId) {

        List<TradeProduct> tradeList = tradeProductRepository.findAllByTrade_BranchId(branchId);
        if (tradeList.isEmpty()) {
            return new ApiResponse("Not Found", false);
        }

        Map<UUID, Double> traderQuantities = new HashMap<>();

        for (TradeProduct tradeProduct : tradeList) {
            UUID traderId = tradeProduct.getTrade().getTrader().getId();
            Double quantity = traderQuantities.getOrDefault(traderId, 0.0);
            quantity += tradeProduct.getTradedQuantity();
            traderQuantities.put(traderId, quantity);
        }

        List<TraderDto> traderDtoList = new ArrayList<>();

        for (Map.Entry<UUID, Double> entry : traderQuantities.entrySet()) {
            UUID traderId = entry.getKey();
            Optional<User> optionalUser = userRepository.findById(traderId);
            UUID photoId = null;
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                photoId = user.getPhoto().getId();
            }
            String traderName = tradeRepository.getTraderNameById(traderId);
            Double quantitySold = entry.getValue();
            if (photoId != null) {
                traderDtoList.add(new TraderDto(traderId, photoId, traderName, quantitySold));
            } else {
                traderDtoList.add(new TraderDto(traderId, traderName, quantitySold));
            }
        }
        List<TraderDto> sortedTraders = traderDtoList.stream()
                .sorted(Comparator.comparingDouble(TraderDto::getQuantitySold).reversed())
                .toList();

        return new ApiResponse("Found", true, sortedTraders);
    }

    public ApiResponse getBacking(UUID branchId) {
        if (!branchRepository.existsById(branchId))
            return new ApiResponse("BRANCH NOT FOUND", false);
        List<TradeProduct> tradeProductList = tradeProductRepository.findAllByTrade_BranchIdAndBackingIsNotNull(branchId);
        if (tradeProductList.isEmpty())
            return new ApiResponse("BACKING PRODUCT NOT FOUND", false);
        return new ApiResponse("SUCCESS", true, toProductBackingDtoMap(tradeProductList));
    }

    private Collection<ProductBackingDto> toProductBackingDtoMap(List<TradeProduct> tradeProductList) {
        Map<UUID, ProductBackingDto> map = new HashMap<>();
        UUID id;
        String name;
        String measurement;
        double quantity;
        for (TradeProduct t : tradeProductList) {
            id = t.getProduct().getId();
            name = t.getProduct().getName();
            measurement = t.getProduct().getMeasurement().getName();

            quantity = t.getBacking();
            ProductBackingDto dto = map.getOrDefault(id, new ProductBackingDto(
                    id,
                    name,
                    measurement,
                    0
            ));
            dto.setQuantity(dto.getQuantity() + quantity);
            map.put(id, dto);
        }
        return map.values();
    }

    public ApiResponse getBackingByProduct(UUID branchId, UUID productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TradeProduct> tradeProductPage;
        tradeProductPage = tradeProductRepository.findAllByTrade_BranchIdAndProductIdAndBackingIsNotNullOrderByCreatedAtDesc(branchId, productId, pageable);

        if (tradeProductPage.isEmpty())
            return new ApiResponse("BACKING PRODUCT NOT FOUND", false);

        Map<String, Object> response = new HashMap<>();
        response.put("getLessProduct", toGroductBackingGetDtoList(tradeProductPage.getContent()));
        response.put("currentPage", tradeProductPage.getNumber());
        response.put("totalItem", tradeProductPage.getTotalElements());
        response.put("totalPage", tradeProductPage.getTotalPages());
        return new ApiResponse("SUCCESS", true, response);
    }

    private List<ProductBackingGetDto> toGroductBackingGetDtoList(List<TradeProduct> tradeProductList) {
        List<ProductBackingGetDto> list = new ArrayList<>();
        for (TradeProduct t : tradeProductList) {
            ProductBackingGetDto dto = new ProductBackingGetDto(
                    t.getCreatedAt(),
                    t.getBacking()
            );
            if (t.getTrade().getCustomer() != null)
                dto.setCustomerName(t.getTrade().getCustomer().getName());
            list.add(dto);
        }
        return list;
    }

    /// new function
    public ApiResponse getAllTreade(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ApiResponse("User not found", false, null);
        }

        List<DataProjection> tradeProductList = tradeRepository.findAllByuserId(user.getId());
        List<DataProjection> typeManyProducts = tradeRepository.findAllTradeTypeManyProductByUserId(user.getId());

        List<DataDto> list1 = getDataDtos(tradeProductList);
        List<DataDto> list2 = getDataDtos(typeManyProducts);
        List<DataDto> combinedList = new LinkedList<>();
        combinedList.addAll(list1);
        combinedList.addAll(list2);
        List<DataDto> separateList = new LinkedList<>(combinedList);

// Yangi alohida list
        return new ApiResponse("Found", true, separateList);
    }

    public ApiResponse getAllTreadeAndSearch(UUID userId, Date startDate, Date endDate) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ApiResponse("User not found", false, null);
        }

        List<DataProjection> tradeProductList = tradeRepository.findAllByUserIdAndSearch(user.getId(), startDate, endDate);
        List<DataProjection> typeManyProducts = tradeRepository.findAllTradeTypeManyAndSearch(user.getId(), startDate, endDate);

        List<DataDto> list1 = getDataDtos(tradeProductList);
        List<DataDto> list2 = getDataDtos(typeManyProducts);
        List<DataDto> combinedList = new LinkedList<>();
        combinedList.addAll(list1);
        combinedList.addAll(list2);
        List<DataDto> separateList = new LinkedList<>(combinedList);

// Yangi alohida list
        return new ApiResponse("Found", true, separateList);
    }

    private List<DataDto> getDataDtos(List<DataProjection> typeManyProducts) {
        Map<String, DataDto> productMap = new HashMap<>();

        for (DataProjection data : typeManyProducts) {
            String productName = data.getProductName();
            String branchName = data.getBranchName();
            Timestamp time = data.getCreatedDate();
            Integer tradedQuantity = data.getTreaderQuantity();
            Integer kpi = data.getKpi();

            String key = productName + "-" + branchName;

            if (!productMap.containsKey(key)) {
                DataDto newData = new DataDto();
                newData.setProductName(productName);
                newData.setBranchName(branchName);
                newData.setAmount(tradedQuantity);
                newData.setTime(time);
                newData.setKpi(kpi);

                productMap.put(key, newData);
            } else {
                DataDto existingData = productMap.get(key);
                existingData.setAmount((existingData.getAmount() == null ? 0 : existingData.getAmount()) + (tradedQuantity == null ? 0 : tradedQuantity));
                existingData.setKpi((existingData.getKpi() == null ? 0 : existingData.getKpi()) + (kpi == null ? 0 : kpi));
            }
        }

        List<DataDto> separateList = new ArrayList<>(productMap.values());
        return separateList;
    }

    public HttpEntity<?> getAllCalculationSumma(UUID branchId, Date startDate, Date endDate, UUID businessId, Boolean isDollar) {
        if (startDate == null || endDate == null) {
            Map<String, Object> data = new HashMap<>();
            if (isDollar) {
                List<TradeInfoResult> results = new LinkedList<>();
                double totalDollar = 0;
                double totalSum = 0;
                List<DebtCanculs> allByTradeBranchId = debtCanculsRepository.findAllByTradeBranch_Id(branchId);
                for (DebtCanculs debtCanculs : allByTradeBranchId) {
                    totalDollar = totalDollar + debtCanculs.getDollarPrice();
                    totalSum = totalSum + debtCanculs.getDebtPrice();
                }
                Double plusDollarAmount = tradeRepository.findAllAmountDollarDifferentPayment(payMethodRepository.findByType("Naqd").orElseThrow().getId(), branchId);
                results.add(new TradeInfoResult("Naqd", totalDollar + (plusDollarAmount == null ? 0 : plusDollarAmount)));
                data.put("data", results);
                data.put("totalAmount", totalSum);
                data.put("dollar", true);
            } else {
                Double nationSum = tradeRepository.findAllNationTradeSum(paymentStatusRepository.findByStatus(StatusName.QISMAN_TOLANGAN.toString()).getId(), paymentStatusRepository.findByStatus(StatusName.TOLANMAGAN.toString()).getId(), branchId);
                List<TradeInfoResult> results = new LinkedList<>();
                List<TradeInfoResult> results2 = new LinkedList<>();

                double total = 0;
                for (PaymentMethod paymentMethod : payMethodRepository.findAll()) {
                    double s = 0;
                    for (Trade trade : tradeRepository.findAllByBranch_IdAndPayMethodIdAndDollarTradeIsFalseAndDifferentPaymentIsFalse(branchId, paymentMethod.getId())) {
                        s = s + trade.getPaidSum();
                    }
                    total = total + s;
                    Double plusSumAmount = tradeRepository.findAllAmountSumDifferentPayment(paymentMethod.getId(), branchId);

                    results.add(new TradeInfoResult(paymentMethod.getType(), (s + (plusSumAmount == null ? 0 : plusSumAmount))));
                }
                for (PaymentMethod paymentMethod : payMethodRepository.findAll()) {
                    Double amount = repaymentDebtRepository.findAllDebtAmountByPayMethodIdAndBranchId(paymentMethod.getId(), branchId);
                    results2.add(new TradeInfoResult(paymentMethod.getType(), amount == null ? 0 : amount));
                }
                data.put("data", results);
                data.put("data2", results2);
                data.put("totalAmount", total);
                data.put("dollar", false);
                data.put("nationTradeTotalSum", nationSum == null ? 0 : nationSum);
            }

            return ResponseEntity.ok(data);
        } else {
            Map<String, Object> data = new HashMap<>();

            if (isDollar) {
                List<TradeInfoResult> results = new LinkedList<>();
                double totalDollar = 0;
                double totalSum = 0;
                List<DebtCanculs> allByTradeBranchId = debtCanculsRepository.findAllByTradeBranch_IdAndCreatedAtBetween(branchId, startDate, endDate);
                for (DebtCanculs debtCanculs : allByTradeBranchId) {
                    totalDollar = totalDollar + debtCanculs.getDollarPrice();
                    totalSum = totalSum + debtCanculs.getDebtPrice();
                }
                Double plusDollarAmount = tradeRepository.findAllAmountDollarDifferentPaymentAndSearchDate(payMethodRepository.findByType("Naqd").orElseThrow().getId(), branchId, startDate, endDate);
                results.add(new TradeInfoResult("Naqd", totalDollar + (plusDollarAmount == null ? 0 : plusDollarAmount)));
                data.put("data", results);
                data.put("totalAmount", totalSum);
                data.put("dollar", true);
            } else {
                Double nationSum = tradeRepository.findAllNationTradeSumByDate(paymentStatusRepository.findByStatus(StatusName.QISMAN_TOLANGAN.toString()).getId(), paymentStatusRepository.findByStatus(StatusName.TOLANMAGAN.toString()).getId(), branchId, startDate, endDate);
                List<TradeInfoResult> results = new LinkedList<>();
                List<TradeInfoResult> results2 = new LinkedList<>();
                double total = 0;
                for (PaymentMethod paymentMethod : payMethodRepository.findAll()) {
                    double s = 0;
                    for (Trade trade : tradeRepository.findAllByBranch_IdAndPayMethodIdAndDollarTradeIsFalseAndDifferentPaymentIsFalseAndCreatedAtBetween(branchId, paymentMethod.getId(), startDate, endDate)) {
                        s = s + trade.getPaidSum();
                    }
                    total = total + s;
                    Double plusSumAmount = tradeRepository.findAllAmountSumDifferentPaymentAndSearchDate(paymentMethod.getId(), branchId, startDate, endDate);

                    results.add(new TradeInfoResult(paymentMethod.getType(), (s + (plusSumAmount == null ? 0 : plusSumAmount))));
                }
                for (PaymentMethod paymentMethod : payMethodRepository.findAll()) {
                    Double amount = repaymentDebtRepository.findAllDebtAmountByPayMethodIdAndBranchIdAndDate(paymentMethod.getId(), branchId, startDate, endDate);
                    results2.add(new TradeInfoResult(paymentMethod.getType(), amount == null ? 0 : amount));
                }
                data.put("data", results);
                data.put("data2", results2);
                data.put("totalAmount", total);
                data.put("dollar", false);
                data.put("nationTradeTotalSum", nationSum == null ? 0 : nationSum);
            }
            return ResponseEntity.ok(data);
        }
    }

    private void setCustomerCredit(TradeDTO tradeDTO) {
        CustomerCreditDto customerCreditDto = tradeDTO.getCustomerCreditDto();
        double paymentAmount = tradeDTO.getCustomerCreditDto().getTotalAmount() / tradeDTO.getCustomerCreditDto().getMonth();
        for (int i = 1; i <= customerCreditDto.getMonth(); i++) {
            CustomerCredit customerCredit = new CustomerCredit();
            customerCredit.setAmount(paymentAmount);
            customerCredit.setComment(tradeDTO.getCustomerCreditDto().getComment() != null ? tradeDTO.getCustomerCreditDto().getComment() : "");
            customerCredit.setPaymentDate(tradeDTO.getCustomerCreditDto().getPaymentDate().plusMonths(i));
            Optional<Customer> optionalCustomer =
                    customerRepository.findById(customerCreditDto.getCustomerId());
            optionalCustomer.ifPresent(customerCredit::setCustomer);
            customerCreditRepository.save(customerCredit);
        }
    }
}
