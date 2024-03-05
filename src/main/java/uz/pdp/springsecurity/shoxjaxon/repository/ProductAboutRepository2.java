package uz.pdp.springsecurity.shoxjaxon.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.pdp.springsecurity.shoxjaxon.activity.ProductAbout2;

import java.util.Date;
import java.util.List;
import java.util.UUID; // Import UUID

@Repository
public class ProductAboutRepository2 {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductAboutRepository2(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductAbout2> getAllProductAboutData() {
        String sql = "SELECT pa.id, pa.created_at, pa.update_at, pa.amount, pa.description,\n" +
                "       pa.trade_id, pa.branch_id, pa.product_id, pa.product_type_price_id,\n" +
                "       b.name as branch_name, p.name as product_name, ptp.name as product_type_price_name,\n" +
                "       m.name as measurement_name\n" +  // Yangi qo'shilgan qism
                "FROM product_about pa\n" +
                "LEFT JOIN branches b ON pa.branch_id = b.id\n" +
                "LEFT JOIN product p ON pa.product_id = p.id\n" +
                "LEFT JOIN product_type_price ptp ON pa.product_type_price_id = ptp.id\n" +
                "LEFT JOIN measurement m ON p.measurement_id = m.id;";
                ; // <-- product nomini olish

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            ProductAbout2 productAbout2 = new ProductAbout2();

            // Qo'shimcha kod qismi
            String idString = resultSet.getString("id");
            if (idString != null) {
                productAbout2.setId(UUID.fromString(idString));
            } else {
                // Agar id null bo'lsa, kerakli harakatlarni bajarish
            }

            productAbout2.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            productAbout2.setUpdatedAt(resultSet.getTimestamp("update_at").toLocalDateTime());
            productAbout2.setAmount(resultSet.getDouble("amount"));
            productAbout2.setDescription(resultSet.getString("description"));

            // product_name nomini olish
            String productName = resultSet.getString("product_name");
            productAbout2.setProductName(productName);

            String branchName = resultSet.getString("branch_name");
            productAbout2.setBranchName(branchName);

            productAbout2.setProductTypeName(resultSet.getString("product_type_price_name"));
            productAbout2.setMeasurementName(resultSet.getString("measurement_name"));


            // trade_id uchun qo'shimcha kod qismi
            String tradeIdString = resultSet.getString("trade_id");
            if (tradeIdString != null) {
                productAbout2.setTradeId(UUID.fromString(tradeIdString));
            } else {
                productAbout2.setTradeId(UUID.randomUUID()); // Yangi UUID yaratish
            }

            // branch_id uchun qo'shimcha kod qismi
            String branchIdString = resultSet.getString("branch_id");
            if (branchIdString != null) {
                productAbout2.setBranchId(UUID.fromString(branchIdString));
            } else {
                productAbout2.setBranchId(UUID.randomUUID()); // Yangi UUID yaratish
            }

            // product_type_price_id uchun qo'shimcha kod qismi
            String productTypePriceIdString = resultSet.getString("product_type_price_id");
            if (productTypePriceIdString != null) {
                productAbout2.setProductTypePriceId(UUID.fromString(productTypePriceIdString));
            } else {
                productAbout2.setProductTypePriceId(UUID.randomUUID()); // Yangi UUID yaratish
            }

            return productAbout2;
        });


    }

    public List<ProductAbout2> getAllDescriptionsWithKeyword(String keyword, UUID branchId) {
        String sql = "SELECT pa.id, pa.created_at, pa.update_at, pa.amount, pa.description,\n" +
                "       pa.trade_id, pa.branch_id, pa.product_id, pa.product_type_price_id,\n" +
                "       b.name as branch_name, p.name as product_name, ptp.name as product_type_price_name,\n" +
                "       m.name as measurement_name\n" +
                "FROM product_about pa\n" +
                "LEFT JOIN branches b ON pa.branch_id = b.id\n" +
                "LEFT JOIN product p ON pa.product_id = p.id\n" +
                "LEFT JOIN product_type_price ptp ON pa.product_type_price_id = ptp.id\n" +
                "LEFT JOIN measurement m ON p.measurement_id = m.id\n" +
                "WHERE pa.description LIKE ? AND pa.branch_id = cast(? as uuid);";

        return jdbcTemplate.query(sql, new Object[]{"%" + keyword + "%",  branchId.toString()}, (resultSet, rowNum) -> {
            ProductAbout2 productAbout2 = new ProductAbout2();

            // Qo'shimcha kod qismi
            String idString = resultSet.getString("id");
            if (idString != null) {
                productAbout2.setId(UUID.fromString(idString));
            } else {
                // Agar id null bo'lsa, kerakli harakatlarni bajarish
            }

            productAbout2.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            productAbout2.setUpdatedAt(resultSet.getTimestamp("update_at").toLocalDateTime());
            productAbout2.setAmount(resultSet.getDouble("amount"));
            productAbout2.setDescription(resultSet.getString("description"));

            // product_name nomini olish
            String productName = resultSet.getString("product_name");
            productAbout2.setProductName(productName);

            String branchName = resultSet.getString("branch_name");
            productAbout2.setBranchName(branchName);

            productAbout2.setProductTypeName(resultSet.getString("product_type_price_name"));
            productAbout2.setMeasurementName(resultSet.getString("measurement_name"));

            // trade_id uchun qo'shimcha kod qismi
            String tradeIdString = resultSet.getString("trade_id");
            if (tradeIdString != null) {
                productAbout2.setTradeId(UUID.fromString(tradeIdString));
            } else {
                productAbout2.setTradeId(UUID.randomUUID()); // Yangi UUID yaratish
            }

            // branch_id uchun qo'shimcha kod qismi
            String branchIdString = resultSet.getString("branch_id");
            if (branchIdString != null) {
                productAbout2.setBranchId(UUID.fromString(branchIdString));
            } else {
                productAbout2.setBranchId(UUID.randomUUID()); // Yangi UUID yaratish
            }

            // product_type_price_id uchun qo'shimcha kod qismi
            String productTypePriceIdString = resultSet.getString("product_type_price_id");
            if (productTypePriceIdString != null) {
                productAbout2.setProductTypePriceId(UUID.fromString(productTypePriceIdString));
            } else {
                productAbout2.setProductTypePriceId(UUID.randomUUID()); // Yangi UUID yaratish
            }

            return productAbout2;
        });
    }



    public List<ProductAbout2> getAllDescriptionsHomashyo(String keyword, UUID branchId) {
        String sql = "SELECT pa.id, pa.created_at, pa.update_at, pa.amount, pa.description,\n" +
                "       pa.trade_id, pa.branch_id, pa.product_id, pa.product_type_price_id,\n" +
                "       b.name as branch_name, p.name as product_name, ptp.name as product_type_price_name,\n" +
                "       m.name as measurement_name\n" +
                "FROM product_about pa\n" +
                "LEFT JOIN branches b ON pa.branch_id = b.id\n" +
                "LEFT JOIN product p ON pa.product_id = p.id\n" +
                "LEFT JOIN product_type_price ptp ON pa.product_type_price_id = ptp.id\n" +
                "LEFT JOIN measurement m ON p.measurement_id = m.id\n" +
                "WHERE pa.description LIKE ? AND pa.branch_id = cast(? as uuid);";

        return jdbcTemplate.query(sql, new Object[]{"%" + keyword + "%",  branchId.toString()}, (resultSet, rowNum) -> {
            ProductAbout2 productAbout2 = new ProductAbout2();

            // Qo'shimcha kod qismi
            String idString = resultSet.getString("id");
            if (idString != null) {
                productAbout2.setId(UUID.fromString(idString));
            } else {
                // Agar id null bo'lsa, kerakli harakatlarni bajarish
            }

            productAbout2.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            productAbout2.setUpdatedAt(resultSet.getTimestamp("update_at").toLocalDateTime());
            productAbout2.setAmount(resultSet.getDouble("amount"));
            productAbout2.setDescription(resultSet.getString("description"));
            productAbout2.setMeasurementName(resultSet.getString("measurement_name"));

            // product_name nomini olish
            String productName = resultSet.getString("product_name");
            productAbout2.setProductName(productName);

            String branchName = resultSet.getString("branch_name");
            productAbout2.setBranchName(branchName);

            productAbout2.setProductTypeName(resultSet.getString("product_type_price_name"));

            // trade_id uchun qo'shimcha kod qismi
            String tradeIdString = resultSet.getString("trade_id");
            if (tradeIdString != null) {
                productAbout2.setTradeId(UUID.fromString(tradeIdString));
            } else {
                productAbout2.setTradeId(UUID.randomUUID()); // Yangi UUID yaratish
            }

            // branch_id uchun qo'shimcha kod qismi
            String branchIdString = resultSet.getString("branch_id");
            if (branchIdString != null) {
                productAbout2.setBranchId(UUID.fromString(branchIdString));
            } else {
                productAbout2.setBranchId(UUID.randomUUID()); // Yangi UUID yaratish
            }

            // product_type_price_id uchun qo'shimcha kod qismi
            String productTypePriceIdString = resultSet.getString("product_type_price_id");
            if (productTypePriceIdString != null) {
                productAbout2.setProductTypePriceId(UUID.fromString(productTypePriceIdString));
            } else {
                productAbout2.setProductTypePriceId(UUID.randomUUID()); // Yangi UUID yaratish
            }

            return productAbout2;
        });
    }



    public List<ProductAbout2> getAllDescriptionsBrak(String keyword, UUID branchId) {
        String sql = "SELECT " +
                "pa.id, " +
                "pa.created_at, " +
                "pa.update_at, " +
                "pa.amount, " +
                "pa.description, " +
                "pa.trade_id, " +
                "pa.branch_id, " +
                "pa.product_id, " +
                "pa.product_type_price_id, " +
                "b.name as branch_name, " +
                "p.name as product_name, " +
                "ptp.name as product_type_price_name, " +
                "m.name as measurement_name " +
                "FROM product_about pa " +
                "LEFT JOIN branches b ON pa.branch_id = b.id " +
                "LEFT JOIN product p ON pa.product_id = p.id " +
                "LEFT JOIN product_type_price ptp ON pa.product_type_price_id = ptp.id " +
                "LEFT JOIN measurement m ON p.measurement_id = m.id " +
                "WHERE pa.description LIKE ? AND pa.branch_id = cast(? as uuid);";



        return jdbcTemplate.query(sql, new Object[]{"%" + keyword + "%",  branchId.toString()}, (resultSet, rowNum) -> {
            ProductAbout2 productAbout2 = new ProductAbout2();

            // Qo'shimcha kod qismi
            String idString = resultSet.getString("id");
            if (idString != null) {
                productAbout2.setId(UUID.fromString(idString));
            } else {
                // Agar id null bo'lsa, kerakli harakatlarni bajarish
            }

            productAbout2.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            productAbout2.setUpdatedAt(resultSet.getTimestamp("update_at").toLocalDateTime());
            productAbout2.setAmount(resultSet.getDouble("amount"));
            productAbout2.setDescription(resultSet.getString("description"));

            // product_name nomini olish
            String productName = resultSet.getString("product_name");
            productAbout2.setProductName(productName);

            String branchName = resultSet.getString("branch_name");
            productAbout2.setBranchName(branchName);

            productAbout2.setProductTypeName(resultSet.getString("product_type_price_name"));

            // trade_id uchun qo'shimcha kod qismi
            String tradeIdString = resultSet.getString("trade_id");
            if (tradeIdString != null) {
                productAbout2.setTradeId(UUID.fromString(tradeIdString));
            } else {
                productAbout2.setTradeId(UUID.randomUUID()); // Yangi UUID yaratish
            }

            // branch_id uchun qo'shimcha kod qismi
            String branchIdString = resultSet.getString("branch_id");
            if (branchIdString != null) {
                productAbout2.setBranchId(UUID.fromString(branchIdString));
            } else {
                productAbout2.setBranchId(UUID.randomUUID()); // Yangi UUID yaratish
            }
            productAbout2.setMeasurementName(resultSet.getString("measurement_name"));

            // product_type_price_id uchun qo'shimcha kod qismi
            String productTypePriceIdString = resultSet.getString("product_type_price_id");
            if (productTypePriceIdString != null) {
                productAbout2.setProductTypePriceId(UUID.fromString(productTypePriceIdString));
            } else {
                productAbout2.setProductTypePriceId(UUID.randomUUID()); // Yangi UUID yaratish
            }

            return productAbout2;
        });


    }

    public List<ProductAbout2> getAllDescriptionsSotuv(String keyword, UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT " +
                "pa.id, " +
                "pa.created_at, " +
                "pa.update_at, " +
                "pa.amount, " +
                "pa.description, " +
                "pa.trade_id, " +
                "pa.branch_id, " +
                "pa.product_id, " +
                "pa.product_type_price_id, " +
                "b.name as branch_name, " +
                "p.name as product_name, " +
                "ptp.name as product_type_price_name, " +
                "m.name as measurement_name " +
                "FROM product_about pa " +
                "LEFT JOIN branches b ON pa.branch_id = b.id " +
                "LEFT JOIN product p ON pa.product_id = p.id " +
                "LEFT JOIN product_type_price ptp ON pa.product_type_price_id = ptp.id " +
                "LEFT JOIN measurement m ON p.measurement_id = m.id " +
                "WHERE pa.description LIKE ? AND pa.branch_id = cast(? as uuid) " +
                "AND pa.created_at BETWEEN ? AND ?";



        return jdbcTemplate.query(sql, new Object[]{"%" + keyword + "%", branchId.toString(), startDate, endDate}, (resultSet, rowNum) -> {
            ProductAbout2 productAbout2 = new ProductAbout2();


            // Qo'shimcha kod qismi
            String idString = resultSet.getString("id");
            if (idString != null) {
                productAbout2.setId(UUID.fromString(idString));
            } else {
                // Agar id null bo'lsa, kerakli harakatlarni bajarish
            }

            productAbout2.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            productAbout2.setUpdatedAt(resultSet.getTimestamp("update_at").toLocalDateTime());
            productAbout2.setAmount(resultSet.getDouble("amount"));
            productAbout2.setDescription(resultSet.getString("description"));

            // product_name nomini olish
            String productName = resultSet.getString("product_name");
            productAbout2.setProductName(productName);

            String branchName = resultSet.getString("branch_name");
            productAbout2.setBranchName(branchName);

            productAbout2.setProductTypeName(resultSet.getString("product_type_price_name"));

            // trade_id uchun qo'shimcha kod qismi
            String tradeIdString = resultSet.getString("trade_id");
            if (tradeIdString != null) {
                productAbout2.setTradeId(UUID.fromString(tradeIdString));
            } else {
                productAbout2.setTradeId(UUID.randomUUID()); // Yangi UUID yaratish
            }

            // branch_id uchun qo'shimcha kod qismi
            String branchIdString = resultSet.getString("branch_id");
            if (branchIdString != null) {
                productAbout2.setBranchId(UUID.fromString(branchIdString));
            } else {
                productAbout2.setBranchId(UUID.randomUUID()); // Yangi UUID yaratish
            }
            productAbout2.setMeasurementName(resultSet.getString("measurement_name"));

            // product_type_price_id uchun qo'shimcha kod qismi
            String productTypePriceIdString = resultSet.getString("product_type_price_id");
            if (productTypePriceIdString != null) {
                productAbout2.setProductTypePriceId(UUID.fromString(productTypePriceIdString));
            } else {
                productAbout2.setProductTypePriceId(UUID.randomUUID()); // Yangi UUID yaratish
            }

            return productAbout2;
        });
    }







}
