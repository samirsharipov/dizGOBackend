package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.UserDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "job", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "branches", ignore = true)
    @Mapping(target = "bonuses", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(source = "businessId", target = "business.id")
    User toEntity(UserDto userDto);


    @Mapping(target = "branchId", ignore = true)
    @Mapping(source = "role.name", target = "roleName")
    @Mapping(target = "photoId", ignore = true)
    @Mapping(target = "bonusesId", ignore = true)
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "job.id", target = "jobId")
    @Mapping(source = "business.id", target = "businessId")
    UserDto toDto(User user);


    List<UserDto> toDto(List<User> users);


    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "job", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "branches", ignore = true)
    @Mapping(target = "bonuses", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(source = "businessId", target = "business.id")
    void update(UserDto userDto, @MappingTarget User user);
}
