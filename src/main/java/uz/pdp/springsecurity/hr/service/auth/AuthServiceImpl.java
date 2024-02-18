package uz.pdp.springsecurity.hr.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.hr.exception.HRException;
import uz.pdp.springsecurity.hr.payload.Result;
import uz.pdp.springsecurity.hr.payload.UserResponse;
import uz.pdp.springsecurity.payload.BranchResponse;
import uz.pdp.springsecurity.payload.LoginDto;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.UserRepository;
import uz.pdp.springsecurity.security.JwtProvider;

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
