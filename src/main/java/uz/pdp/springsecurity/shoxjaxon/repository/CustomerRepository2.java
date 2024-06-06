package uz.pdp.springsecurity.shoxjaxon.repository;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import uz.pdp.springsecurity.shoxjaxon.activity.Customer2;

import java.util.Date;
import java.util.List;
import java.util.UUID;
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


}
