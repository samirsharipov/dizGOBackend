package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.*;
import uz.dizgo.erp.entity.Currency;
import uz.dizgo.erp.payload.*;
import uz.dizgo.erp.repository.*;
import uz.dizgo.erp.enums.HistoryName;
import uz.dizgo.erp.helpers.ProductEntityHelper;
import uz.dizgo.erp.repository.specifications.PurchaseSpecification;
import uz.dizgo.erp.service.logger.ProductActivityLogger;
import uz.dizgo.erp.utils.Constants;
import uz.dizgo.erp.utils.AppConstant;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final WarehouseService warehouseService;
    private final BalanceService balanceService;
    private final PayMethodRepository payMethodRepository;
    private final FifoCalculationRepository fifoCalculationRepository;
    private final HistoryRepository historyRepository;
    private final CustomerSupplierRepository customerSupplierRepository;
    private final CustomerSupplierService customerSupplierService;
    private final PurchaseOutlayCategoryRepository purchaseOutlayCategoryRepository;
    private final PurchaseOutlayRepository purchaseOutlayRepository;
    private final ProductEntityHelper productEntityHelper;
    private final MessageService messageService;
    private final ProductActivityLogger productActivityLogger;

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
        if (optionalPaymentMethod.isEmpty()) return new ApiResponse("NOT FOUND PAYMENT METHOD", false);

        PaymentMethod paymentMethod = optionalPaymentMethod.get();
        purchase.setPaymentMethod(paymentMethod);

        double debtSum = purchase.getDebtSum();
        if (purchaseDto.getDebtSum() > 0 || debtSum != purchaseDto.getDebtSum()) {
            supplier.setDebt(supplier.getDebt() - debtSum + purchaseDto.getDebtSum());
            supplierRepository.save(supplier);
            Optional<CustomerSupplier> optionalCustomerSupplier = customerSupplierRepository.findBySupplierId(supplier.getId());
            optionalCustomerSupplier.ifPresent(customerSupplierService::calculation);
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
                PurchaseProduct purchaseProduct = createOrEditPurchaseProduct(new PurchaseProduct(), purchaseProductDto, course, branch);
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
                PurchaseProduct editPurchaseProduct = createOrEditPurchaseProduct(purchaseProduct, purchaseProductDto, course, branch);
                if (editPurchaseProduct == null) continue;
                editPurchaseProduct.setPurchase(purchase);
                purchaseProductList.add(editPurchaseProduct);
                purchaseProduct.setPurchasedQuantity(purchaseProductDto.getPurchasedQuantity());
                fifoCalculationService.editPurchaseProduct(purchaseProduct, amount);
                warehouseService.createOrEditWareHouse(purchaseProduct, amount);
            }
        }

        // purchase outlay create
        if (!isEdit) {
            createOrEditPurchaseOutlay(purchase, purchaseDto);
        } else {
            List<PurchaseOutlay> all = purchaseOutlayRepository.findAllByPurchaseId(purchase.getId());
            purchaseOutlayRepository.deleteAll(all);
            createOrEditPurchaseOutlay(purchase, purchaseDto);
        }

        purchaseProductRepository.saveAll(purchaseProductList);


        if (isEdit) {
            if (purchaseDto.getPaidSum() > 0) {
                // todo dollar sum joyini korib chiqish
                balanceService.edit(branch.getId(), oldSumma, true, purchaseDto.getPaymentMethodId(), false, "purchase");
            }
        }
        if (purchaseDto.getPaidSum() > 0) {
            // todo dollar sum joyini korib chiqish
            balanceService.edit(branch.getId(), purchaseDto.getPaidSum(), false, purchaseDto.getPaymentMethodId(), false, "purchase");
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
//                for (User admin : admins) {
////                    if (admin.getChatId() != null) {
////                        SendMessage sendMessage = SendMessage
////                                .builder()
////                                .text(sendText)
////                                .parseMode(ParseMode.HTML)
////                                .chatId(admin.getChatId())
////                                .build();
////                        RestTemplate restTemplate = new RestTemplate();
////                        restTemplate.postForObject("https://api.telegram.org/bot" + Constants.BOT_TOKEN + "/sendMessage", sendMessage, Object.class);
////                    }
//                }
            } else {
                System.out.println("Purchase or Products not found.");
            }
        }
        return new ApiResponse("SUCCESS", true);
    }

    private void createOrEditPurchaseOutlay(Purchase purchase, PurchaseDto purchaseDto) {
        List<PurchaseOutlayDto> purchaseOutlayDtoList = purchaseDto.getPurchaseOutlayDtoList();
        if (purchaseOutlayDtoList != null) {
            if (!purchaseOutlayDtoList.isEmpty()) {
                for (PurchaseOutlayDto purchaseOutlayDto : purchaseOutlayDtoList) {
                    PurchaseOutlay purchaseOutlay = new PurchaseOutlay();
                    purchaseOutlay.setPurchase(purchase);
                    purchaseOutlay.setBusiness(purchase.getBranch().getBusiness());

                    Optional<PurchaseOutlayCategory> optionalPurchaseOutlayCategory = purchaseOutlayCategoryRepository.findById(purchaseOutlayDto.getCategoryId());
                    optionalPurchaseOutlayCategory.ifPresent(purchaseOutlay::setCategory);

                    purchaseOutlay.setTotalPrice(purchaseOutlayDto.getTotalPrice());
                    purchaseOutlayRepository.save(purchaseOutlay);
                }
            }
        }
    }

    private PurchaseProduct createOrEditPurchaseProduct(PurchaseProduct purchaseProduct, PurchaseProductDto purchaseProductDto, double course, Branch branch) {

        Product product = new Product();

        Product oldProduct = new Product();
        Product newProduct = new Product();
        UUID productId = purchaseProductDto.getProductId();

        if (purchaseProductDto.isNew()) {
            Product mainProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Globaldan kelgan maxsulot " + messageService.getMessage("not.found")));
            Optional<Product> optionalProduct = productRepository.findByBarcodeAndBusinessId(mainProduct.getBarcode(),
                    branch.getBusiness().getId());

            if (optionalProduct.isEmpty()) {
                Optional<Product> optional =
                        productRepository.findByBarcodeAndBusinessIdAndActiveFalseAndDeletedTrue(mainProduct.getBarcode(), branch.getBusiness().getId());
                if (optional.isPresent()) {
                    optionalProduct = optional;
                }
            }

            if (optionalProduct.isPresent()) {
                product = optionalProduct.get();

                oldProduct = product;

                List<Branch> branches = product.getBranch();
                branches.add(branch);
                product.setBranch(branches);
                product.setActive(true);
                product.setDeleted(false);
            } else {
                product = productEntityHelper.cloneProduct(productId, branch);
            }
        } else {
            Optional<Product> optional = productRepository.findById(productId);
            if (optional.isEmpty()) return null;
            product = optional.get();
        }
        product.setSalePrice(purchaseProductDto.getSalePrice());
        product.setBuyPrice(purchaseProductDto.getBuyPrice());
        product.setBuyPriceDollar(Math.round(purchaseProductDto.getBuyPrice() / course * 100) / 100.);
        product.setSalePriceDollar(Math.round(purchaseProductDto.getSalePrice() / course * 100) / 100.);
        product.setMargin(purchaseProductDto.getMargin());
        product.setQqs(purchaseProductDto.isQqs());
        productRepository.save(product);

        Map<String, Object> extra = Map.of(
                "buy price", purchaseProductDto.getBuyPrice(),
                "quantity", purchaseProductDto.getPurchasedQuantity(),
                "total sum", purchaseProductDto.getTotalSum());
        productActivityLogger.logPurchase(oldProduct, product, extra);

        purchaseProduct.setProduct(product);
        purchaseProduct.setPurchasedQuantity(purchaseProductDto.getPurchasedQuantity());
        purchaseProduct.setSalePrice(purchaseProductDto.getSalePrice());
        purchaseProduct.setBuyPrice(purchaseProductDto.getBuyPrice());
        purchaseProduct.setTotalSum(purchaseProductDto.getTotalSum());
        return purchaseProduct;
    }

    public ApiResponse getOne(UUID id) {
        return getPurchaseResponse(id, false);
    }

    public ApiResponse view(UUID purchaseId) {
        return getPurchaseResponse(purchaseId, true);
    }

    private ApiResponse getPurchaseResponse(UUID purchaseId, boolean includeProducts) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(purchaseId);
        if (optionalPurchase.isEmpty()) return new ApiResponse("NOT FOUND PURCHASE", false);

        Purchase purchase = optionalPurchase.get();
        List<PurchaseProduct> purchaseProductList = purchaseProductRepository.findAllByPurchaseId(purchase.getId());

        if (purchaseProductList.isEmpty()) return new ApiResponse("NOT FOUND PRODUCTS", false);

        if (includeProducts) {
            Map<String, Object> response = new HashMap<>();
            response.put("purchase", purchase);
            response.put("purchaseProductGetDtoList", toPurchaseProductGetDtoList(purchaseProductList));
            return new ApiResponse("FOUND", true, response);
        }

        PurchaseGetOneDto purchaseGetOneDto = new PurchaseGetOneDto(purchase, purchaseProductList);
        return new ApiResponse("FOUND", true, purchaseGetOneDto);
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
            dto.setName(purchaseProduct.getProduct().getName());
            if (purchaseProduct.getProduct().getMeasurement() != null) {
                dto.setMeasurement(purchaseProduct.getProduct().getMeasurement().getName());
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

    public ApiResponse getByBusiness(UUID businessId, UUID branchId, UUID userId, UUID supplierId,
                                     Timestamp startDate, Timestamp endDate, String status,
                                     int page, int size) {

        Specification<Purchase> spec = Specification
                .where(Optional.ofNullable(branchId)
                        .map(PurchaseSpecification::belongsToBranch)
                        .orElseGet(() -> PurchaseSpecification.belongsToBusiness(businessId)))
                .and(Optional.ofNullable(userId).map(PurchaseSpecification::belongsToUser).orElse(null))
                .and(Optional.ofNullable(supplierId).map(PurchaseSpecification::belongsToSupplier).orElse(null))
                .and((startDate != null && endDate != null) ? PurchaseSpecification.hasCreatedAtBetween(startDate, endDate) : null)
                .and(Optional.ofNullable(status).filter(s -> !s.isBlank()).map(PurchaseSpecification::hasPurchaseStatus).orElse(null));

        return getPurchasesResponse(spec, page, size);
    }

    public ApiResponse getDebtPurchaseBySupplierId(UUID supplierId, int page, int size) {
        Specification<Purchase> spec = Specification
                .where(PurchaseSpecification.belongsToSupplier(supplierId))
                .and(PurchaseSpecification.hasDebtGreaterThanZero());

        return getPurchasesResponse(spec, page, size);
    }

    // 🔹 Natijalarni olish va qaytarish uchun umumiy metod
    private ApiResponse getPurchasesResponse(Specification<Purchase> spec, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Purchase> purchases = purchaseRepository.findAll(spec, pageable);

        if (purchases.isEmpty()) {
            return new ApiResponse(messageService.getMessage("not.found"), false);
        }

        return new ApiResponse(messageService.getMessage("found"), true, Map.of(
                "list", getPurchases(purchases.getContent()),
                "currentPage", purchases.getNumber(),
                "totalPage", purchases.getTotalPages(),
                "totalItem", purchases.getTotalElements()
        ));
    }

    public PurchaseGetDto convertToDTO(Purchase purchase) {
        PurchaseGetDto dto = new PurchaseGetDto();
        dto.setId(purchase.getId());
        dto.setCreatedAt(purchase.getCreatedAt());
        dto.setInvoice(purchase.getInvoice());
        dto.setBranchName(purchase.getBranch() != null ? purchase.getBranch().getName() : null);
        dto.setSupplierName(purchase.getSupplier() != null ? purchase.getSupplier().getName() : null);
        dto.setDebt(purchase.getDebtSum());
        dto.setStatus(purchase.getPurchaseStatus() != null ? purchase.getPaymentStatus().getStatus() : null);
        dto.setTotal(purchase.getTotalSum());
        dto.setSellerFullName(purchase.getSeller() != null ? purchase.getSeller().getFirstName() + " " + purchase.getSeller().getLastName() : null);
        return dto;
    }

    public List<PurchaseGetDto> getPurchases(List<Purchase> purchases) {
        return purchases.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}