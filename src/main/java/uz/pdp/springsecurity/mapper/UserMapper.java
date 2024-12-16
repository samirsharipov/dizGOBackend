package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.UserDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // UserDTO dan User entity ga o'tkazish
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "grossPriceControlOneUser", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "contractFile", ignore = true)
    @Mapping(target = "chatId", ignore = true)
    @Mapping(target = "bonuses", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photo", ignore = true) // Attachment maydonni keyinchalik sozlaymiz
    @Mapping(target = "job", ignore = true) // Job maydonni keyinchalik sozlaymiz
    @Mapping(target = "branches", ignore = true) // Branch maydonni keyinchalik sozlaymiz
    @Mapping(target = "role", ignore = true) // Role maydonni keyinchalik sozlaymiz
    @Mapping(target = "business", ignore = true) // Business maydonni keyinchalik sozlaymiz
    User toEntity(UserDTO userDto);

    // User entity dan UserDTO ga o'tkazish
    @Mapping(target = "roleCategoryName", ignore = true)
    @Mapping(target = "roleParentName", ignore = true)
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "roleParentId", source = "role.parentRole.id")
    @Mapping(target = "branchIds", ignore = true)
    @Mapping(target = "contractFileId", ignore = true)
    @Mapping(target = "photoId", ignore = true)
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "job.id", target = "jobId")
    @Mapping(source = "business.id", target = "businessId")
    UserDTO toDto(User user);

    // User entity ro'yxatidan UserDTO ro'yxatiga o'tkazish
    List<UserDTO> toDto(List<User> users);

    // User entity ni yangilash
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "contractFile", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "grossPriceControlOneUser", ignore = true)
    @Mapping(target = "chatId", ignore = true)
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
    void update(UserDTO userDto, @MappingTarget User user);
}