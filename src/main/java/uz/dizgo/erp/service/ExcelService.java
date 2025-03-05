package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import uz.dizgo.erp.entity.*;
import uz.dizgo.erp.entity.Currency;
import uz.dizgo.erp.repository.*;
import uz.dizgo.erp.enums.CarInvoiceType;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.ExcelDto;
import uz.dizgo.erp.payload.ExportExcelDto;
import uz.dizgo.erp.payload.ProductViewDtos;
import uz.dizgo.erp.payload.excel.customertradehistory.ExcelCustomerTradeHistory;
import uz.dizgo.erp.payload.excel.customertradehistory.ExcelProductTrade;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExcelService {
    private final ProductRepository productRepository;
    private final TradeProductRepository tradeProductRepository;
    private final WarehouseRepository warehouseRepository;
    private final MeasurementRepository measurementRepository;
    private final BranchRepository branchRepository;
    private final CarRepository carRepository;
    private final CarInvoiceRepository carInvoiceRepository;
    private final CategoryRepository categoryRepository;
    private final CurrencyRepository currencyRepository;
    private final ProductTypeValueRepository productTypeValueRepository;
    private final TradeRepository tradeRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;
    private final BrandRepository brandRepository;
    private final FifoCalculationRepository fifoCalculationRepository;

    public List<ProductViewDtos> getByBusiness(UUID businessId) {

        boolean checkingBranch = false;
        Optional<Branch> optionalBranch = branchRepository.findById(businessId);
        if (optionalBranch.isPresent()) {
            checkingBranch = true;
        }

        List<ProductViewDtos> productViewDtoList = new ArrayList<>();
        List<Product> productList = null;
        productList = productRepository.findAllByBranchIdAndActiveTrue(businessId);
        if (productList.isEmpty()) {
            productList = productRepository.findAllByBusiness_IdAndActiveTrue(businessId);
        }
        if (productList.isEmpty()) {
            return null;
        } else {
            for (Product product : productList) {
                ProductViewDtos productViewDto = new ProductViewDtos();
                productViewDto.setProductName(product.getName());
                if (product.getBrand() != null) productViewDto.setBrandName(product.getBrand().getName());
                productViewDto.setBarcode(productViewDto.getBarcode());
                productViewDto.setBuyPrice(product.getBuyPrice());
                productViewDto.setSalePrice(product.getSalePrice());
                productViewDto.setMinQuantity(product.getMinQuantity());
                productViewDto.setExpiredDate(product.getExpireDate());
                Optional<Measurement> optionalMeasurement = measurementRepository.findById(product.getMeasurement().getId());
                optionalMeasurement.ifPresent(measurement -> productViewDto.setMeasurementId(measurement.getName()));
                if (checkingBranch) {
                    Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(businessId, product.getId());
                    optionalWarehouse.ifPresent(warehouse -> productViewDto.setAmount(warehouse.getAmount()));
                } else {
                    List<Warehouse> warehouseList = warehouseRepository.findAllByBranch_BusinessIdAndProductId(businessId, product.getId());
                    double amount = 0;
                    for (Warehouse warehouse : warehouseList) {
                        amount += warehouse.getAmount();
                    }
                    productViewDto.setAmount(amount);
                }
                productViewDtoList.add(productViewDto);
            }
            return productViewDtoList;
        }
    }


    public ApiResponse save(MultipartFile file, UUID categoryId, UUID measurementId, UUID branchId, UUID brandId) {

        Business business = null;

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        Optional<Measurement> optionalMeasurement = measurementRepository.findById(measurementId);
        Brand brand = null;
        if (brandId != null) {
            Optional<Brand> optionalBrand = brandRepository.findById(brandId);
            brand = optionalBrand.get();
        }
        Category category = null;
        if (categoryId != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            category = optionalCategory.get();
        }

        if (optionalBranch.isEmpty()) {
            return new ApiResponse("NOT FOUND BRANCH", false);
        }

        if (optionalMeasurement.isEmpty()) {
            return new ApiResponse("NOT FOUND MEASUREMENT", false);
        }

        try {
            business = optionalBranch.get().getBusiness();
            List<Branch> branchList = new ArrayList<>();
            branchList.add(optionalBranch.get());

            List<ExportExcelDto> exportExcelDtoList = ExcelHelper.excelToTutorials(file.getInputStream());
            List<Product> productList = new ArrayList<>();
            List<FifoCalculation> fifoCalculationList = new ArrayList<>();
            List<Warehouse> warehouseList = new ArrayList<>();
            int count = 0;
            for (ExportExcelDto excelDto : exportExcelDtoList) {

                if (checkProduct(branchId, optionalBranch, fifoCalculationList, excelDto.getBarcode(), excelDto.getAmount(), excelDto.getBuyPrice()))
                    continue;

                if (Objects.equals(excelDto.getProductName(), "")) {
                    continue;
                }
                Product product = new Product();
                product.setBusiness(business);
                product.setName(excelDto.getProductName());
                product.setExpireDate(excelDto.getExpiredDate());
                boolean exists = productRepository.existsByBarcodeAndBusinessIdAndActiveTrue(excelDto.getBarcode(), optionalBranch.get().getBusiness().getId());
                if (exists) {
                    continue;
                }
                product.setBarcode(String.valueOf(excelDto.getBarcode()));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                if (excelDto.getExpiredDate() != null) {
                    product.setDueDate(formatter.parse(formatter.format(excelDto.getExpiredDate())));
                } else {
                    Date date = new Date();
                    product.setDueDate(date);
                }
                product.setBuyPrice(excelDto.getBuyPrice());
                product.setSalePrice(excelDto.getSalePrice());
                product.setMinQuantity(excelDto.getMinQuantity());
                product.setBranch(branchList);
                product.setTax(0);
                if (category != null) {
                    product.setCategory(category);
                }
                if (brand != null) {
                    product.setBrand(brand);
                }
                product.setMeasurement(optionalMeasurement.get());
                product.setPhoto(null);
                Warehouse warehouse = new Warehouse();
                warehouse.setBranch(optionalBranch.get());
                warehouse.setAmount(excelDto.getAmount());
                warehouse.setProduct(product);
                fifoCalculationList.add(
                        new FifoCalculation(
                                optionalBranch.get(),
                                excelDto.getAmount(),
                                excelDto.getAmount(),
                                excelDto.getBuyPrice(),
                                new Date(),
                                product
                        )
                );
                warehouseList.add(warehouse);
                productList.add(product);
                count++;
            }
            if (exportExcelDtoList.size() > 0) {
                productRepository.saveAll(productList);
                warehouseRepository.saveAll(warehouseList);
                fifoCalculationRepository.saveAll(fifoCalculationList);
                return new ApiResponse("Successfully Added " + count + " Product", true);
            }
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data:" + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new ApiResponse();
    }

    @Transactional
    public ApiResponse saveExcel(MultipartFile file, UUID branchId) {

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);

        if (optionalBranch.isEmpty()) {
            return new ApiResponse("NOT FOUND BRANCH", false);
        }

        Business business = optionalBranch.get().getBusiness();
        List<Branch> branchList = Collections.singletonList(optionalBranch.get());
        Optional<Currency> optionalCurrency = currencyRepository.findByBusinessId(business.getId());
        Currency currency = null;
        if (optionalCurrency.isPresent()) {
            currency = optionalCurrency.get();
        }

        try {
            List<ExcelDto> exportExcelDtoList = ExcelHelper.excelToTutorial(file.getInputStream());
            List<Product> productList = new ArrayList<>();
            List<FifoCalculation> fifoCalculationList = new ArrayList<>();
            List<Warehouse> warehouseList = new ArrayList<>();
            int count = 0;

            for (ExcelDto excelDto : exportExcelDtoList) {

                if (checkProduct(branchId, optionalBranch, fifoCalculationList, excelDto.getBarcode(), excelDto.getAmount(), excelDto.getBuyPrice())) {
                    continue;
                }

                if (excelDto.getName().isEmpty()) {
                    continue;
                }

                Measurement measurement = excelDto.getMeasurement() != null
                        ? measurementRepository.findByBusinessIdAndName(business.getId(), excelDto.getMeasurement()).orElse(null)
                        : null;
                Brand brand = excelDto.getBrand() != null
                        ? brandRepository.findByBusiness_IdAndName(business.getId(), excelDto.getBrand()).orElse(null)
                        : null;
                Category category = categoryRepository.findByBusiness_IdAndName(business.getId(), excelDto.getCategory()).orElse(null);

                Product product = new Product();
                product.setBusiness(business);
                product.setName(excelDto.getName());
                product.setExpireDate(excelDto.getExpiredDate());
                product.setBarcode(excelDto.getBarcode());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date dueDate = (excelDto.getExpiredDate() != null)
                        ? formatter.parse(formatter.format(excelDto.getExpiredDate()))
                        : new Date();
                product.setDueDate(dueDate);

                product.setMeasurement(measurement);
                product.setBrand(brand);
                product.setCategory(category);

                if (excelDto.getDollarBuy().equals("true")) {
                    assert currency != null;
                    product.setBuyPrice(currency.getCourse() * excelDto.getBuyPrice());
                    product.setBuyPriceDollar(excelDto.getBuyPrice());
                } else {
                    product.setBuyPrice(excelDto.getBuyPrice());
                    product.setBuyPriceDollar(Math.round(excelDto.getBuyPrice() / currency.getCourse() * 100) / 100.);

                }

                if (excelDto.getDollarSale().equals("true")) {
                    product.setSalePrice(currency.getCourse() * excelDto.getSalePrice());
                    product.setSalePriceDollar(excelDto.getSalePrice());
                    product.setGrossPrice(currency.getCourse() * excelDto.getWholeSale());
                    product.setGrossPriceDollar(excelDto.getWholeSale());
                } else {
                    product.setSalePrice(excelDto.getSalePrice());
                    product.setSalePriceDollar(Math.round(excelDto.getSalePrice() / currency.getCourse() * 100) / 100.);
                    product.setGrossPrice(excelDto.getWholeSale());
                    product.setGrossPriceDollar(Math.round(excelDto.getWholeSale() / currency.getCourse() * 100) / 100.);
                }
                product.setMinQuantity(excelDto.getAlertQuantity());
                product.setBranch(branchList);
                product.setGrossPrice(excelDto.getWholeSale());
                product.setTax(0);
                product.setActive(true);
                if (excelDto.getPhoto() == null) {
                    product.setPhoto(null);
                } else {
                    RestTemplate restTemplate = new RestTemplate();
                    Attachment attachment = new Attachment();
                    attachment.setName(UUID.randomUUID().toString());
                    attachment.setContentType("img/jpeg");
                    attachment.setFileOriginalName(UUID.randomUUID() + ".jpg");
                    Attachment save = attachmentRepository.save(attachment);
                    AttachmentContent content = new AttachmentContent();
                    content.setAttachment(save);
                    content.setMainContent(restTemplate.getForObject(excelDto.getPhoto(), byte[].class));
                    attachmentContentRepository.save(content);
                    product.setPhoto(save);
                }
                Warehouse warehouse = new Warehouse();
                warehouse.setBranch(optionalBranch.get());
                warehouse.setAmount(excelDto.getAmount());
                warehouse.setProduct(product);

                fifoCalculationList.add(new FifoCalculation(
                        optionalBranch.get(),
                        excelDto.getAmount(),
                        excelDto.getAmount(),
                        excelDto.getBuyPrice(),
                        new Date(),
                        product
                ));
                boolean checkingSize = true;
                boolean checkingColor = true;
                if (excelDto.getTypeSize() != null) {
                    checkingSize = false;
                }
                if (excelDto.getTypeColor() != null) {
                    checkingColor = false;
                }
                if (checkingSize && checkingColor) {
                    String typeSizes = excelDto.getTypeColor();
                    String typeColor = excelDto.getTypeSize();

                    double parseDouble = Double.parseDouble(typeSizes);
                    int typeSize = (int) parseDouble;
                    ProductTypeValue productTypeValueColor = productTypeValueRepository
                            .findAllByProductType_BusinessIdAndName(business.getId(), String.valueOf(typeSize))
                            .orElse(null);

                    ProductTypeValue productTypeValueSize = productTypeValueRepository
                            .findAllByProductType_BusinessIdAndName(business.getId(), typeColor)
                            .orElse(null);
                    assert productTypeValueColor != null;
                    if (productTypeValueSize != null) {
                        product.setName(product.getName() + " ( " + productTypeValueColor.getName() + " " + productTypeValueSize.getName() + " )");
                    } else {
                        product.setName(product.getName() + "( " + productTypeValueColor.getProductType().getName() + " - " + productTypeValueColor.getName() + " )");
                    }
                    productRepository.save(product);

                    Warehouse warehouse1 = new Warehouse();
                    warehouse1.setBranch(optionalBranch.get());
                    warehouse1.setAmount(excelDto.getAmount());
                    warehouseRepository.save(warehouse1);
                } else {
                    warehouseList.add(warehouse);
                    productList.add(product);
                }

            }

            if (!productList.isEmpty()) {
                productRepository.saveAll(productList);
                warehouseRepository.saveAll(warehouseList);
                fifoCalculationRepository.saveAll(fifoCalculationList);
                return new ApiResponse("Successfully Added " + count + " Product", true);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store excel data: " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return new ApiResponse();
    }

    private String generateBarcode(UUID businessId, String productName, UUID productId, boolean isUpdate) {
        String name = productName.toLowerCase();
        StringBuilder str = new StringBuilder(String.valueOf(System.currentTimeMillis()));
        str.append(name.charAt(0));
        str.reverse();
        String barcode = str.substring(0, 9);
        if (isUpdate) {
            if (productRepository.existsByBarcodeAndBusinessIdAndIdIsNotAndActiveTrue(barcode, businessId, productId))
                return generateBarcode(businessId, productName, productId, isUpdate);
        } else {
            if (productRepository.existsByBarcodeAndBusinessIdAndActiveTrue(barcode, businessId))
                return generateBarcode(businessId, productName, productId, isUpdate);
        }
        return barcode;
    }

    private boolean checkProduct(UUID branchId, Optional<Branch> optionalBranch, List<FifoCalculation> fifoCalculationList, String barcode, double amount, double buyPrice) {
        Optional<Product> optionalProduct = productRepository.findByBarcodeAndBranch_IdAndActiveTrue(barcode, branchId);
        if (optionalProduct.isPresent()) {
            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByBranchIdAndProductId(branchId, optionalProduct.get().getId());
            if (optionalWarehouse.isPresent()) {
                Warehouse warehouse = optionalWarehouse.get();
                warehouse.setAmount(amount + warehouse.getAmount());
                fifoCalculationList.add(
                        new FifoCalculation(
                                optionalBranch.get(),
                                amount,
                                amount,
                                buyPrice,
                                new Date(),
                                optionalProduct.get()
                        )
                );
            }
            return true;
        }
        return false;
    }

    public ResponseEntity<?> getCustomerTradeHistory(String customerID, Date startDate, Date endDate) {

        List<Trade> tradeList = tradeRepository.findAllByCreatedAtBetweenAndCustomer_Id(new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), UUID.fromString(customerID));
        List<ExcelCustomerTradeHistory> sales = new ArrayList<>();
        for (Trade trade : tradeList) {
            ExcelCustomerTradeHistory history = new ExcelCustomerTradeHistory();
//            history.setCustomerName(trade.getCustomer().getName());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = sdf.format(trade.getPayDate());
            history.setDate(formattedDate);
            history.setId(trade.getInvoice());
            for (TradeProduct tradeProduct : tradeProductRepository.findAllByTradeId(trade.getId())) {
                assert tradeProduct.getProduct() != null;
                history.getProducts().add(new ExcelProductTrade(tradeProduct.getProduct().getName(), tradeProduct.getProduct().getBarcode(), tradeProduct.getTradedQuantity(), tradeProduct.getTotalSalePrice() / tradeProduct.getTradedQuantity()));
            }
            sales.add(history);
        }
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sales");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Savdo Raqami");
            headerRow.createCell(2).setCellValue("Vaqti");
            headerRow.createCell(4).setCellValue("Haridor Nomi");
            headerRow.createCell(6).setCellValue("Mahsulot Nomi");
            headerRow.createCell(8).setCellValue("SHtrix Kodi");
            headerRow.createCell(10).setCellValue("Soni");
            headerRow.createCell(12).setCellValue("Summa/Donaga");

            // Populate the Excel sheet with sales data
            int rowIndex = 1;
            for (ExcelCustomerTradeHistory sale : sales) {
                for (ExcelProductTrade product : sale.getProducts()) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(sale.getId());
                    row.createCell(2).setCellValue(sale.getDate());
                    row.createCell(4).setCellValue(sale.getCustomerName());
                    row.createCell(6).setCellValue(product.getName());
                    row.createCell(8).setCellValue(product.getBarCode());
                    row.createCell(10).setCellValue(product.getCount());
                    row.createCell(12).setCellValue(product.getPerSum());
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=sales.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new ByteArrayResource(out.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    public HttpEntity<?> getCarInvoiceHistory(UUID carId) {

        try {
            Car car = carRepository.findById(carId).orElseThrow();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(car.getCarNumber());
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("HARAJAT TURI");
            headerRow.createCell(2).setCellValue("FILIAL");
            headerRow.createCell(4).setCellValue("HARAJAT MIQDORI");
            headerRow.createCell(6).setCellValue("TO'LOV TURI");
            headerRow.createCell(8).setCellValue("TO'LOV SANASI");
            headerRow.createCell(10).setCellValue("TAVSIF");
            headerRow.createCell(12).setCellValue("PROBEG");

            // Populate the Excel sheet with sales data
            Page<CarInvoice> invoices = carInvoiceRepository.findAllByCarIdOrderByCreatedAtDesc(car.getId(), PageRequest.of(0, 1));
            int rowIndex = 1;
            for (CarInvoice carInvoice : carInvoiceRepository.findAllByCarIdOrderByCreatedAtDesc(car.getId(), PageRequest.of(0, Math.toIntExact(invoices.getTotalElements())))) {
                Row row = sheet.createRow(rowIndex++);
                if (carInvoice.getType().equals(CarInvoiceType.INCOME)) {
                    row.createCell(0).setCellValue("KIRIM");
                } else if (carInvoice.getType().equals(CarInvoiceType.EXPENSIVE)) {
                    row.createCell(0).setCellValue("CHIQIM");
                }
                row.createCell(2).setCellValue(carInvoice.getBranch().getName());
                row.createCell(4).setCellValue(carInvoice.getAmount());
                row.createCell(6).setCellValue(carInvoice.getPaymentMethod().getType());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                row.createCell(8).setCellValue(dateFormat.format(carInvoice.getCreatedAt()));
                row.createCell(10).setCellValue(carInvoice.getDescription());
                row.createCell(12).setCellValue(carInvoice.getMileage());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + car.getCarNumber() + ".xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new ByteArrayResource(out.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}