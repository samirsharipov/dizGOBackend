package uz.pdp.springsecurity.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.mapper.UserMapper;
import uz.pdp.springsecurity.payload.UserDto;

@Component
public class UserSession {

    private final UserMapper userMapper;

    public UserSession(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    public UserDto getUser(){
        UserDto userDto = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            userDto = userMapper.toDto((User) authentication.getPrincipal());
        }
        return userDto;
    }
}
