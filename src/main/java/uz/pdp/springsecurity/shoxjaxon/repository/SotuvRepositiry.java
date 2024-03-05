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
import java.util.UUID;

@Repository
public class SotuvRepositiry {

    private static final Logger logger = LoggerFactory.getLogger(SotuvRepositiry.class);


    private final JdbcTemplate jdbcTemplate;

    public SotuvRepositiry(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal calculateTotalSumForBranchWithDateRange(UUID branchId) {
        String sql = "SELECT SUM(total_sum) AS total_sum_for_branch " +
                "FROM trade " +
                "WHERE branch_id = ? " +
                "AND created_at BETWEEN ? AND ?";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, branchId);
    }
    public BigDecimal calculateTotalSumForBranch(UUID branchId) {
        String sql = "SELECT SUM(total_sum) AS total_sum_for_branch " +
                "FROM trade " +
                "WHERE branch_id = ?";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, branchId);
    }

    public BigDecimal calculateTotalFoydaForBranch(UUID branchId) {
        try {
            String sql = "SELECT COALESCE(SUM(total_profit), 0) AS total_foyda_for_branch " +
                    "FROM trade " +
                    "WHERE branch_id = ?";

            return jdbcTemplate.queryForObject(sql, BigDecimal.class, branchId);
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



    public BigDecimal calculateTotalSumForBranchWithDateRangeFiltered(UUID branchId, Date startDate, Date endDate) {
        String sql = "SELECT SUM(total_sum) AS total_sum_for_branch " +
                "FROM trade " +
                "WHERE branch_id = ? " +
                "AND created_at BETWEEN ? AND ?";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, branchId, startDate, endDate);
    }


}
