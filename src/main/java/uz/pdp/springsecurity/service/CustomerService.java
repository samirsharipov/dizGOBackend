package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.StatusName;
import uz.pdp.springsecurity.mapper.CustomerMapper;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.payload.projections.InActiveUserProjection;
import uz.pdp.springsecurity.payload.projections.MonthProjection;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.utils.Constants;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerGroupRepository customerGroupRepository;
    private final CustomerRepository customerRepository;
    private final BranchRepository branchRepository;
    private final CustomerMapper mapper;
    private final TradeRepository tradeRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final BalanceService balanceService;
    private final RepaymentDebtRepository repaymentDebtRepository;
    private final CustomerDebtRepository customerDebtRepository;
    private final PayMethodRepository payMethodRepository;
    private final TradeProductRepository tradeProductRepository;
    private final CustomerDebtRepaymentRepository customerDebtRepaymentRepository;
    private final CustomerSupplierRepository customerSupplierRepository;
    private final CustomerSupplierService customerSupplierService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse add(CustomerDto customerDto) {
        return createEdit(new Customer(), customerDto);
    }

    public ApiResponse edit(UUID id, CustomerDto customerDto) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.map(customer -> createEdit(customer, customerDto)).orElseGet(() -> new ApiResponse("CUSTOMER NOT FOUND ", false));
    }

    private ApiResponse createEdit(Customer customer, CustomerDto customerDto) {
        List<Branch> branches = branchRepository.findAllById(customerDto.getBranches());
        if (branches.isEmpty())
            return new ApiResponse("BRANCH NOT FOUND", false);
        customer.setBranches(branches);
        if (customerDto.getCustomerGroupId() != null) {
            Optional<CustomerGroup> optionalCustomerGroup = customerGroupRepository.findById(customerDto.getCustomerGroupId());
            optionalCustomerGroup.ifPresent(customer::setCustomerGroup);
        } else {
            customer.setCustomerGroup(null);
        }
        String phoneNumber = customerDto.getPhoneNumber();
        phoneNumber = phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
        phoneNumber = phoneNumber.replaceAll(" ", "");
        customer.setName(customerDto.getName());
        customer.setTelegram(customerDto.getTelegram());
        customer.setBirthday(customerDto.getBirthday());
        customer.setDebt(customerDto.getDebt());
        customer.setPayDate(customerDto.getPayDate());
        customer.setLidCustomer(customerDto.getLidCustomer());
        customer.setDescription(customerDto.getDescription());
        customer.setAddress(customerDto.getAddress()); // Yangi maydon
        customer.setLatitude(customerDto.getLatitude());
        customer.setLongitude(customerDto.getLongitude());
        customer.setPassword(passwordEncoder.encode("password"));

        customer.setBusiness(branches.get(0).getBusiness()); // TODO: 6/6/2023  delete
        customer.setBranch(branches.get(0)); // TODO: 6/6/2023  delete

        Customer save = customerRepository.save(customer);
        return new ApiResponse("SUCCESS", true, save);
    }


    @Transactional
    public ApiResponse createCustomer(CustomerRegisterDto customerDto) {
        try {

            Role customerRole = roleRepository.findByName("Customer")
                    .orElseThrow(() -> new RuntimeException("Customer role not found"));

            User user = new User();

            if (customerDto.getUserId() != null) {
                Optional<User> optionalUser = userRepository.findById(customerDto.getUserId());
                if (optionalUser.isEmpty()) {
                    return new ApiResponse("User does not exist", false);
                }
                user = optionalUser.get();
            } else {
                // 3. Yangi User yaratish
                user.setFirstName(customerDto.getFirstName());
                user.setLastName(customerDto.getLastName());

                user.setUsername(customerDto.getPhoneNumber());
                user.setPassword(passwordEncoder.encode(customerDto.getPassword()));

                user.setPhoneNumber(customerDto.getPhoneNumber());
                user.setRole(customerRole);
                user.setEnabled(true); // Akkaunt aktiv
                user.setActive(true); // Akkaunt faol
                userRepository.save(user);
            }

            if (customerDto.getUserId() == null) {
                Optional<User> optionalUser = userRepository.findByPhoneNumber(customerDto.getPhoneNumber());
                if (optionalUser.isPresent()) {
                    user = optionalUser.get();
                }
            }

            // 4. Customer yaratish va User bilan bog'lash
            Customer customer = new Customer();
            customer.setName(customerDto.getFirstName());
            customer.setPhoneNumber(customerDto.getPhoneNumber());
            customer.setUsername(customerDto.getPhoneNumber());
            customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
            try {
                customer.setUniqueCode(generateCode());
            } catch (Exception e) {
                customer.setUniqueCode(generateCode());
            }
            customer.setUser(user); // User bilan bog'lash
            customer.setActive(true); // Faol holatda

            Optional<CustomerGroup> optionalCustomerGroup = customerGroupRepository.findByName("defaultCustomerGroup");
            optionalCustomerGroup.ifPresent(customer::setCustomerGroup);

            customerRepository.save(customer);

            return new ApiResponse("Customer successfully created", true);

        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Error while creating customer", false);
        }
    }


    public ApiResponse get(UUID id) {
        Optional<Customer> optional = customerRepository.findById(id);
        if (optional.isEmpty()) {
            return new ApiResponse("NOT FOUND", true);
        }
        CustomerDto customerDto = mapper.toDto(optional.get());
        List<UUID> branches = new ArrayList<>();
        for (Branch branch : optional.get().getBranches()) {
            branches.add(branch.getId());
        }
        customerDto.setBranches(branches);
        return new ApiResponse("FOUND", true, customerDto);
    }

    public ApiResponse delete(UUID id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        Customer customer = optionalCustomer.get();
        try {
            List<CustomerDebt> all = customerDebtRepository.findByCustomer_Id(id);
            for (CustomerDebt customerDebt : all) {
                customerDebt.setDelete(true);
                customerDebtRepository.save(customerDebt);
            }
            List<RepaymentDebt> allByCustomerId = repaymentDebtRepository.findAllByCustomer_Id(id);
            for (RepaymentDebt repaymentDebt : allByCustomerId) {
                repaymentDebt.setDelete(true);
                repaymentDebtRepository.save(repaymentDebt);
            }
            customer.setActive(false);
            customerRepository.save(customer);
        } catch (Exception e) {
            return new ApiResponse("Qarzi bor yoki savdo qilgan mijozni o'chirib bo'lmaydi!", false);
        }
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getAllByBusinessId(UUID businessId) {
        List<Customer> customerList = customerRepository.findAllByBusiness_IdAndActiveIsTrueOrBusiness_IdAndActiveIsNull(businessId, businessId);
        if (customerList.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, toCustomerDtoList(customerList));
    }

    public ApiResponse getTopCustomers(UUID businessId) {
        List<Customer> customerList = customerRepository.findAllByBusiness_IdAndActiveIsTrueOrBusiness_IdAndActiveIsNull(businessId, businessId);
        List<CustomerInfoDto> data = new LinkedList<>();
        for (Customer customer : customerList) {
            CustomerInfoDto customerInfoDto = new CustomerInfoDto();
            CustomerDto customerDto = mapper.toDto(customer);
            customerInfoDto.setCustomerDto(customerDto);
            Double totalSumByCustomer = tradeRepository.totalSumByCustomer(customer.getId());
            Double totalProfitByCustomer = tradeRepository.totalProfitByCustomer(customer.getId());
            customerInfoDto.setTotalTradeSum(totalSumByCustomer != null ? totalSumByCustomer : 0);
            customerInfoDto.setTotalProfitSum(totalProfitByCustomer != null ? totalProfitByCustomer : 0);
            data.add(customerInfoDto);
        }
        List<CustomerInfoDto> sortedList = data.stream()
                .sorted(Comparator.comparingDouble(CustomerInfoDto::getTotalTradeSum).reversed())
                .toList();

        // Getting the top 5 elements from the sorted list
        List<CustomerInfoDto> top5List = sortedList.stream()
                .limit(5)
                .toList();
        return new ApiResponse("", true, top5List);
    }

    public ApiResponse getTopCustomers100(UUID businessId) {
        List<Customer> customerList = customerRepository.findAllByBusiness_IdAndActiveIsTrueOrBusiness_IdAndActiveIsNull(businessId, businessId);
        List<CustomerInfoDto> data = new LinkedList<>();
        for (Customer customer : customerList) {
            CustomerInfoDto customerInfoDto = new CustomerInfoDto();
            CustomerDto customerDto = mapper.toDto(customer);
            customerInfoDto.setCustomerDto(customerDto);
            Double totalSumByCustomer = tradeRepository.totalSumByCustomer(customer.getId());
            Double totalProfitByCustomer = tradeRepository.totalProfitByCustomer(customer.getId());
            customerInfoDto.setTotalTradeSum(totalSumByCustomer != null ? totalSumByCustomer : 0);
            customerInfoDto.setTotalProfitSum(totalProfitByCustomer != null ? totalProfitByCustomer : 0);
            data.add(customerInfoDto);
        }
        List<CustomerInfoDto> sortedList = data.stream()
                .sorted(Comparator.comparingDouble(CustomerInfoDto::getTotalTradeSum).reversed())
                .toList();

        // Getting the top 5 elements from the sorted list
        List<CustomerInfoDto> top5List = sortedList.stream()
                .limit(100)
                .toList();
        return new ApiResponse("", true, top5List);
    }

    private List<CustomerDto> toCustomerDtoList(List<Customer> customerList) {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        for (Customer customer : customerList) {
            CustomerDto customerDto = mapper.toDto(customer);
            List<UUID> branches = new ArrayList<>();
            for (Branch branch : customer.getBranches()) {
                branches.add(branch.getId());
            }
            customerDto.setBranches(branches);
            customerDtoList.add(customerDto);
        }
        return customerDtoList;
    }

    public ApiResponse getAllByBranchId(UUID branchId) {
        List<Customer> customerList = customerRepository.findAllByBranchesIdAndActiveIsTrueOrBranchesIdAndActiveIsNull(branchId, branchId);
        if (customerList.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, toCustomerDtoList(customerList));
    }

    public ApiResponse repayment(UUID id, RepaymentDto repaymentDto) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) return new ApiResponse("CUSTOMER NOT FOUND", false);
        if (repaymentDto.getPayDate() == null) return new ApiResponse("PAY_DATE NOT FOUND", false);
        Customer customer = optionalCustomer.get();
        if (repaymentDto.getTotalPaidSum() != null) {
            customer.setDebt(customer.getDebt() - repaymentDto.getTotalPaidSum());
            customer.setPayDate(repaymentDto.getPayDate());
            customerRepository.save(customer);
            Optional<CustomerSupplier> optionalCustomerSupplier = customerSupplierRepository.findByCustomerId(customer.getId());
            optionalCustomerSupplier.ifPresent(customerSupplierService::calculation);
            try {

                repaymentHelper(repaymentDto.getTotalPaidSum(), customer, repaymentDto.getPaymentMethodId(), repaymentDto.getPayDate(), 0.0, false
                        , repaymentDto.getDescription());

                balanceService.edit(customer.getBranch().getId(), repaymentDto.getTotalPaidSum(), true, repaymentDto.getPaymentMethodId(), false, "customer");

                UUID paymentMethodId = repaymentDto.getPaymentMethodId();
                Optional<PaymentMethod> optionalPaymentMethod = payMethodRepository.findById(paymentMethodId);
                optionalPaymentMethod.ifPresent(paymentMethod ->
                        repaymentDebtRepository.save(
                                new RepaymentDebt(
                                        customer,
                                        repaymentDto.getTotalPaidSum(),
                                        0.0,
                                        false,
                                        paymentMethod,
                                        repaymentDto.getDescription(),
                                        false,
                                        repaymentDto.getPayDate())));

                try {
                    if (customer.getChatId() != null) {
                        String text = "<b>#YANGI_TOLOV</b>\n\n" +
                                "<b>MIJOZ: </b>" + customer.getName() + "\n" +
                                "<b>TO'LOV SUMMASI: </b>" + repaymentDto.getTotalPaidSum() + "UZS" + "\n" +
                                "<b>TO'LOV TURI: </b>" + optionalPaymentMethod.get().getType() + "\n\n" +
                                "<b>HOZIRGI " + (customer.getDebt() < 0 ? "HAQINGIZ" : "QARZINGIZ") + "</b>: " + (customer.getDebt() < 0 ? Math.abs(customer.getDebt()) : customer.getDebt());
//                        SendMessage sendMessage = SendMessage
//                                .builder()
//                                .chatId(customer.getChatId())
//                                .text(text)
//                                .parseMode(ParseMode.HTML)
//                                .build();
//                        RestTemplate restTemplate = new RestTemplate();
//                        restTemplate.postForObject("https://api.telegram.org/bot" + Constants.CUSTOMER_BOT_TOKEN + "/sendMessage", sendMessage, Object.class);
                    }
                } catch (Exception e) {
                    return new ApiResponse("telegram bot send message error", true);
                }

                return new ApiResponse("Repayment Customer !", true);

            } catch (Exception e) {
                return new ApiResponse(e.getMessage(), false);
            }
        } else {
            return new ApiResponse("brat qarzingiz null kelyabdi !", false);
        }
    }

    private void repaymentHelper(double paidSum, Customer customer, UUID paymentMethodId, Timestamp payDate, Double dollar, Boolean isDollar, String description) {
        Optional<PaymentMethod> optionalPaymentMethod = payMethodRepository.findById(paymentMethodId);
        CustomerDebtRepayment customerDebtRepayment = new CustomerDebtRepayment();
        customerDebtRepayment.setCustomer(customer);
        customerDebtRepayment.setPayDate(payDate);
        customerDebtRepayment.setPaidSum(paidSum);
        customerDebtRepayment.setDescription(description);
        customerDebtRepayment.setPaidSumDollar(dollar);
        customerDebtRepayment.setDollarRepayment(isDollar);
        optionalPaymentMethod.ifPresent(customerDebtRepayment::setPaymentMethod);
        customerDebtRepaymentRepository.save(customerDebtRepayment);


        PaymentStatus tolangan = paymentStatusRepository.findByStatus(StatusName.TOLANGAN.name());
        PaymentStatus qisman_tolangan = paymentStatusRepository.findByStatus(StatusName.QISMAN_TOLANGAN.name());
        List<Trade> tradeList = tradeRepository.findAllByCustomerIdAndDebtSumIsNotOrderByCreatedAtAsc(customer.getId(), 0d);
        for (Trade trade : tradeList) {
            List<Payment> paymentList = paymentRepository.findAllByTradeId(trade.getId());
            Payment payment = paymentList.get(0);
            if (paidSum > trade.getDebtSum()) {
                paidSum -= trade.getDebtSum();
                trade.setDebtSum(0);
                trade.setPaidSum(trade.getTotalSum());
                trade.setPaymentStatus(tolangan);
                payment.setPaidSum(payment.getPaidSum() + trade.getDebtSum());
                paymentRepository.save(payment);
            } else if (paidSum == trade.getDebtSum()) {
                trade.setDebtSum(0);
                trade.setPaidSum(trade.getTotalSum());
                trade.setPaymentStatus(tolangan);
                payment.setPaidSum(payment.getPaidSum() + trade.getDebtSum());
                paymentRepository.save(payment);
                break;
            } else {
                trade.setDebtSum(trade.getDebtSum() - paidSum);
                trade.setPaidSum(trade.getPaidSum() + paidSum);
                trade.setPaymentStatus(qisman_tolangan);
                payment.setPaidSum(payment.getPaidSum() + paidSum);
                paymentRepository.save(payment);
                break;
            }

        }
        tradeRepository.saveAll(tradeList);
    }

    public ApiResponse getAllByGroupId(UUID groupId) {
        List<Customer> customerList = customerRepository.findAllByCustomerGroupIdAndActiveIsTrueOrCustomerGroupIdAndActiveIsNull(groupId, groupId);
        if (customerList.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        return new ApiResponse("all customers", true, toCustomerDtoList(customerList));
    }

    public ApiResponse getAllByLidCustomer(UUID branchId) {
        List<Customer> customerList = customerRepository.findAllByBranchesIdAndLidCustomerIsTrueAndActiveIsTrueOrBranchesIdAndLidCustomerIsTrueAndActiveIsNull(branchId, branchId);
        if (customerList.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        List<CustomerDto> customerDtoList = toCustomerDtoList(customerList);
        List<Map<String, Object>> responses = new ArrayList<>();
        double totalTrade = 0;

        for (CustomerDto customerDto : customerDtoList) {
            Map<String, Object> response = new HashMap<>();
            List<Trade> allByCustomerId = tradeRepository.findAllByCustomer_Id(customerDto.getId());
            double totalSumma = 0;
            double profit = 0;
            for (Trade trade : allByCustomerId) {
                totalSumma += trade.getTotalSum();
                profit += trade.getTotalProfit();
            }
            totalTrade += totalSumma;
            response.put("customer", customerDto);
            response.put("totalSumma", totalSumma);
            response.put("size", customerList.size());
            response.put("totalTrade", totalTrade);
            response.put("profit", profit);
            responses.add(response);
        }
        return new ApiResponse("found", true, responses);
    }

    public ApiResponse getCustomerInfo(UUID customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            return new ApiResponse("not found customer", false);
        }

        CustomerInfoDto customerInfoDto = new CustomerInfoDto();
        Customer customer = optionalCustomer.get();
        CustomerDto customerDto = mapper.toDto(customer);
        customerInfoDto.setCustomerDto(customerDto);
        Double totalSumByCustomer = tradeRepository.totalSumByCustomer(customerId);
        Double totalProfitByCustomer = tradeRepository.totalProfitByCustomer(customerId);
        customerInfoDto.setTotalTradeSum(totalSumByCustomer != null ? totalSumByCustomer : 0);
        customerInfoDto.setTotalProfitSum(totalProfitByCustomer != null ? totalProfitByCustomer : 0);
        customerDto.setLatitude(customer.getLatitude());
        customerDto.setLongitude(customer.getLongitude());
        return new ApiResponse("data", true, customerInfoDto);
    }

    public ApiResponse getCustomerTradeInfo(UUID customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            return new ApiResponse("not found customer", false);
        }

        List<CustomerTradeInfo> customerTradeInfo = new ArrayList<>();
        List<Trade> all = tradeRepository.findAllByCustomer_Id(customerId);
        for (Trade trade : all) {
            CustomerTradeInfo customerTradeInfo1 = new CustomerTradeInfo();
            List<TradeProductCustomerDto> tradeProductCustomerDtoList = new ArrayList<>();

            List<TradeProduct> allByTradeId = tradeProductRepository.findAllByTradeId(trade.getId());

            for (TradeProduct tradeProduct : allByTradeId) {
                TradeProductCustomerDto productCustomerDto = new TradeProductCustomerDto();

                if (tradeProduct.getBacking() != null) {
                    customerTradeInfo1.setTrade(false);
                    customerTradeInfo1.setTotalSumma(tradeProduct.getBacking());
                } else {
                    customerTradeInfo1.setTrade(true);
                    customerTradeInfo1.setTotalSumma(tradeProduct.getTrade().getTotalSum());
                }

                if (tradeProduct.getProduct().getPhoto() != null) {
                    productCustomerDto.setAttachmentId(tradeProduct.getProduct().getPhoto().getId());
                }


                productCustomerDto.setProductName(tradeProduct.getProduct().getName());

                productCustomerDto.setProductCount(tradeProduct.getTradedQuantity());

                //measurementni nameni olish uchun product type price ni null likga tekshirildi!
                productCustomerDto.setMeasurementName(tradeProduct.getProduct().getMeasurement().getName());

                tradeProductCustomerDtoList.add(productCustomerDto);
            }

            if (customerTradeInfo1.getTotalSumma() != null) {
                customerTradeInfo1.setCreateAt(trade.getCreatedAt());
                customerTradeInfo1.setProductCutomerDtoList(tradeProductCustomerDtoList);
                customerTradeInfo.add(customerTradeInfo1);
            }

        }

        List<CustomerDebtRepayment> customerDebtRepaymentList = customerDebtRepaymentRepository.findAllByCustomer_Id(customerId);

        for (CustomerDebtRepayment customerDebtRepayment : customerDebtRepaymentList) {

            CustomerTradeInfo customerTradeInfo2 = new CustomerTradeInfo();
            customerTradeInfo2.setCreateAt(customerDebtRepayment.getPayDate() != null ? customerDebtRepayment.getPayDate() : customerDebtRepayment.getCreatedAt());
            customerTradeInfo2.setTotalSumma(customerDebtRepayment.getPaidSum());
            customerTradeInfo2.setPaid(true);
            customerTradeInfo.add(customerTradeInfo2);
        }

        if (customerTradeInfo.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        customerTradeInfo.sort(Comparator.comparing(CustomerTradeInfo::getCreateAt).reversed());

        return new ApiResponse("all", true, customerTradeInfo);
    }

    public ApiResponse getCustomerPreventedInfo(UUID customerId) {

        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            return new ApiResponse("not found customer", false);
        }

        Customer customer = optionalCustomer.get();
        List<Trade> all = tradeRepository.findAllByCustomer_Id(customerId);
        List<CustomerPreventedInfoDto> customerPreventedInfoDtoList = new ArrayList<>();

        for (Trade trade : all) {
            CustomerPreventedInfoDto customerPreventedInfoDto = new CustomerPreventedInfoDto();
            Double paidSum = tradeRepository.totalPaidSum(trade.getId());
            TotalPaidSumDto totalPaidSumDto = new TotalPaidSumDto();
            totalPaidSumDto.setCreateAt(trade.getCreatedAt());
            totalPaidSumDto.setPaidSum(paidSum != null ? paidSum : 0);
            totalPaidSumDto.setPayMethodName(trade.getPayMethod().getType());
            customerPreventedInfoDto.setTotalPaidSumDto(totalPaidSumDto);

            if (trade.getDebtSum() != 0) {
                customerPreventedInfoDto.setDebtSum(trade.getDebtSum());
            }

            List<TradeProduct> allByTradeId = tradeProductRepository.findAllByTradeId(trade.getId());
            for (TradeProduct tradeProduct : allByTradeId) {
                BackingProductDto backingProductDto = new BackingProductDto();
                if (tradeProduct.getBacking() != null) {
                    backingProductDto.setCreateAt(tradeProduct.getCreatedAt());
                    backingProductDto.setPaidSum(tradeProduct.getBacking());
                    backingProductDto.setPayMethodName(tradeProduct.getTrade().getPayMethod().getType());
                } else {
                    backingProductDto.setPaidSum(0.0);
                }
                customerPreventedInfoDto.setBackingProductDto(backingProductDto);
            }
            customerPreventedInfoDtoList.add(customerPreventedInfoDto);
            customerPreventedInfoDto.setBalance(customer.getDebt());
        }

        List<CustomerDebtRepayment> customerDebtRepaymentList = customerDebtRepaymentRepository.findAllByCustomer_Id(customerId);
        for (CustomerDebtRepayment customerDebtRepayment : customerDebtRepaymentList) {
            CustomerPreventedInfoDto customerPreventedInfoDto = new CustomerPreventedInfoDto();
            CustomerDebtRepaymentDto customerDebtRepaymentDto = new CustomerDebtRepaymentDto();

            customerDebtRepaymentDto.setCreateAt(customerDebtRepayment.getPayDate() != null ? customerDebtRepayment.getPayDate() : customerDebtRepayment.getCreatedAt());
            customerDebtRepaymentDto.setPaidSum(customerDebtRepayment.getPaidSum());
            customerDebtRepaymentDto.setPayMethodName(customerDebtRepayment.getPaymentMethod() != null ? customerDebtRepayment.getPaymentMethod().getType() : null);
            customerDebtRepaymentDto.setDescription(customerDebtRepayment.getDescription());
            customerPreventedInfoDto.setCustomerDebtRepaymentDto(customerDebtRepaymentDto);
            customerPreventedInfoDtoList.add(customerPreventedInfoDto);
            customerPreventedInfoDto.setBalance(customer.getDebt());
        }

        if (customerPreventedInfoDtoList.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        return new ApiResponse("found", true, customerPreventedInfoDtoList);
    }

    public ApiResponse search(UUID branchId, String name) {
        List<Customer> all = customerRepository.findAllByBranchIdAndNameContainingIgnoreCaseAndActiveTrue(branchId, name);
        Set<Customer> allCustomer = new HashSet<>(all);
        all = new ArrayList<>(allCustomer);

        if (all.isEmpty()) {
            return new ApiResponse("not found", false);
        }
        return new ApiResponse("all", true, toCustomerDtoList(all));
    }


    public ApiResponse getAllCustomersCountAndFilter(UUID businessId, UUID branchId, Date startDate, Date endDate) {
        int customerCount = 0;
        double totalDebt = 0;
        if (startDate == null && endDate == null) {
            if (branchId != null) {
                List<Customer> all = customerRepository.findAllByBranchId(branchId);
                customerCount = all.size();
                double debt = 0;
                for (Customer customer : all) {
                    debt += customer.getDebt();
                }
                totalDebt = debt;
            } else {
                List<Customer> all = customerRepository.findAllByBusinessId(businessId);
                customerCount = all.size();
                double debt = 0;
                for (Customer customer : all) {
                    debt += customer.getDebt();
                }
                totalDebt = debt;
            }
        } else if (startDate != null && endDate != null) {
            Timestamp start = new Timestamp(startDate.getTime());
            Timestamp end = new Timestamp(endDate.getTime());
            if (branchId != null) {
                List<Customer> all = customerRepository.findAllByBranch_IdAndCreatedAtBetween(branchId, start, end);
                customerCount = all.size();
                double debt = 0;
                for (Customer customer : all) {
                    debt += customer.getDebt();
                }
                totalDebt = debt;
            } else {
                List<Customer> all = customerRepository.findAllByBusiness_IdAndCreatedAtBetween(businessId, start, end);
                customerCount = all.size();
                double debt = 0;
                for (Customer customer : all) {
                    debt += customer.getDebt();
                }
                totalDebt = debt;
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("count", customerCount);
        data.put("debt", totalDebt);
        return new ApiResponse("Mijozlar soni", true, data);
    }

    public ApiResponse getCustomersCountByGroup(UUID businessId) {
        List<CustomerByGroup> data = new LinkedList<>();
        int allCustomers = customerRepository.countAllByBusinessId(businessId);
        List<CustomerGroup> groups = customerGroupRepository.findAllByBusiness_Id(businessId);
        for (CustomerGroup group : groups) {
            int groupCustomers = customerRepository.countAllByCustomerGroupId(group.getId());
            double per = ((double) groupCustomers / allCustomers) * 100;
            DecimalFormat df = new DecimalFormat("#.##");
            String formattedPercentage = df.format(per);
            data.add(new CustomerByGroup(group.getName(), groupCustomers, formattedPercentage));
        }
        return new ApiResponse("Mijozlar guruhlar bo'yicha", true, data);
    }

    public ApiResponse getInActiveCustomers(UUID branchId) {
        Pageable pageable = PageRequest.of(0, 100);
        Page<InActiveUserProjection> customers = customerRepository.findAllByBranchIdAndPayDateNotNullOrderByPayDateAsc(branchId, pageable);
        return new ApiResponse("Faol bo'lmagan mijozlar ro'yxati", true, customers.getContent());
    }

    public ApiResponse ourMoneyForCustomer(UUID businessId) {
        Double allOurMoney = customerRepository.allOurMoney(businessId);
        Double allYourMoney = customerRepository.allYourMoney(businessId);
        Map<String, Object> data = new HashMap<>();
        data.put("all_our_money", allOurMoney);
        data.put("all_your_money", allYourMoney);
        return new ApiResponse(true, data);
    }

    public ApiResponse getAllTradeForClientCalendars(UUID branchId, UUID productId, Month selectedMonth, Integer selectedYear, Integer page, Integer size) {
        LocalDateTime selectedLocalDateTime = LocalDateTime.of(selectedYear, selectedMonth, 1, 0, 0);
        YearMonth yearMonth = YearMonth.of(selectedYear, selectedMonth);
        int thisDay = LocalDate.of(selectedYear, selectedMonth, yearMonth.lengthOfMonth()).getDayOfMonth();
        if (!branchRepository.existsById(branchId)) return new ApiResponse("BRANCH NOT FOUND", false);
        Pageable pageable = PageRequest.of((page == null ? 0 : page), (size == null ? 5 : size));
        Page<Customer> customerList = customerRepository.findAllByBranch_IdAndActiveIsTrue(pageable, branchId);
        if (customerList.isEmpty()) return new ApiResponse("CUSTOMERS NOT FOUND", false);
        List<CustomerCalendarDto> customerCalendarDtoList = new ArrayList<>();
        for (Customer customer : customerList.getContent()) {
            List<Timestamp> timestampList = new ArrayList<>();
            for (int day = 0; day < thisDay; day++) {
                List<TradeProduct> tradeList = findTradeProductList(customer, branchId, productId, selectedLocalDateTime, day);
                if (!tradeList.isEmpty()) {
                    if (selectedMonth.equals(formatDate(tradeList.get(0).getCreatedAt()).getMonth()) &&
                            selectedYear.equals(formatDate(tradeList.get(0).getCreatedAt()).getYear())) {
                        timestampList.add(tradeList.get(0).getCreatedAt());
                    }
                }
            }
            customerCalendarDtoList.add(new CustomerCalendarDto(
                    customer.getId(),
                    customer.getName(),
                    timestampList
            ));
        }
        HashMap result = new HashMap<>();
        result.put("data", customerCalendarDtoList);
        result.put("totalPage", customerList.getTotalPages());
        return new ApiResponse(result);
    }

    private List<TradeProduct> findTradeProductList(Customer customer, UUID branchId, UUID productId, LocalDateTime selectedLocalDateTime, int day) {
        if (productId == null) {
            return tradeProductRepository.findAllByTrade_Customer_IdAndTrade_Branch_IdAndCreatedAtBetween(
                    customer.getId(),
                    branchId,
                    Timestamp.valueOf(selectedLocalDateTime.plusDays(day)),
                    Timestamp.valueOf(selectedLocalDateTime.plusDays(day + 1))
            );
        }
        return tradeProductRepository.findAllByTrade_Customer_IdAndTrade_Branch_IdAndProduct_IdAndCreatedAtBetween(
                customer.getId(),
                branchId,
                productId,
                Timestamp.valueOf(selectedLocalDateTime.plusDays(day)),
                Timestamp.valueOf(selectedLocalDateTime.plusDays(day + 1))
        );
    }

    private LocalDate formatDate(Timestamp selectedDate) {
        Date date = new Date(selectedDate.getTime());

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public ApiResponse getMonthsByYear(UUID branchId) {
        List<MonthProjection> allMonths = tradeRepository.getAllMonths(branchId);
        List<MonthProjection> filteredMonths = new ArrayList<>();

        Set<String> uniqueMonthYearSet = new HashSet<>();

        for (MonthProjection monthProjection : allMonths) {
            // Vaqtni olamiz (timestamp)
            Long timestamp = monthProjection.getArrivaltime().getTime();

            // Vaqtni Oy va Yil obyektiga aylantiramiz
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            int monthOfYear = calendar.get(Calendar.MONTH);
            int yearValue = calendar.get(Calendar.YEAR);
            // Oy va yilni birlikda kalit sifatida qo'shamiz
            String monthYearKey = monthOfYear + "-" + yearValue;

            // Bir xil oy va yil kombinatsiyalarini tekshiramiz
            if (!uniqueMonthYearSet.contains(monthYearKey)) {
                filteredMonths.add(monthProjection);
                uniqueMonthYearSet.add(monthYearKey);
            }
        }

        return new ApiResponse(true, filteredMonths);
    }

    public ApiResponse getAllPageAble(UUID businessId, UUID branchId, UUID groupId, int size, int page, String name, Boolean isName, Boolean isDebt, String balanceFilter) {
        Pageable pageable = PageRequest.of(page, size);

        if (isName) {
            pageable = PageRequest.of(page, size, Sort.Direction.ASC, "name");
        } else {
            pageable = PageRequest.of(page, size, Sort.Direction.DESC, "name");
        }
        if (isDebt) {
            pageable = PageRequest.of(page, size, Sort.Direction.ASC, "debt");
        } else {
            pageable = PageRequest.of(page, size, Sort.Direction.DESC, "debt");
        }

        Page<Customer> all;

        if (name != null) {
            switch (balanceFilter) {
                case "haqdor" ->
                        all = customerRepository.findAllByBusinessIdAndNameContainingIgnoreCaseAndDebtGreaterThanAndActiveTrueOrBusinessIdAndPhoneNumberContainingIgnoreCaseAndDebtGreaterThanAndActiveTrue(businessId, name, 0, businessId, name, 0, pageable);
                case "balance" ->
                        all = customerRepository.findAllByBusinessIdAndNameContainingIgnoreCaseAndDebtAndActiveTrueOrBusinessIdAndPhoneNumberContainingIgnoreCaseAndDebtAndActiveTrue(businessId, name, 0, businessId, name, 0, pageable);
                case "qarzdor" ->
                        all = customerRepository.findAllByBusinessIdAndNameContainingIgnoreCaseAndDebtLessThanAndActiveTrueOrBusinessIdAndPhoneNumberContainingIgnoreCaseAndDebtLessThanAndActiveTrue(businessId, name, 0, businessId, name, 0, pageable);
                default ->
                        all = customerRepository.findAllByBusinessIdAndNameContainingIgnoreCaseAndActiveTrueOrBusinessIdAndPhoneNumberNotContainingIgnoreCaseAndActiveTrue(businessId, name, businessId, name, pageable);
            }
        } else if (branchId != null) {
            switch (balanceFilter) {
                case "haqdor" ->
                        all = customerRepository.findAllByBranch_IdAndActiveIsTrueAndDebtGreaterThan(branchId, 0, pageable);
                case "balance" ->
                        all = customerRepository.findAllByBranch_IdAndActiveIsTrueAndDebt(branchId, 0, pageable);
                case "qarzdor" ->
                        all = customerRepository.findAllByBranch_IdAndActiveIsTrueAndDebtLessThan(branchId, 0, pageable);
                default -> all = customerRepository.findAllByBranch_IdAndActiveIsTrue(pageable, branchId);
            }
        } else if (groupId != null) {
            switch (balanceFilter) {
                case "haqdor" ->
                        all = customerRepository.findAllByCustomerGroupIdAndActiveIsTrueAndDebtGreaterThan(groupId, 0, pageable);
                case "balance" ->
                        all = customerRepository.findAllByCustomerGroupIdAndActiveIsTrueAndDebt(groupId, 0, pageable);
                case "qarzdor" ->
                        all = customerRepository.findAllByCustomerGroupIdAndActiveIsTrueAndDebtLessThan(groupId, 0, pageable);
                default -> all = customerRepository.findAllByCustomerGroupIdAndActiveIsTrue(groupId, pageable);
            }
        } else {
            switch (balanceFilter) {
                case "haqdor" ->
                        all = customerRepository.findAllByBusiness_IdAndDebtGreaterThanAndActiveTrue(businessId, 0, pageable);
                case "balance" ->
                        all = customerRepository.findAllByBusiness_IdAndDebtAndActiveTrue(businessId, 0, pageable);
                case "qarzdor" ->
                        all = customerRepository.findAllByBusiness_IdAndDebtLessThanAndActiveTrue(businessId, 0, pageable);
                default -> all = customerRepository.findAllByBusiness_IdAndActiveTrue(businessId, pageable);
            }
        }

        if (all.isEmpty()) {
            return new ApiResponse("not found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", toCustomerDtoList(all.stream().toList()));
        response.put("currentPage", all.getNumber());
        response.put("totalItems", all.getTotalElements());
        response.put("totalPages", all.getTotalPages());

        return new ApiResponse("found", true, response);
    }


    public String generateCode() {
        Random random = new Random();
        long randomNumber = 100000000000L + random.nextLong(900000000000L); // 100000000-999999999
        return String.valueOf(randomNumber);
    }

    public ApiResponse getByBarcode(String barcode) {
        Optional<CustomerGet> optional = customerRepository.findCustomerByUniqueCode(barcode);
        return optional.map(customerGet -> new ApiResponse("found", true, customerGet))
                .orElseGet(() -> new ApiResponse("not found", false));
    }

    public ApiResponse getByUserId(UUID userId) {
        Optional<Customer> optionalCustomer = customerRepository.findByUser_Id(userId);
        return optionalCustomer.map(customer -> new ApiResponse("found", true, customer))
                .orElseGet(() -> new ApiResponse("not found", false));
    }

    public ApiResponse checkNumber(String number) {
        boolean exists = customerRepository.existsByUsername(number);
        if (exists) {
            return new ApiResponse("Customer already exists", true);
        }

        return new ApiResponse("Customer does not exist", false);
    }

    public ApiResponse getForTrade(String query) {
        Optional<CustomerResponseDto> optional = customerRepository.findByCustomerPhoneNumberOrUniqueCode(query, query);
        return optional
                .map(customerResponseDto -> new ApiResponse("Customer exists", true, customerResponseDto))
                .orElseGet(() -> new ApiResponse("Customer does not exist", false));
    }
}
