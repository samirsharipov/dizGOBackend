package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.pdp.springsecurity.entity.Currency;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.HistoryName;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.util.Constants;
import uz.pdp.springsecurity.utils.AppConstant;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseProductRepository purchaseProductRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ExchangeStatusRepository exchangeStatusRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final FifoCalculationService fifoCalculationService;
    private final ProductTypePriceRepository productTypePriceRepository;
    private final WarehouseService warehouseService;
    private final BalanceService balanceService;
    private final PayMethodRepository payMethodRepository;
    private final FifoCalculationRepository fifoCalculationRepository;
    private final HistoryRepository historyRepository;

    public ApiResponse add(PurchaseDto purchaseDto) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findFirstByBranchIdOrderByCreatedAtDesc(purchaseDto.getBranchId());
        int invoice = 0;
        if (optionalPurchase.isPresent()) {
            String invoiceStr = optionalPurchase.get().getInvoice();
            invoice = invoiceStr != null ? Integer.parseInt(invoiceStr) : 0;
        }
        Purchase purchase = new Purchase();
        purchase.setInvoice(String.valueOf(++invoice));
        return createOrEditPurchase(false, purchase, purchaseDto);
    }

    public ApiResponse edit(UUID id, PurchaseDto purchaseDto) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);
        if (optionalPurchase.isEmpty()) return new ApiResponse("NOT FOUND", false);

        Purchase purchase = optionalPurchase.get();
        if (!purchase.isEditable()) return new ApiResponse("30 KUNDAN KEYIN TAHRIRLASH MUMKIN EMAS", false);
        LocalDateTime createdAt = purchase.getCreatedAt().toLocalDateTime();
        int day = LocalDateTime.now().getDayOfYear() - createdAt.getDayOfYear();
        if (day > 30) {
            purchase.setEditable(false);
            purchaseRepository.save(purchase);
            return new ApiResponse("30 KUNDAN KEYIN TAHRIRLASH MUMKIN EMAS", false);
        }
        return createOrEditPurchase(true, purchase, purchaseDto);
    }

    private ApiResponse createOrEditPurchase(boolean isEdit, Purchase purchase, PurchaseDto purchaseDto) {

        double oldSumma = 0;
        UUID payMethodId = null;
        if (isEdit) {
            if (purchase.getPaidSum() >= 0) {
                oldSumma = purchase.getPaidSum();
            }
            payMethodId = purchase.getPaymentMethod().getId();
        }

        if (purchaseDto.getPurchaseProductsDto().isEmpty()) {
            return new ApiResponse("NOT FOUND PURCHASE PRODUCT", false);
        }

        Optional<Supplier> optionalSupplier = supplierRepository.findById(purchaseDto.getSupplerId());
        if (optionalSupplier.isEmpty()) return new ApiResponse("SUPPLIER NOT FOUND", false);
        Supplier supplier = optionalSupplier.get();
        purchase.setSupplier(supplier);

        Optional<User> optionalUser = userRepository.findById(purchaseDto.getSeller());
        if (optionalUser.isEmpty()) return new ApiResponse("SELLER NOT FOUND", false);
        purchase.setSeller(optionalUser.get());

        Optional<ExchangeStatus> optionalPurchaseStatus = exchangeStatusRepository.findById(purchaseDto.getPurchaseStatusId());
        if (optionalPurchaseStatus.isEmpty()) return new ApiResponse("PURCHASE STATUS NOT FOUND", false);
        purchase.setPurchaseStatus(optionalPurchaseStatus.get());

        Optional<PaymentStatus> optionalPaymentStatus = paymentStatusRepository.findById(purchaseDto.getPaymentStatusId());
        if (optionalPaymentStatus.isEmpty()) return new ApiResponse("PAYMENT STATUS NOT FOUND", false);
        purchase.setPaymentStatus(optionalPaymentStatus.get());

        Optional<Branch> optionalBranch = branchRepository.findById(purchaseDto.getBranchId());
        if (optionalBranch.isEmpty()) return new ApiResponse("BRANCH NOT FOUND", false);
        Branch branch = optionalBranch.get();
        purchase.setBranch(branch);

        Optional<PaymentMethod> optionalPaymentMethod = payMethodRepository.findById(purchaseDto.getPaymentMethodId());
        if (optionalPaymentMethod.isEmpty()) {
            return new ApiResponse("NOT FOUND PAYMENT METHOD", false);
        }
        PaymentMethod paymentMethod = optionalPaymentMethod.get();
        purchase.setPaymentMethod(paymentMethod);

        double debtSum = purchase.getDebtSum();
        if (purchaseDto.getDebtSum() > 0 || debtSum != purchaseDto.getDebtSum()) {
            supplier.setDebt(supplier.getDebt() - debtSum + purchaseDto.getDebtSum());
            supplierRepository.save(supplier);
        }

        purchase.setTotalSum(purchaseDto.getTotalSum());
        purchase.setPaidSum(purchaseDto.getPaidSum());
        purchase.setDebtSum(purchaseDto.getDebtSum());
        purchase.setDeliveryPrice(purchaseDto.getDeliveryPrice());
        purchase.setDate(purchaseDto.getDate());
        purchase.setDescription(purchaseDto.getDescription());


        purchaseRepository.save(purchase);
//        HISTORY
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (isEdit) {
            historyRepository.save(new History(
                    HistoryName.XARID,
                    user,
                    branch,
                    purchase.getInvoice() + AppConstant.EDIT_PURCHASE
            ));
        } else {
            historyRepository.save(new History(
                    HistoryName.XARID,
                    user,
                    branch,
                    purchase.getInvoice() + AppConstant.ADD_PURCHASE
            ));
        }


        UUID businessId = branch.getBusiness().getId();
        Optional<Currency> optionalCurrency = currencyRepository.findByBusinessId(businessId);
        double course = 11500;
        if (optionalCurrency.isPresent()) {
            course = optionalCurrency.get().getCourse();
        }

        List<PurchaseProductDto> purchaseProductDtoList = purchaseDto.getPurchaseProductsDto();
        List<PurchaseProduct> purchaseProductList = new ArrayList<>();

        for (PurchaseProductDto purchaseProductDto : purchaseProductDtoList) {
            if (purchaseProductDto.getPurchaseProductId() == null) {
                PurchaseProduct purchaseProduct = createOrEditPurchaseProduct(new PurchaseProduct(), purchaseProductDto, course);
                if (purchaseProduct == null) continue;
                purchaseProduct.setPurchase(purchase);
                purchaseProductRepository.save(purchaseProduct);
                purchaseProductList.add(purchaseProduct);
                double minusAmount = warehouseService.createOrEditWareHouse(purchaseProduct, purchaseProduct.getPurchasedQuantity());
                fifoCalculationService.createPurchaseProduct(purchaseProduct, minusAmount);
            } else if (purchaseProductDto.isDelete()) {
                if (purchaseProductRepository.existsById(purchaseProductDto.getPurchaseProductId())) {
                    PurchaseProduct purchaseProduct = purchaseProductRepository.getById(purchaseProductDto.getPurchaseProductId());
                    warehouseService.createOrEditWareHouse(purchaseProduct, -purchaseProduct.getPurchasedQuantity());
                    purchaseProductRepository.deleteById(purchaseProductDto.getPurchaseProductId());
                }
            } else {
                Optional<PurchaseProduct> optionalPurchaseProduct = purchaseProductRepository.findById(purchaseProductDto.getPurchaseProductId());
                if (optionalPurchaseProduct.isEmpty()) continue;
                PurchaseProduct purchaseProduct = optionalPurchaseProduct.get();
                double amount = purchaseProductDto.getPurchasedQuantity() - purchaseProduct.getPurchasedQuantity();
                PurchaseProduct editPurchaseProduct = createOrEditPurchaseProduct(purchaseProduct, purchaseProductDto, course);
                if (editPurchaseProduct == null) continue;
                editPurchaseProduct.setPurchase(purchase);
                purchaseProductList.add(editPurchaseProduct);
                purchaseProduct.setPurchasedQuantity(purchaseProductDto.getPurchasedQuantity());
                fifoCalculationService.editPurchaseProduct(purchaseProduct, amount);
                warehouseService.createOrEditWareHouse(purchaseProduct, amount);
            }
        }
        purchaseProductRepository.saveAll(purchaseProductList);


        if (isEdit) {
            if (purchaseDto.getPaidSum() > 0) {
                balanceService.edit(branch.getId(), oldSumma, true, payMethodId);
            }
        }
        if (purchaseDto.getPaidSum() > 0) {
            balanceService.edit(branch.getId(), purchaseDto.getPaidSum(), false, payMethodId);
        }
        if (!isEdit) {
            Purchase save = purchaseRepository.findByBranchIdAndInvoiceContainingIgnoreCase(purchaseDto.getBranchId(), purchase.getInvoice());
            ApiResponse view = view(save.getId());
            if (view.isSuccess()) {
                Map<String, Object> responseData = (Map<String, Object>) view.getObject();
                Purchase purchase1 = (Purchase) responseData.get("purchase");
                List<PurchaseProductGetDto> purchaseProductGetDtoList = (List<PurchaseProductGetDto>) responseData.get("purchaseProductGetDtoList");
                StringBuilder products = new StringBuilder();
                for (PurchaseProductGetDto purchaseProductGetDto : purchaseProductGetDtoList) {
                    products.append("<b>Mahsulot nomi: ").append(purchaseProductGetDto.getName()).append("</b>").append(" || ").append(purchaseProductGetDto.getQuantity()).append(" X ").append(purchaseProductGetDto.getSalePrice()).append(" = ").append(purchaseProductGetDto.getTotalSum()).append("\n");
                }
                List<User> admins = userRepository.findAllByBusiness_IdAndRoleName(purchase1.getSeller().getBusiness().getId(), Constants.ADMIN);
                String sendText = "<b>#YANGI_XARID \uD83D\uDCD1 \n\n</b>" +
                                  "<b>Sotuvchi: </b>" + purchase1.getSeller().getFirstName() + "\n" +
                                  "<b>Diller: </b>" + purchase1.getSupplier().getName() + "\n" +
                                  "<b>To'lov usuli: </b>" + purchase1.getPaymentMethod().getType() + "\n" +
                                  "<b>To'lov statusi: </b>" + purchase1.getPaymentStatus().getStatus() + "\n" +
                                  "<b>Xarid holati: </b>" + purchase1.getPurchaseStatus().getStatus() + "\n\n" +
                                  "<b><i>MAHSULOTLAR \uD83D\uDCD1</i></b> \n\n" + products;
                for (User admin : admins) {
                    if (admin.getChatId()!=null){
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
            } else {
                System.out.println("Purchase or Products not found.");
            }
        }
        return new ApiResponse("SUCCESS", true);
    }

    private PurchaseProduct createOrEditPurchaseProduct(PurchaseProduct purchaseProduct, PurchaseProductDto purchaseProductDto, double course) {

        //SINGLE TYPE
        if (purchaseProductDto.getProductId() != null) {
            UUID productId = purchaseProductDto.getProductId();
            Optional<Product> optional = productRepository.findById(productId);
            if (optional.isEmpty()) return null;
            Product product = optional.get();
            product.setSalePrice(purchaseProductDto.getSalePrice());
            product.setBuyPrice(purchaseProductDto.getBuyPrice());
            product.setBuyPriceDollar(Math.round(purchaseProductDto.getBuyPrice() / course * 100) / 100.);
            product.setSalePriceDollar(Math.round(purchaseProductDto.getSalePrice() / course * 100) / 100.);
            productRepository.save(product);
            purchaseProduct.setProduct(product);
        } else {//MANY TYPE
            UUID productTypePriceId = purchaseProductDto.getProductTypePriceId();
            Optional<ProductTypePrice> optional = productTypePriceRepository.findById(productTypePriceId);
            if (optional.isEmpty()) return null;
            ProductTypePrice productTypePrice = optional.get();
            productTypePrice.setBuyPrice(purchaseProductDto.getBuyPrice());
            productTypePrice.setSalePrice(purchaseProductDto.getSalePrice());
            productTypePrice.setBuyPriceDollar(Math.round(purchaseProductDto.getBuyPrice() / course * 100) / 100.);
            productTypePrice.setSalePriceDollar(Math.round(purchaseProductDto.getSalePrice() / course * 100) / 100.);
            productTypePriceRepository.save(productTypePrice);
            purchaseProduct.setProductTypePrice(productTypePrice);
        }

        purchaseProduct.setPurchasedQuantity(purchaseProductDto.getPurchasedQuantity());
        purchaseProduct.setSalePrice(purchaseProductDto.getSalePrice());
        purchaseProduct.setBuyPrice(purchaseProductDto.getBuyPrice());
        purchaseProduct.setTotalSum(purchaseProductDto.getTotalSum());
        return purchaseProduct;
    }

    public ApiResponse getOne(UUID id) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);
        if (optionalPurchase.isEmpty()) return new ApiResponse("NOT FOUND PURCHASE", false);
        Purchase purchase = optionalPurchase.get();
        List<PurchaseProduct> purchaseProductList = purchaseProductRepository.findAllByPurchaseId(purchase.getId());
        if (purchaseProductList.isEmpty()) return new ApiResponse("NOT FOUND PRODUCTS", false);
        PurchaseGetOneDto purchaseGetOneDto = new PurchaseGetOneDto(
                purchase,
                purchaseProductList
        );
        return new ApiResponse("FOUND", true, purchaseGetOneDto);
    }

    public ApiResponse view(UUID purchaseId) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(purchaseId);
        if (optionalPurchase.isEmpty()) return new ApiResponse("NOT FOUND PURCHASE", false);
        Purchase purchase = optionalPurchase.get();
        List<PurchaseProduct> purchaseProductList = purchaseProductRepository.findAllByPurchaseId(purchase.getId());
        if (purchaseProductList.isEmpty()) return new ApiResponse("NOT FOUND PRODUCTS", false);
        Map<String, Object> response = new HashMap<>();
        response.put("purchase", purchase);
        response.put("purchaseProductGetDtoList", toPurchaseProductGetDtoList(purchaseProductList));
        return new ApiResponse("FOUND", true, response);
    }

    private List<PurchaseProductGetDto> toPurchaseProductGetDtoList(List<PurchaseProduct> purchaseProductList) {
        List<PurchaseProductGetDto> purchaseProductGetDtoList = new ArrayList<>();
        for (PurchaseProduct purchaseProduct : purchaseProductList) {
            PurchaseProductGetDto dto = new PurchaseProductGetDto(
                    purchaseProduct.getPurchasedQuantity(),
                    purchaseProduct.getBuyPrice(),
                    purchaseProduct.getSalePrice(),
                    purchaseProduct.getTotalSum()
            );
            if (purchaseProduct.getProduct() != null) {
                dto.setName(purchaseProduct.getProduct().getName());
                dto.setMeasurement(purchaseProduct.getProduct().getMeasurement().getName());
            } else {
                dto.setName(purchaseProduct.getProductTypePrice().getName());
                dto.setMeasurement(purchaseProduct.getProductTypePrice().getProduct().getMeasurement().getName());
            }
            double remainQuantity = fifoCalculationRepository.remainQuantityByPurchaseProductId(purchaseProduct.getId());
            remainQuantity = Math.round(remainQuantity * 100) / 100.;
            dto.setSoldQuantity(dto.getQuantity() - remainQuantity);
            dto.setProfit((dto.getSalePrice() - dto.getBuyPrice()) * dto.getSoldQuantity());
            purchaseProductGetDtoList.add(dto);
        }
        return purchaseProductGetDtoList;
    }

    public ApiResponse delete(UUID id) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);
        if (optionalPurchase.isEmpty())
            return new ApiResponse("NOT FOUND", false);
        String invoice = optionalPurchase.get().getInvoice();
//        HISTORY
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        historyRepository.save(new History(
                HistoryName.XARID,
                user,
                optionalPurchase.get().getBranch(),
                invoice + AppConstant.DELETE_PURCHASE
        ));
        purchaseRepository.deleteById(id);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getByBranch(UUID brId, UUID userId, UUID supId, Date date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Purchase> purchasePage;
        if (userId != null && supId != null && date != null)
            purchasePage = purchaseRepository.findAllByBranchIdAndSellerIdAndSupplierIdAndDate(brId, userId, supId, date, pageable);
        else if (userId != null && supId != null)
            purchasePage = purchaseRepository.findAllByBranchIdAndSellerIdAndSupplierId(brId, userId, supId, pageable);
        else if (userId != null && date != null)
            purchasePage = purchaseRepository.findAllByBranchIdAndSellerIdAndDate(brId, userId, date, pageable);
        else if (supId != null && date != null)
            purchasePage = purchaseRepository.findAllByBranchIdAndSupplierIdAndDate(brId, supId, date, pageable);
        else if (userId != null)
            purchasePage = purchaseRepository.findAllByBranchIdAndSellerId(brId, userId, pageable);
        else if (supId != null)
            purchasePage = purchaseRepository.findAllByBranchIdAndSupplierId(brId, supId, pageable);
        else if (date != null)
            purchasePage = purchaseRepository.findAllByBranchIdAndDate(brId, date, pageable);
        else
            purchasePage = purchaseRepository.findAllByBranchId(brId, pageable);
        return getAllHelper(purchasePage);
    }

    public ApiResponse getByBusiness(UUID busId, UUID userId, UUID supId, Date date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Purchase> purchasePage;
        if (userId != null && supId != null && date != null)
            purchasePage = purchaseRepository.findAllByBranch_BusinessIdAndSellerIdAndSupplierIdAndDate(busId, userId, supId, date, pageable);
        else if (userId != null && supId != null)
            purchasePage = purchaseRepository.findAllByBranch_BusinessIdAndSellerIdAndSupplierId(busId, userId, supId, pageable);
        else if (userId != null && date != null)
            purchasePage = purchaseRepository.findAllByBranch_BusinessIdAndSellerIdAndDate(busId, userId, date, pageable);
        else if (supId != null && date != null)
            purchasePage = purchaseRepository.findAllByBranch_BusinessIdAndSupplierIdAndDate(busId, supId, date, pageable);
        else if (userId != null)
            purchasePage = purchaseRepository.findAllByBranch_BusinessIdAndSellerId(busId, userId, pageable);
        else if (supId != null)
            purchasePage = purchaseRepository.findAllByBranch_BusinessIdAndSupplierId(busId, supId, pageable);
        else if (date != null)
            purchasePage = purchaseRepository.findAllByBranch_BusinessIdAndDate(busId, date, pageable);
        else
            purchasePage = purchaseRepository.findAllByBranch_BusinessId(busId, pageable);
        return getAllHelper(purchasePage);
    }

    private ApiResponse getAllHelper(Page<Purchase> purchasePage) {
        if (purchasePage.isEmpty()) {
            return new ApiResponse("MA'LUMOT TOPILMADI", false);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("list", purchasePage.getContent());
        response.put("currentPage", purchasePage.getNumber());
        response.put("totalPage", purchasePage.getTotalPages());
        response.put("totalItem", purchasePage.getTotalElements());
        return new ApiResponse(true, response);
    }
}
