package uz.dizgo.erp.hr.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.exception.HRException;
import uz.dizgo.erp.hr.payload.Result;
import uz.dizgo.erp.hr.payload.UserResponse;
import uz.dizgo.erp.payload.BranchResponse;
import uz.dizgo.erp.payload.LoginDto;
import uz.dizgo.erp.repository.BranchRepository;
import uz.dizgo.erp.repository.UserRepository;
import uz.dizgo.erp.security.JwtProvider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtProvider jwtService;
    private final BranchRepository branchRepository;

    @Override
    public HttpEntity<Result> loginHrUser(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            User byUsername = userRepository.findByUsername(loginDto.getUsername()).orElseThrow();
            Map<String, Object> map = new HashMap<>();
            map.put("hr_access_token", jwtService.generateToken(byUsername.getUsername(), byUsername.getRole()));
            return ResponseEntity.ok(new Result(true, "Kirish muvaffaqiyatli", map));
        } catch (Exception e) {
            throw new HRException("Login yoki parol xato!");
        }
    }

    @Override
    public HttpEntity<Result> getMe(User user) {
        List<BranchResponse> branchResponses = new LinkedList<>();
        for (Branch branch : branchRepository.findAllByBusiness_Id(user.getBusiness().getId())) {
            branchResponses.add(new BranchResponse(
                    branch.getId(),
                    branch.getName()
            ));
        }
        Map<String, Object> business = new HashMap<>();
        business.put("id", user.getBusiness().getId());
        business.put("name", user.getBusiness().getName());
        business.put("branches", branchResponses);
        return ResponseEntity.ok(new Result(true, "Foydalanuvchi topildi", new UserResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getRole().getPermissions(),
                business
        )));
    }
}
