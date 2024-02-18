package uz.pdp.springsecurity.hr.service.business;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Role;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.hr.payload.Result;
import uz.pdp.springsecurity.hr.payload.RolePayload;
import uz.pdp.springsecurity.repository.RoleRepository;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {
    private final RoleRepository roleRepository;

    @Override
    public HttpEntity<Result> getBusinessRole(User user) {
        List<RolePayload> rolePayloads = new LinkedList<>();
        for (Role role : roleRepository.findAllByBusinessId(user.getBusiness().getId())) {
            rolePayloads.add(new RolePayload(role.getId(), role.getName()));
        }

        return ResponseEntity.ok(new Result(true, "Lavozimlar", rolePayloads));
    }
}
