package uz.dizgo.erp.shoxjaxon.repository;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import uz.dizgo.erp.shoxjaxon.activity.Customer2;

import java.math.BigDecimal;
import java.util.*;

@Repository // Add this annotation

public class CustomerRepository2 {

    private final JdbcOperations jdbcTemplate;

    public CustomerRepository2(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Customer2> getCustomersByBusinessId(UUID businessId) {
        String sql = "SELECT c.update_at, c.active, c.birthday, c.debt, c.description, c.lid_customer, c.name, " +
                "c.pay_date, c.phone_number, c.telegram, c.branch_id, c.business_id, c.customer_group_id, b.name as branch_name " +
                "FROM customer c " +
                "LEFT JOIN branches b ON c.branch_id = b.id " +  // Join with branches table
                "WHERE c.business_id = ?";

        return jdbcTemplate.query(sql, new Object[]{businessId}, (resultSet, rowNum) -> {
            Customer2 customer2 = new Customer2();

            customer2.setUpdateAt(resultSet.getTimestamp("update_at").toLocalDateTime());
            customer2.setActive(resultSet.getBoolean("active"));
            java.sql.Date birthdayDate = resultSet.getDate("birthday");
            customer2.setBirthday(birthdayDate != null ? birthdayDate.toLocalDate() : null);
            customer2.setDebt(resultSet.getDouble("debt"));
            customer2.setDescription(resultSet.getString("description"));
            customer2.setLidCustomer(resultSet.getString("lid_customer"));
            customer2.setName(resultSet.getString("name"));
            java.sql.Date payDateDate = resultSet.getDate("pay_date");
            customer2.setPayDate(payDateDate != null ? payDateDate.toLocalDate() : null);
            customer2.setPhoneNumber(resultSet.getString("phone_number"));
            customer2.setTelegram(resultSet.getString("telegram"));
            customer2.setBranchId(UUID.fromString(resultSet.getString("branch_id")));
            customer2.setBusinessId(UUID.fromString(resultSet.getString("business_id")));
            String customerGroupIdString = resultSet.getString("customer_group_id");
            customer2.setCustomerGroupId(
                    customerGroupIdString != null ? UUID.fromString(customerGroupIdString) : null
            );
            customer2.setBranchName(resultSet.getString("branch_name"));

            return customer2;
        });
    }

    public Integer getTotalCustomersForBusiness(UUID businessId) {
        String sql = "SELECT COUNT(*) FROM customer WHERE business_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, businessId);
    }



    public Integer getTotalCustomersForBranch(UUID branchId) {
        String sql = "SELECT COUNT(*) FROM customer_branches WHERE branches_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, branchId);
    }

    public Integer getTotalSuppliersForBusiness(UUID businessId) {
        String sql = "SELECT COUNT(*) FROM supplier WHERE business_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, businessId);
    }

    public Integer getTotalTradesForBranch(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM trade WHERE branch_id = ? AND created_at BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{branchId, startDate, endDate}, Integer.class);
    }

    public Integer getTotalTradesForBusiness(UUID businessId, Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM trade WHERE branch_id IN (SELECT id FROM branches WHERE business_id = ?) AND created_at BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{businessId, startDate, endDate}, Integer.class);
    }

    public Integer getTotalHaridForBranch(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM purchase WHERE branch_id = ? AND created_at BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{branchId, startDate, endDate}, Integer.class);
    }

    public Integer getTotalHaridForBusiness(UUID businessId, Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM purchase WHERE branch_id IN (SELECT id FROM branches WHERE business_id = ?) AND created_at BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{businessId, startDate, endDate}, Integer.class);
    }

    public Integer getTotalIshlaForBranch(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM purchase WHERE branch_id = ? AND created_at BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{branchId, startDate, endDate}, Integer.class);
    }

    public Integer getTotalIshlaForBusiness(UUID businessId, Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM purchase WHERE branch_id IN (SELECT id FROM branches WHERE business_id = ?) AND created_at BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{businessId, startDate, endDate}, Integer.class);
    }

    public Integer getTotalXarForBranch(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM outlay WHERE branch_id = ? AND created_at BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{branchId, startDate, endDate}, Integer.class);
    }

    public Integer getTotalXarForBusiness(UUID businessId, Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM outlay WHERE branch_id IN (SELECT id FROM branches WHERE business_id = ?) AND created_at BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{businessId, startDate, endDate}, Integer.class);
    }


    public Map<String, BigDecimal> getTotalTayyorDetailsForBranch(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT SUM(content_price) AS totalContentPrice, SUM(cost) AS totalCost, SUM(total_price) AS totalPrice FROM production WHERE branch_id = ? AND created_at BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{branchId, startDate, endDate}, (rs, rowNum) -> {
            Map<String, BigDecimal> result = new HashMap<>();
            result.put("totalContentPrice", rs.getBigDecimal("totalContentPrice"));
            result.put("totalCost", rs.getBigDecimal("totalCost"));
            result.put("totalPrice", rs.getBigDecimal("totalPrice"));
            return result;
        });
    }

    public Map<String, BigDecimal> getTotalTayyorDetailsForBusiness(UUID businessId, Date startDate, Date endDate) {
        String sql = "SELECT SUM(content_price) AS totalContentPrice, SUM(cost) AS totalCost, SUM(total_price) AS totalPrice FROM production WHERE branch_id IN (SELECT id FROM branches WHERE business_id = ?) AND created_at BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{businessId, startDate, endDate}, (rs, rowNum) -> {
            Map<String, BigDecimal> result = new HashMap<>();
            result.put("totalContentPrice", rs.getBigDecimal("totalContentPrice"));
            result.put("totalCost", rs.getBigDecimal("totalCost"));
            result.put("totalPrice", rs.getBigDecimal("totalPrice"));
            return result;
        });
    }

    public List<Map<String, Object>> getTotalCostsByTypeForBranch(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT ct.name as costType, SUM(c.sum) as totalSum " +
                "FROM cost c " +
                "JOIN cost_type ct ON c.cost_type_id = ct.id " +
                "WHERE ct.branch_id = ? AND c.created_at BETWEEN ? AND ? " +
                "GROUP BY ct.name";
        return jdbcTemplate.query(sql, new Object[]{branchId, startDate, endDate}, (rs, rowNum) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("costType", rs.getString("costType"));
            result.put("totalSum", rs.getBigDecimal("totalSum"));
            return result;
        });
    }

    public List<Map<String, Object>> getTotalCostsByTypeForBusiness(UUID businessId, Date startDate, Date endDate) {
        String sql = "SELECT ct.name as costType, SUM(c.sum) as totalSum " +
                "FROM cost c " +
                "JOIN cost_type ct ON c.cost_type_id = ct.id " +
                "WHERE ct.branch_id IN (SELECT id FROM branches WHERE business_id = ?) " +
                "AND c.created_at BETWEEN ? AND ? " +
                "GROUP BY ct.name";
        return jdbcTemplate.query(sql, new Object[]{businessId, startDate, endDate}, (rs, rowNum) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("costType", rs.getString("costType"));
            result.put("totalSum", rs.getBigDecimal("totalSum"));
            return result;
        });
    }






}
