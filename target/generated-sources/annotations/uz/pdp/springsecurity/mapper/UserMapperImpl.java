package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.Job;
import uz.pdp.springsecurity.entity.Role;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.BranchGetDto;
import uz.pdp.springsecurity.payload.UserDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-28T11:55:46+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setBusiness( userDtoToBusiness( userDto ) );
        user.setId( userDto.getId() );
        user.setFirstName( userDto.getFirstName() );
        user.setLastName( userDto.getLastName() );
        user.setEmail( userDto.getEmail() );
        user.setUsername( userDto.getUsername() );
        user.setPassword( userDto.getPassword() );
        user.setPhoneNumber( userDto.getPhoneNumber() );
        user.setSex( userDto.isSex() );
        user.setBirthday( userDto.getBirthday() );
        user.setActive( userDto.isActive() );
        user.setAddress( userDto.getAddress() );
        user.setDescription( userDto.getDescription() );
        user.setProbation( userDto.getProbation() );
        user.setWorkingTime( userDto.getWorkingTime() );
        user.setSalary( userDto.getSalary() );
        user.setArrivalTime( userDto.getArrivalTime() );
        user.setLeaveTime( userDto.getLeaveTime() );
        user.setPassportNumber( userDto.getPassportNumber() );
        user.setDateOfEmployment( userDto.getDateOfEmployment() );
        user.setEnabled( userDto.isEnabled() );

        return user;
    }

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setRoleName( userRoleName( user ) );
        userDto.setRoleId( userRoleId( user ) );
        userDto.setJobId( userJobId( user ) );
        userDto.setBusinessId( userBusinessId( user ) );
        userDto.setId( user.getId() );
        userDto.setFirstName( user.getFirstName() );
        userDto.setLastName( user.getLastName() );
        userDto.setEmail( user.getEmail() );
        userDto.setUsername( user.getUsername() );
        userDto.setPassword( user.getPassword() );
        userDto.setPhoneNumber( user.getPhoneNumber() );
        userDto.setSex( user.isSex() );
        userDto.setBirthday( user.getBirthday() );
        userDto.setBranches( branchSetToBranchGetDtoSet( user.getBranches() ) );
        userDto.setActive( user.isActive() );
        userDto.setAddress( user.getAddress() );
        userDto.setDescription( user.getDescription() );
        userDto.setProbation( user.getProbation() );
        userDto.setWorkingTime( user.getWorkingTime() );
        userDto.setSalary( user.getSalary() );
        userDto.setArrivalTime( user.getArrivalTime() );
        userDto.setLeaveTime( user.getLeaveTime() );
        userDto.setEnabled( user.isEnabled() );
        userDto.setPassportNumber( user.getPassportNumber() );
        userDto.setDateOfEmployment( user.getDateOfEmployment() );

        return userDto;
    }

    @Override
    public List<UserDto> toDto(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( users.size() );
        for ( User user : users ) {
            list.add( toDto( user ) );
        }

        return list;
    }

    @Override
    public void update(UserDto userDto, User user) {
        if ( userDto == null ) {
            return;
        }

        if ( user.getBusiness() == null ) {
            user.setBusiness( new Business() );
        }
        userDtoToBusiness1( userDto, user.getBusiness() );
        user.setFirstName( userDto.getFirstName() );
        user.setLastName( userDto.getLastName() );
        user.setEmail( userDto.getEmail() );
        user.setUsername( userDto.getUsername() );
        user.setPassword( userDto.getPassword() );
        user.setPhoneNumber( userDto.getPhoneNumber() );
        user.setSex( userDto.isSex() );
        user.setBirthday( userDto.getBirthday() );
        user.setActive( userDto.isActive() );
        user.setAddress( userDto.getAddress() );
        user.setDescription( userDto.getDescription() );
        user.setProbation( userDto.getProbation() );
        user.setWorkingTime( userDto.getWorkingTime() );
        user.setSalary( userDto.getSalary() );
        user.setArrivalTime( userDto.getArrivalTime() );
        user.setLeaveTime( userDto.getLeaveTime() );
        user.setPassportNumber( userDto.getPassportNumber() );
        user.setDateOfEmployment( userDto.getDateOfEmployment() );
        user.setEnabled( userDto.isEnabled() );
    }

    protected Business userDtoToBusiness(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        Business business = new Business();

        business.setId( userDto.getBusinessId() );

        return business;
    }

    private String userRoleName(User user) {
        if ( user == null ) {
            return null;
        }
        Role role = user.getRole();
        if ( role == null ) {
            return null;
        }
        String name = role.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private UUID userRoleId(User user) {
        if ( user == null ) {
            return null;
        }
        Role role = user.getRole();
        if ( role == null ) {
            return null;
        }
        UUID id = role.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID userJobId(User user) {
        if ( user == null ) {
            return null;
        }
        Job job = user.getJob();
        if ( job == null ) {
            return null;
        }
        UUID id = job.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID userBusinessId(User user) {
        if ( user == null ) {
            return null;
        }
        Business business = user.getBusiness();
        if ( business == null ) {
            return null;
        }
        UUID id = business.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected BranchGetDto branchToBranchGetDto(Branch branch) {
        if ( branch == null ) {
            return null;
        }

        BranchGetDto branchGetDto = new BranchGetDto();

        branchGetDto.setId( branch.getId() );
        branchGetDto.setName( branch.getName() );

        return branchGetDto;
    }

    protected Set<BranchGetDto> branchSetToBranchGetDtoSet(Set<Branch> set) {
        if ( set == null ) {
            return null;
        }

        Set<BranchGetDto> set1 = new LinkedHashSet<BranchGetDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Branch branch : set ) {
            set1.add( branchToBranchGetDto( branch ) );
        }

        return set1;
    }

    protected void userDtoToBusiness1(UserDto userDto, Business mappingTarget) {
        if ( userDto == null ) {
            return;
        }

        mappingTarget.setId( userDto.getBusinessId() );
    }
}
