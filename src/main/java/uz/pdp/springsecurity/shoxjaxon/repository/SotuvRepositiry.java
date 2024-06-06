package uz.pdp.springsecurity.shoxjaxon.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.pdp.springsecurity.shoxjaxon.BusinessTotalSum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class SotuvRepositiry {

    private static final Logger logger = LoggerFactory.getLogger(SotuvRepositiry.class);


    private final JdbcTemplate jdbcTemplate;

    public SotuvRepositiry(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal[] calculateTotalDebtSumForBranchWithDateRange(UUID branchId) {
        String sql = "SELECT " +
                "SUM(CASE WHEN t.dollar_trade = false THEN COALESCE(dc.dollar_price, t.debt_sum) ELSE 0 END) AS total_sum_debt_sum, " +
                "SUM(CASE WHEN t.dollar_trade = true THEN COALESCE(dc.dollar_price, t.debt_sum_dollar) ELSE 0 END) AS total_sum_debt_sum_dollar " +
                "FROM trade t " +
                "LEFT JOIN debt_canculs dc ON t.id = dc.trade_id " +
                "WHERE t.branch_id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            BigDecimal totalSumDebtSum = rs.getBigDecimal("total_sum_debt_sum");
            BigDecimal totalSumDebtSumDollar = rs.getBigDecimal("total_sum_debt_sum_dollar");
            return new BigDecimal[]{totalSumDebtSum, totalSumDebtSumDollar};
        }, branchId);
    }

    public BigDecimal calculateTotalDollarXarajatForBranch(UUID branchId) {
        String sql = "SELECT SUM(total_sum) AS total_dollar_xarajat_for_branch " +
                "FROM outlay " +
                "WHERE branch_id = ? " +
                "AND dollar_outlay = true";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, branchId);
    }

    public BigDecimal calculateTotalDollarXarajatForBranchWithDateRange(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT SUM(total_sum) AS total_dollar_xarajat_for_branch " +
                "FROM outlay " +
                "WHERE branch_id = ? " +
                "AND dollar_outlay = true " +
                "AND created_at BETWEEN ? AND ?";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, branchId, startDate, endDate);
    }

    public BigDecimal[] calculateTotalDebtSumForBranchWithDateRangeFiltered(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT " +
                "SUM(CASE WHEN t.dollar_trade = false THEN COALESCE(dc.dollar_price, t.debt_sum) ELSE 0 END) AS total_sum_debt_sum, " +
                "SUM(CASE WHEN t.dollar_trade = true THEN COALESCE(dc.dollar_price, t.debt_sum_dollar) ELSE 0 END) AS total_sum_debt_sum_dollar " +
                "FROM trade t " +
                "LEFT JOIN debt_canculs dc ON t.id = dc.trade_id " +
                "WHERE t.branch_id = ? " +
                "AND t.created_at BETWEEN ? AND ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            BigDecimal totalSumDebtSum = rs.getBigDecimal("total_sum_debt_sum");
            BigDecimal totalSumDebtSumDollar = rs.getBigDecimal("total_sum_debt_sum_dollar");
            return new BigDecimal[]{totalSumDebtSum, totalSumDebtSumDollar};
        }, branchId, startDate, endDate);
    }

    public BigDecimal[] calculateTotalSumForBranchWithDateRange(UUID branchId) {
        String sql = "SELECT " +
                "SUM(CASE WHEN t.dollar_trade = false THEN COALESCE(dc.dollar_price, t.paid_sum) ELSE 0 END) AS total_sum_paid_sum, " +
                "SUM(CASE WHEN t.dollar_trade = true THEN COALESCE(dc.dollar_price, t.paid_sum_dollar) ELSE 0 END) AS total_sum_paid_sum_dollar " +
                "FROM trade t " +
                "LEFT JOIN debt_canculs dc ON t.id = dc.trade_id " +
                "WHERE t.branch_id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            BigDecimal totalSumPaidSum = rs.getBigDecimal("total_sum_paid_sum");
            BigDecimal totalSumPaidSumDollar = rs.getBigDecimal("total_sum_paid_sum_dollar");
            return new BigDecimal[]{totalSumPaidSum, totalSumPaidSumDollar};
        }, branchId);
    }


    public BigDecimal[] calculateTotalSumForBranchWithDateRangeFiltered(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT " +
                "SUM(CASE WHEN t.dollar_trade = false THEN COALESCE(dc.dollar_price, t.paid_sum) ELSE 0 END) AS total_sum_paid_sum, " +
                "SUM(CASE WHEN t.dollar_trade = true THEN COALESCE(dc.dollar_price, t.paid_sum_dollar) ELSE 0 END) AS total_sum_paid_sum_dollar " +
                "FROM trade t " +
                "LEFT JOIN debt_canculs dc ON t.id = dc.trade_id " +
                "WHERE t.branch_id = ? " +
                "AND t.created_at BETWEEN ? AND ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            BigDecimal totalSumPaidSum = rs.getBigDecimal("total_sum_paid_sum");
            BigDecimal totalSumPaidSumDollar = rs.getBigDecimal("total_sum_paid_sum_dollar");
            return new BigDecimal[]{totalSumPaidSum, totalSumPaidSumDollar};
        }, branchId, startDate, endDate);
    }




    public BigDecimal calculateTotalFoydaForBranch(UUID branchId) {
        try {
            String sql = "SELECT COALESCE(SUM(total_profit), 0) AS total_foyda_for_branch " +
                    "FROM trade " +
                    "WHERE branch_id = ? AND NOT total_profit IS NULL AND NOT total_profit = 'NaN'";
            return jdbcTemplate.queryForObject(sql, BigDecimal.class, branchId);
        } catch (Exception e) {
            logger.error("Unexpected error calculating total foyda for branchId: " + branchId, e);
            throw new RuntimeException("Error calculating total foyda for branchId: " + branchId, e);
        }
    }

    public BigDecimal calculateTotalFoydaForBranchWithDateRange(UUID branchId, Date startDate, Date endDate) {
        try {
            String sql = "SELECT COALESCE(SUM(total_profit), 0) AS total_foyda_for_branch " +
                    "FROM trade " +
                    "WHERE branch_id = ? AND created_at BETWEEN ? AND ? AND NOT total_profit IS NULL AND NOT total_profit = 'NaN'";
            return jdbcTemplate.queryForObject(sql, BigDecimal.class, branchId, startDate, endDate);
        } catch (Exception e) {
            logger.error("Unexpected error calculating total foyda for branchId: " + branchId, e);
            throw new RuntimeException("Error calculating total foyda for branchId: " + branchId, e);
        }
    }


    public BigDecimal calculateTotalXarajatForBranch(UUID branchId) {
        String sql = "SELECT SUM(total_sum) AS total_xarajat_for_branch " +
                "FROM outlay " +
                "WHERE branch_id = ?";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, branchId);
    }

    public BigDecimal calculateTotalXarajatForBranchWithDateRange(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT SUM(total_sum) AS total_xarajat_for_branch " +
                "FROM outlay " +
                "WHERE branch_id = ? " +
                "AND created_at BETWEEN ? AND ?";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, branchId, startDate, endDate);
    }

    public List<Map<String, Object>> getOutlaySumsByPaymentMethod(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT pm.type AS payment_method_type, SUM(o.total_sum) AS total_sum " +
                "FROM outlay o " +
                "JOIN payment_method pm ON o.payment_method_id = pm.id " +
                "WHERE o.branch_id = ? " +
                "AND o.dollar_outlay = false " + // Qo'shildi: dollar_outlay ustuni false bo'lganda
                (startDate != null && endDate != null ? "AND o.created_at BETWEEN ? AND ? " : "") +
                "GROUP BY pm.type";

        if (startDate != null && endDate != null) {
            return jdbcTemplate.queryForList(sql, branchId, startDate, endDate);
        } else {
            return jdbcTemplate.queryForList(sql, branchId);
        }
    }


    public List<Map<String, Object>> getTradeSumsByPaymentMethod(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT pm.type AS payment_method_type, SUM(t.paid_sum) AS paid_sum " +
                "FROM trade t " +
                "JOIN payment_method pm ON t.pay_method_id = pm.id " +
                "WHERE t.branch_id = ? " +
                (startDate != null && endDate != null ? "AND t.created_at BETWEEN ? AND ? " : "") +
                "GROUP BY pm.type";

        if (startDate != null && endDate != null) {
            return jdbcTemplate.queryForList(sql, branchId, startDate, endDate);
        } else {
            return jdbcTemplate.queryForList(sql, branchId);
        }
    }

    public BigDecimal calculateTotalPurchaseSumForBranch(UUID branchId) {
        String sql = "SELECT SUM(total_sum) AS total_purchase_sum_for_branch " +
                "FROM purchase " +
                "WHERE branch_id = ?";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, branchId);
    }

    public BigDecimal calculateTotalPurchaseSumForBranchWithDateRange(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT SUM(total_sum) AS total_purchase_sum_for_branch " +
                "FROM purchase " +
                "WHERE branch_id = ? " +
                "AND created_at BETWEEN ? AND ?";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, branchId, startDate, endDate);
    }

    public List<Map<String, Object>> getPurchaseSumsByPaymentMethod(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT pm.type AS payment_method_type, SUM(p.total_sum) AS total_sum " +
                "FROM purchase p " +
                "JOIN payment_method pm ON p.payment_method_id = pm.id " +
                "WHERE p.branch_id = ? " +
                (startDate != null && endDate != null ? "AND p.created_at BETWEEN ? AND ? " : "") +
                "GROUP BY pm.type";

        if (startDate != null && endDate != null) {
            return jdbcTemplate.queryForList(sql, branchId, startDate, endDate);
        } else {
            return jdbcTemplate.queryForList(sql, branchId);
        }
    }

    public BigDecimal calculateTotalRepaymentDebtSumForBranchWithDateRange(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT SUM(rd.debt_sum) AS total_repayment_debt_sum_for_branch " +
                "FROM repayment_debt rd " +
                "JOIN customer c ON rd.customer_id = c.id " +
                "WHERE c.branch_id = ? AND rd.created_at BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, branchId, startDate, endDate);
    }

    public List<Map<String, Object>> getRepaymentDebtSumsByPaymentMethod(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT pm.type AS payment_method_type, SUM(rd.debt_sum) AS total_sum " +
                "FROM repayment_debt rd " +
                "JOIN payment_method pm ON rd.payment_method_id = pm.id " +
                "JOIN customer c ON rd.customer_id = c.id " +
                "WHERE c.branch_id = ? AND rd.created_at BETWEEN ? AND ? " +
                "GROUP BY pm.type";
        return jdbcTemplate.queryForList(sql, branchId, startDate, endDate);
    }

    public List<Map<String, Object>> getDebtCanculsByPaymentMethod(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT pm.type AS payment_method_type, " +
                "SUM(CASE WHEN dc.trade_id IS NOT NULL THEN dc.debt_price ELSE 0 END) AS total_debt_sum, " +
                "SUM(CASE WHEN dc.trade_id IS NOT NULL THEN dc.dollar_price ELSE CASE WHEN t.dollar_trade THEN dc.dollar_price ELSE 0 END END) AS total_dollar_sum " +
                "FROM debt_canculs dc " +
                "JOIN trade t ON dc.trade_id = t.id " +
                "JOIN payment_method pm ON t.pay_method_id = pm.id " +
                "WHERE t.branch_id = ? " +
                (startDate != null && endDate != null ? "AND dc.created_at BETWEEN ? AND ? " : "") +
                "GROUP BY pm.type";

        if (startDate != null && endDate != null) {
            return jdbcTemplate.queryForList(sql, branchId, startDate, endDate);
        } else {
            return jdbcTemplate.queryForList(sql, branchId);
        }
    }

    public BigDecimal calculateTotalSumForBranchWithDateRange(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT SUM(t.total_sum) AS total_sum " +
                "FROM trade t " +
                "WHERE t.branch_id = ? " +
                "AND t.created_at BETWEEN ? AND ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{branchId, startDate, endDate}, BigDecimal.class);
    }

    public BigDecimal calculateTotalSumForBranch(UUID branchId) {
        String sql = "SELECT SUM(t.total_sum) AS total_sum " +
                "FROM trade t " +
                "WHERE t.branch_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{branchId}, BigDecimal.class);
    }


}
