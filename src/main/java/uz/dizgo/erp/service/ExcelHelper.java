package uz.dizgo.erp.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import uz.dizgo.erp.payload.ExcelDto;
import uz.dizgo.erp.payload.ExportExcelDto;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static String TYPE2 = "application/octet-stream";
    public static String SHEET = "product";

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType()) || !TYPE2.equals(file.getContentType())) {
            file.getContentType();
            return false;
        }
        return true;
    }

    public static List<ExportExcelDto> excelToTutorials(InputStream is) {

        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<ExportExcelDto> exportExcelDtoList = new ArrayList<ExportExcelDto>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row row = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = row.iterator();

                ExportExcelDto exportExcelDto = new ExportExcelDto();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            exportExcelDto.setProductName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            exportExcelDto.setBuyPrice(currentCell.getNumericCellValue());
                            break;
                        case 2:
                            exportExcelDto.setSalePrice(currentCell.getNumericCellValue());
                            break;
                        case 3:
                            exportExcelDto.setAmount(currentCell.getNumericCellValue());
                            break;
                        case 4:
                            exportExcelDto.setMinQuantity(currentCell.getNumericCellValue());
                            break;
                        case 5:
                            exportExcelDto.setExpiredDate(currentCell.getDateCellValue());
                            break;
                        case 6:
                            currentCell.setCellType(CellType.STRING);
                            exportExcelDto.setBarcode(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                exportExcelDtoList.add(exportExcelDto);
                workbook.close();
            }
            return exportExcelDtoList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static boolean hasExcelFormats(MultipartFile file) {

        if (!TYPE.equals(file.getContentType()) || !TYPE2.equals(file.getContentType())) {
            file.getContentType();
            return false;
        }
        return true;
    }

    public static List<ExcelDto> excelToTutorial(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<ExcelDto> excelDtoList = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row row = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = row.iterator();

                ExcelDto exportExcelDto = new ExcelDto();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            exportExcelDto.setName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            exportExcelDto.setBuyPrice(currentCell.getNumericCellValue());
                            break;
                        case 2:
                            exportExcelDto.setSalePrice(currentCell.getNumericCellValue());
                            break;
                        case 3:
                            exportExcelDto.setWholeSale(currentCell.getNumericCellValue());
                            break;
                        case 4:
                            exportExcelDto.setDollarBuy(currentCell.getStringCellValue());
                            break;
                        case 5:
                            exportExcelDto.setDollarSale(currentCell.getStringCellValue());
                            break;
                        case 6:
                            exportExcelDto.setAmount(currentCell.getNumericCellValue());
                            break;
                        case 7:
                            exportExcelDto.setAlertQuantity(currentCell.getNumericCellValue());
                            break;
                        case 8:
                            exportExcelDto.setMeasurement(currentCell.getStringCellValue());
                            break;
                        case 9:
                            if (currentCell.getCellType() == CellType.STRING) {
                                exportExcelDto.setTypeSize(currentCell.getStringCellValue());
                            } else if (currentCell.getCellType() == CellType.NUMERIC) {
                                double numericValue = currentCell.getNumericCellValue();
                                exportExcelDto.setTypeSize(String.valueOf(numericValue));
                            }
                            break;
                        case 10:
                            if (currentCell.getCellType() == CellType.STRING) {
                                exportExcelDto.setTypeColor(currentCell.getStringCellValue());
                            } else if (currentCell.getCellType() == CellType.NUMERIC) {
                                double numericValue = currentCell.getNumericCellValue();
                                exportExcelDto.setTypeColor(String.valueOf(numericValue));
                            }
                            break;
                        case 11:
                            exportExcelDto.setBrand(currentCell.getStringCellValue());
                            break;
                        case 12:
                            exportExcelDto.setCategory(currentCell.getStringCellValue());
                            break;
                        case 13:
                            if (currentCell.getCellType() == CellType.STRING) {
                                exportExcelDto.setBarcode(currentCell.getStringCellValue());
                            } else if (currentCell.getCellType() == CellType.NUMERIC) {
                                double numericValue = currentCell.getNumericCellValue();
                                exportExcelDto.setBarcode(String.valueOf(numericValue));
                            }
                            break;
                        case 14:
                            exportExcelDto.setExpiredDate(currentCell.getDateCellValue());
                            break;
                        case 15:
                            exportExcelDto.setPhoto(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                excelDtoList.add(exportExcelDto);
            }
            workbook.close();
            return excelDtoList;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }
}