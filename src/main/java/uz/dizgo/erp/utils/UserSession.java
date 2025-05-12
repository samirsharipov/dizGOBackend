package uz.dizgo.erp.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.mapper.UserMapper;
import uz.dizgo.erp.payload.UserDTO;
import uz.dizgo.erp.repository.UserRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserSession {

    private final UserMapper userMapper;
    private final UserRepository userRepository;


    @Transactional
    public UserDTO getUser() {

        UserDTO userDto = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            userDto = userMapper.toDto((User) authentication.getPrincipal());
        } else {
            // faqat birinchi chaqiruvda findByRoleName ishlatiladi
            Optional<User> superadmin = userRepository.findByRoleName(Constants.SUPER_ADMIN);
            if (superadmin.isPresent()) {
                userDto = userMapper.toDto(superadmin.get());
            } else {
                throw new RuntimeException("Superadmin not found");
            }
        }


        return userDto;
    }
}
