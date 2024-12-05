package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Customer;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.CustomerRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerExcelService {

    private final CustomerRepository customerRepository;
    private final BranchRepository branchRepository;

    public ApiResponse importCustomersFromExcel(MultipartFile file, UUID branchId) throws IOException {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("Branch not found");
        }

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip the header row
                }

                try {
                    Customer customer = new Customer();
//                    customer.setName(getStringValueFromCell(row.getCell(0)));
//                    customer.setPhoneNumber(getStringValueFromCell(row.getCell(1)));
//                    customer.setTelegram(getStringValueFromCell(row.getCell(2)));

                    Cell debtCell = row.getCell(3);
                    if (debtCell != null && debtCell.getCellType() == CellType.NUMERIC) {
                        customer.setDebt(debtCell.getNumericCellValue());
                    } else {
                        customer.setDebt(0);
                    }

                    customer.setCustomerGroup(null);

                    Cell payDateCell = row.getCell(4);
                    if (payDateCell != null && payDateCell.getCellType() == CellType.NUMERIC) {
                        customer.setPayDate(payDateCell.getDateCellValue());
                    } else {
                        customer.setPayDate(null);
                    }

//                    customer.setBusiness(optionalBranch.get().getBusiness());
//                    customer.setBranch(optionalBranch.get());
//                    customer.setBranches(List.of(optionalBranch.get()));
                    customerRepository.save(customer);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ApiResponse("Failed", false);
                }
            }
            return new ApiResponse("Saved", true);
        }
    }

    private String getStringValueFromCell(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCellType() == CellType.NUMERIC) {
                return NumberToTextConverter.toText(cell.getNumericCellValue());
            }
        }
        return null;
    }

    public byte[] exportCustomersToExcel(UUID branchId) throws IOException {
        List<Customer> customers = customerRepository.findAllByBranchesIdAndActiveIsTrueOrBranchesIdAndActiveIsNull(branchId, branchId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Customers");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Name");
        header.createCell(1).setCellValue("Phone Number");
        header.createCell(2).setCellValue("Telegram");
        header.createCell(3).setCellValue("Debt");
        header.createCell(4).setCellValue("Pay Date");
        header.createCell(5).setCellValue("Customer Group Name");
        header.createCell(6).setCellValue("Branch Name");

        header.setHeightInPoints(30);


        int rowNum = 1;
        for (Customer customer : customers) {
            Row row = sheet.createRow(rowNum++);
//            row.createCell(0).setCellValue(customer.getName());
//            row.createCell(1).setCellValue(customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "");
//            row.createCell(2).setCellValue(customer.getTelegram() != null ? customer.getTelegram() : "");
            row.createCell(3).setCellValue(customer.getDebt() != 0 ? customer.getDebt() : 0);
            Cell payDateCell = row.createCell(4);
            if (customer.getPayDate() != null) {
                payDateCell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(customer.getPayDate()));
            } else {
                payDateCell.setCellValue("");
            }
            payDateCell.setCellType(CellType.STRING);
            row.createCell(5).setCellValue(customer.getCustomerGroup() != null && customer.getCustomerGroup().getName() != null ? customer.getCustomerGroup().getName() : "");
//            row.createCell(6).setCellValue(customer.getBranch().getName() != null ? customer.getBranch().getName() : "");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);

        return baos.toByteArray();
    }
}
