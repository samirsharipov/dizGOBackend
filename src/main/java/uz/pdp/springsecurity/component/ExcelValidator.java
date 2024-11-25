package uz.pdp.springsecurity.component;

public class ExcelValidator {
    public static void validateRequiredField(String value, String fieldName, int rowIndex) throws Exception {
        if (value == null || value.isEmpty()) {
            throw new Exception("Majburiy maydon bo'sh: " + fieldName + ", qator: " + rowIndex);
        }
    }

    public static Double parseDouble(String value, String fieldName, int rowIndex) throws Exception {
        try {
            return value.isEmpty() ? null : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new Exception("Raqamli qiymat noto'g'ri formatda: " + fieldName + ", qator: " + rowIndex);
        }
    }
}
