package uz.pdp.springsecurity.configuration;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uz.pdp.springsecurity.payload.ProductViewDtos;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ExcelGenerator {

    private final List<ProductViewDtos> productViewDtosList;

    private final XSSFWorkbook workbook;

    private XSSFSheet sheet;

    public ExcelGenerator(List <ProductViewDtos> productViewDtosList) {
        this.productViewDtosList = productViewDtosList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Product");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "name", style);
        createCell(row, 1, "branch", style);
        createCell(row, 2, "buy price", style);
        createCell(row, 3, "sale price", style);
        createCell(row, 4, "amount", style);
        createCell(row, 5, "brand", style);
        createCell(row, 6, "alert quantity", style);
        createCell(row, 7, "expired date", style);
        createCell(row, 8, "barcode", style);
        createCell(row, 9, "measurement", style);
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Double) {
            cell.setCellValue((Double) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (ProductViewDtos record: productViewDtosList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.getProductName(), style);
            createCell(row, columnCount++, record.getBranch(), style);
            createCell(row, columnCount++, record.getBuyPrice(), style);
            createCell(row, columnCount++, record.getSalePrice(), style);
            createCell(row, columnCount++, record.getAmount(), style);
            createCell(row, columnCount++, record.getBrandName(), style);
            createCell(row, columnCount++, record.getMinQuantity(), style);
            createCell(row, columnCount++, record.getExpiredDate(), style);
            createCell(row, columnCount++, record.getBarcode(), style);
            createCell(row, columnCount++, record.getMeasurementId(), style);
        }
    }

    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
