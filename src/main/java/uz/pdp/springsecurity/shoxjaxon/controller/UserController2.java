package uz.pdp.springsecurity.shoxjaxon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.shoxjaxon.activity.User2;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users2")
public class UserController2 {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/getUsers2")
    public List<User2> getUsersByBusinessId() {
        UUID currentUserBusinessId = getCurrentUserBusinessId();

        String query = "SELECT " +
                "u.id, " +
                "u.username, " +
                "u.first_name, " +
                "u.last_name, " +
                "u.birthday, " +
                "u.phone_number, " +
                "u.photo_id, " +
                "u.arrival_time," +
                "u.leave_time," +
                "a.id AS attachment_id, " +
                "a.created_at AS attachment_created_at, " +
                "a.update_at AS attachment_update_at, " +
                "a.content_type, " +
                "a.file_original_name, " +
                "a.name AS attachment_name, " +
                "a.size, " +
                "r.name AS role_name, " +
                "string_agg(DISTINCT b.name, ', ') AS branch_names " +
                "FROM users u " +
                "LEFT JOIN attachment a ON u.photo_id = a.id " +
                "LEFT JOIN role r ON u.role_id = r.id " +
                "LEFT JOIN users_branches ub ON u.id = ub.users_id " +
                "LEFT JOIN branches b ON ub.branches_id = b.id " +
                "WHERE u.business_id = ? " +
                "AND u.active = true " +
                "GROUP BY " +
                "u.id, " +
                "u.username, " +
                "u.first_name, " +
                "u.last_name, " +
                "u.birthday, " +
                "u.phone_number, " +
                "u.photo_id, " +
                "u.arrival_time," +
                "u.leave_time," +
                "a.id, " +
                "a.created_at, " +
                "a.update_at, " +
                "a.content_type, " +
                "a.file_original_name, " +
                "a.name, " +
                "a.size, " +
                "r.name";

        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(User2.class), currentUserBusinessId);
    }


    private UUID getCurrentUserBusinessId() {
        // Agar Spring Security ishlatilsa
        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        System.out.println("Principal Type: " + principal.getClass());
        User userDetails = (User) authentication.getPrincipal();
        return userDetails.getBusiness().getId();
    }
}
