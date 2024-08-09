package uz.pdp.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.HistoryName;
import uz.pdp.springsecurity.enums.OUTLAY_STATUS;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.util.Constants;
import uz.pdp.springsecurity.utils.AppConstant;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.*;

@Service
public class OutlayService {
    @Autowired
    OutlayRepository outlayRepository;

    @Autowired
    OutlayCategoryRepository outlayCategoryRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PayMethodRepository payMethodRepository;
    @Autowired
    BalanceService balanceService;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private OutlayProductRepository outlayProductRepository;

    public ApiResponse add(OutlayDto outlayDto) {
        Outlay outlay = new Outlay();

        Optional<OutlayCategory> optionalCategory = outlayCategoryRepository.findById(outlayDto.getOutlayCategoryId());
        if (optionalCategory.isEmpty()) return new ApiResponse("OUTLAY CATEGORY NOT FOUND", false);
        outlay.setOutlayCategory(optionalCategory.get());

        outlay.setTotalSum(outlayDto.getTotalSum());

        Optional<Branch> optionalBranch = branchRepository.findById(outlayDto.getBranchId());
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("BRANCH NOT FOUND", false);
        }
        outlay.setBranch(optionalBranch.get());

        Optional<User> spender = userRepository.findById(outlayDto.getSpenderId());
        if (spender.isEmpty()) return new ApiResponse("SPENDER NOT FOUND", false);
        outlay.setSpender(spender.get());

        outlay.setDescription(outlayDto.getDescription());
        outlay.setDate(outlayDto.getDate());
        outlay.setDollarOutlay(outlayDto.getDollarOutlay());


        Optional<PaymentMethod> optionalPaymentMethod = payMethodRepository.findById(outlayDto.getPaymentMethodId());
        if (optionalPaymentMethod.isEmpty()) {
            return new ApiResponse("not found pay method id", false);
        }

        PaymentMethod paymentMethod = optionalPaymentMethod.get();
        outlay.setPaymentMethod(paymentMethod);

        balanceService.edit(optionalBranch.get().getId(), outlayDto.getTotalSum(), false, outlayDto.getPaymentMethodId(), outlayDto.getDollarOutlay(),"");

        Outlay save = outlayRepository.save(outlay);
//        HISTORY


        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        historyRepository.save(new History(
                HistoryName.XARAJAT,
                user,
                outlay.getBranch(),
                outlay.getTotalSum() + AppConstant.ADD_OUTLAY
        ));
        String sendText = "<b>#YANGI_XARAJAT \uD83D\uDCB8 \n\n</b>" +
                          "<b>Filial: </b>" + save.getBranch().getName() + "\n" +
                          "<b>Xarajat turi: </b>" + save.getOutlayCategory().getTitle() + "\n" +
                          "<b>Sarflovchi</b>: " + save.getSpender().getFirstName() + "\n" +
                          "<b>To'lov usuli: </b>" + save.getPaymentMethod().getType() + "\n" +
                          "<b>Xarajat: </b>" + save.getTotalSum() + "\n" +
                          "<b>Tavsif: </b>" + save.getDescription() + "\n";
        for (User admin : userRepository.findAllByBusiness_IdAndRoleName(save.getSpender().getBusiness().getId(), Constants.ADMIN)) {
            if (admin.getChatId() != null) {
                SendMessage sendMessage = SendMessage
                        .builder()
                        .text(sendText)
                        .parseMode(ParseMode.HTML)
                        .chatId(admin.getChatId())
                        .build();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.postForObject("https://api.telegram.org/bot" + Constants.BOT_TOKEN + "/sendMessage", sendMessage, Object.class);
            }
        }
        return new ApiResponse("ADDED", true);
    }

    public ApiResponse edit(UUID id, OutlayDto outlayDto) {
        if (!outlayRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);
        Optional<PaymentMethod> optionalPaymentMethod = payMethodRepository.findById(outlayDto.getPaymentMethodId());
        if (optionalPaymentMethod.isEmpty()) {
            return new ApiResponse("not found pay method id", false);
        }


        Outlay outlay = outlayRepository.getById(id);
        PaymentMethod paymentMethod = outlay.getPaymentMethod();
        double totalSum = outlay.getTotalSum();

        Optional<OutlayCategory> optionalCategory = outlayCategoryRepository.findById(outlayDto.getOutlayCategoryId());
        if (optionalCategory.isEmpty()) return new ApiResponse("OUTLAY CATEGORY NOT FOUND", false);
        outlay.setOutlayCategory(optionalCategory.get());

        outlay.setTotalSum(outlayDto.getTotalSum());

        Optional<Branch> optionalBranch = branchRepository.findById(outlayDto.getBranchId());
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("BRANCH NOT FOUND", false);
        }
        outlay.setBranch(optionalBranch.get());

        Optional<User> spender = userRepository.findById(outlayDto.getSpenderId());
        if (spender.isEmpty()) return new ApiResponse("SPENDER NOT FOUND", false);
        outlay.setSpender(spender.get());
        outlay.setDescription(outlayDto.getDescription());
        outlay.setDate(outlayDto.getDate());

        //eski summani balance ga qaytarish
        balanceService.edit(outlay.getBranch().getId(), totalSum, true, paymentMethod.getId(), outlay.isDollarOutlay(),"outlay");

        //yangi summa kiritish
        balanceService.edit(outlay.getBranch().getId(), outlayDto.getTotalSum(), false, outlayDto.getPaymentMethodId(), outlay.isDollarOutlay(),"outlay");

        outlayRepository.save(outlay);

//        HISTORY
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        historyRepository.save(new History(
                HistoryName.XARAJAT,
                user,
                outlay.getBranch(),
                AppConstant.EDIT_OUTLAY
        ));
        return new ApiResponse("EDITED", true);
    }

    public ApiResponse get(UUID id) {
        if (!outlayRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, outlayRepository.findById(id).get());
    }

    public ApiResponse delete(UUID id) {
        Optional<Outlay> optionalOutlay = outlayRepository.findById(id);
        if (optionalOutlay.isEmpty()) return new ApiResponse("NOT FOUND", false);
        Outlay outlay = optionalOutlay.get();
//        HISTORY
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        historyRepository.save(new History(
                HistoryName.XARAJAT,
                user,
                outlay.getBranch(),
                outlay.getTotalSum() + AppConstant.DELETE_OUTLAY
        ));
        outlayRepository.deleteById(id);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getByDate(Date date, UUID branch_id) {
        List<Outlay> allByDate = outlayRepository.findAllByDate(date, branch_id);
        if (allByDate.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, allByDate);
    }

    public ApiResponse getAllByBranchId(UUID branch_id) {
        List<Outlay> allByBranch_id = outlayRepository.findAllByBranch_IdOrderByCreatedAtDesc(branch_id);
        if (allByBranch_id.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, allByBranch_id);
    }

    public ApiResponse getAllByBusinessId(UUID businessId) {
        List<Outlay> allByBusinessId = outlayRepository.findAllByBranch_BusinessIdOrderByCreatedAtDesc(businessId);
        if (allByBusinessId.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }
        return new ApiResponse("FOUND", true, allByBusinessId);
    }

    public ApiResponse getAllByDate(Date date, UUID business_id) {
        List<Outlay> allByDateAndBusinessId = outlayRepository.findAllByDateAndBusinessId(business_id, date);
        if (allByDateAndBusinessId.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, allByDateAndBusinessId);
    }

    public ApiResponse copyOutlay(UUID outlayId) {
        Outlay outlay = outlayRepository.findById(outlayId).get();
        Outlay newOutlay = new Outlay(
                outlay.getOutlayCategory(),
                outlay.getTotalSum(),
                outlay.getBranch(),
                outlay.getSpender(),
                outlay.getPaymentMethod(),
                outlay.getDescription(),
                new java.util.Date(),
                outlay.isDollarOutlay(),
                null
        );
        outlayRepository.save(newOutlay);
        return new ApiResponse("FOUND", true, "Copy successfully");
    }

    @Transactional
    public ApiResponse addOutlayType(OutlayTypeDto outlayTypeDto, User user) {
        try {
            double sum = 0;
            for (OutlayTypeProduct product : outlayTypeDto.getProducts()) {
                sum = sum + product.getPrice();
            }
            Outlay outlay = outlayRepository.save(new Outlay(
                    null,
                    sum,
                    branchRepository.findById(outlayTypeDto.getBranchId()).orElseThrow(),
                    user,
                    payMethodRepository.findById(outlayTypeDto.getPaymentId()).orElseThrow(),
                    outlayTypeDto.getName(),
                    new java.util.Date(),
                    outlayTypeDto.isDollar(),
                    outlayTypeDto.getStatus()
            ));
            for (OutlayTypeProduct product : outlayTypeDto.getProducts()) {
                outlayProductRepository.save(new OutlayProduct(
                        product.getName(),
                        product.getPrice(),
                        true,
                        outlay
                ));
            }
            return new ApiResponse("Xarajat qo'shildi", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik yuzaga keldi", false);
        }
    }

    public ApiResponse getOutlaysListByType(OUTLAY_STATUS type, UUID branchId, Integer page, Integer limit, java.util.Date startDate, java.util.Date endDate) {
        if (startDate == null && endDate == null) {
            try {
                Page<Outlay> outlayPage = outlayRepository.findByBranch_IdAndStatusEqualsOrderByCreatedAtDesc(branchId, type, PageRequest.of(page - 1, limit));
                Map<String, Object> data = new HashMap<>();
                data.put("totalPages", outlayPage.getTotalPages());
                data.put("list", outlayPage.getContent());
                return new ApiResponse("Xarajatlar ro'yxati", true, data);
            } catch (Exception e) {
                e.printStackTrace();
                return new ApiResponse("Xatolik!", false);
            }
        } else {
            try {
                Page<Outlay> outlayPage = outlayRepository.findByBranch_IdAndStatusEqualsAndCreatedAtBetweenOrderByCreatedAtDesc(branchId, type, PageRequest.of(page - 1, limit), startDate, endDate);
                Map<String, Object> data = new HashMap<>();
                data.put("totalPages", outlayPage.getTotalPages());
                data.put("list", outlayPage.getContent());
                return new ApiResponse("Xarajatlar ro'yxati", true, data);
            } catch (Exception e) {
                e.printStackTrace();
                return new ApiResponse("Xatolik!", false);
            }
        }
    }

    public ApiResponse getOutlayProducts(UUID outlayId) {
        return new ApiResponse("Xarajat Info", true, outlayProductRepository.findAllByOutlay_IdAndActiveTrueOrderByCreatedAtDesc(outlayId));
    }

    @Transactional
    public ApiResponse editOutlayType(OutlayTypeDto outlayTypeDto, User user, UUID id) {
        try {
            Outlay outlay = outlayRepository.findById(id).orElseThrow();
            for (OutlayProduct outlayProduct : outlayProductRepository.findAllByOutlay_IdAndActiveTrueOrderByCreatedAtDesc(outlay.getId())) {
                outlayProduct.setActive(false);
                outlayProductRepository.save(outlayProduct);
            }
            double sum = 0;
            for (OutlayTypeProduct product : outlayTypeDto.getProducts()) {
                sum = sum + product.getPrice();
            }
            outlay.setTotalSum(sum);
            outlay.setDollarOutlay(outlayTypeDto.isDollar());
            outlay.setBranch(branchRepository.findById(outlayTypeDto.getBranchId()).orElseThrow());
            outlay.setPaymentMethod(payMethodRepository.findById(outlayTypeDto.getPaymentId()).orElseThrow());
            outlay.setSpender(user);
            outlay.setStatus(outlayTypeDto.getStatus());
            outlayRepository.save(outlay);
            for (OutlayTypeProduct product : outlayTypeDto.getProducts()) {
                outlayProductRepository.save(new OutlayProduct(
                        product.getName(),
                        product.getPrice(),
                        true,
                        outlay
                ));
            }
            return new ApiResponse("Tahrirlandi ✅", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Error", false);
        }
    }
}
