package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.EmergencyContact;
import uz.pdp.springsecurity.entity.Job;
import uz.pdp.springsecurity.entity.Role;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.EmergencyContactDTO;
import uz.pdp.springsecurity.payload.UserDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-15T18:13:49+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserDTO userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setFirstName( userDto.getFirstName() );
        user.setLastName( userDto.getLastName() );
        user.setSureName( userDto.getSureName() );
        user.setSex( userDto.isSex() );
        user.setBirthday( userDto.getBirthday() );
        user.setEmail( userDto.getEmail() );
        user.setPhoneNumber( userDto.getPhoneNumber() );
        user.setUsername( userDto.getUsername() );
        user.setPassword( userDto.getPassword() );
        user.setPassportNumber( userDto.getPassportNumber() );
        user.setJshshsr( userDto.getJshshsr() );
        user.setAddress( userDto.getAddress() );
        user.setDepartment( userDto.getDepartment() );
        user.setPosition( userDto.getPosition() );
        user.setArrivalTime( userDto.getArrivalTime() );
        user.setLeaveTime( userDto.getLeaveTime() );
        user.setSalaryAmount( userDto.getSalaryAmount() );
        user.setSalaryType( userDto.getSalaryType() );
        user.setPinCode( userDto.getPinCode() );
        user.setShiftType( userDto.getShiftType() );
        user.setProbation( userDto.getProbation() );
        user.setDescription( userDto.getDescription() );
        user.setDateOfEmployment( userDto.getDateOfEmployment() );
        user.setContractNumber( userDto.getContractNumber() );
        user.setEmergencyContacts( emergencyContactDTOListToEmergencyContactList( userDto.getEmergencyContacts() ) );
        user.setDateStartContract( userDto.getDateStartContract() );
        user.setDateEndContract( userDto.getDateEndContract() );

        return user;
    }

    @Override
    public UserDTO toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setRoleName( userRoleName( user ) );
        userDTO.setRoleParentId( userRoleParentRoleId( user ) );
        userDTO.setRoleId( userRoleId( user ) );
        userDTO.setJobId( userJobId( user ) );
        userDTO.setBusinessId( userBusinessId( user ) );
        userDTO.setId( user.getId() );
        userDTO.setFirstName( user.getFirstName() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setSureName( user.getSureName() );
        userDTO.setSex( user.isSex() );
        userDTO.setBirthday( user.getBirthday() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setPhoneNumber( user.getPhoneNumber() );
        userDTO.setUsername( user.getUsername() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setPassportNumber( user.getPassportNumber() );
        userDTO.setJshshsr( user.getJshshsr() );
        userDTO.setAddress( user.getAddress() );
        userDTO.setDepartment( user.getDepartment() );
        userDTO.setPosition( user.getPosition() );
        userDTO.setArrivalTime( user.getArrivalTime() );
        userDTO.setLeaveTime( user.getLeaveTime() );
        userDTO.setSalaryAmount( user.getSalaryAmount() );
        userDTO.setSalaryType( user.getSalaryType() );
        userDTO.setPinCode( user.getPinCode() );
        userDTO.setShiftType( user.getShiftType() );
        userDTO.setProbation( user.getProbation() );
        userDTO.setDescription( user.getDescription() );
        userDTO.setDateOfEmployment( user.getDateOfEmployment() );
        userDTO.setContractNumber( user.getContractNumber() );
        userDTO.setEmergencyContacts( emergencyContactListToEmergencyContactDTOList( user.getEmergencyContacts() ) );
        userDTO.setActive( user.isActive() );
        userDTO.setEnabled( user.isEnabled() );
        userDTO.setGrossPriceControlOneUser( user.isGrossPriceControlOneUser() );
        userDTO.setDateStartContract( user.getDateStartContract() );
        userDTO.setDateEndContract( user.getDateEndContract() );

        return userDTO;
    }

    @Override
    public List<UserDTO> toDto(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( users.size() );
        for ( User user : users ) {
            list.add( toDto( user ) );
        }

        return list;
    }

    @Override
    public void update(UserDTO userDto, User user) {
        if ( userDto == null ) {
            return;
        }

        if ( user.getBusiness() == null ) {
            user.setBusiness( new Business() );
        }
        userDTOToBusiness( userDto, user.getBusiness() );
        user.setFirstName( userDto.getFirstName() );
        user.setLastName( userDto.getLastName() );
        user.setSureName( userDto.getSureName() );
        user.setSex( userDto.isSex() );
        user.setBirthday( userDto.getBirthday() );
        user.setEmail( userDto.getEmail() );
        user.setPhoneNumber( userDto.getPhoneNumber() );
        user.setUsername( userDto.getUsername() );
        user.setPassword( userDto.getPassword() );
        user.setPassportNumber( userDto.getPassportNumber() );
        user.setJshshsr( userDto.getJshshsr() );
        user.setAddress( userDto.getAddress() );
        user.setDepartment( userDto.getDepartment() );
        user.setPosition( userDto.getPosition() );
        user.setArrivalTime( userDto.getArrivalTime() );
        user.setLeaveTime( userDto.getLeaveTime() );
        user.setSalaryAmount( userDto.getSalaryAmount() );
        user.setSalaryType( userDto.getSalaryType() );
        user.setPinCode( userDto.getPinCode() );
        user.setShiftType( userDto.getShiftType() );
        user.setProbation( userDto.getProbation() );
        user.setActive( userDto.isActive() );
        user.setDescription( userDto.getDescription() );
        user.setDateOfEmployment( userDto.getDateOfEmployment() );
        user.setContractNumber( userDto.getContractNumber() );
        if ( user.getEmergencyContacts() != null ) {
            List<EmergencyContact> list = emergencyContactDTOListToEmergencyContactList( userDto.getEmergencyContacts() );
            if ( list != null ) {
                user.getEmergencyContacts().clear();
                user.getEmergencyContacts().addAll( list );
            }
            else {
                user.setEmergencyContacts( null );
            }
        }
        else {
            List<EmergencyContact> list = emergencyContactDTOListToEmergencyContactList( userDto.getEmergencyContacts() );
            if ( list != null ) {
                user.setEmergencyContacts( list );
            }
        }
        user.setDateStartContract( userDto.getDateStartContract() );
        user.setDateEndContract( userDto.getDateEndContract() );
        user.setEnabled( userDto.isEnabled() );
    }

    protected EmergencyContact emergencyContactDTOToEmergencyContact(EmergencyContactDTO emergencyContactDTO) {
        if ( emergencyContactDTO == null ) {
            return null;
        }

        EmergencyContact emergencyContact = new EmergencyContact();

        emergencyContact.setName( emergencyContactDTO.getName() );
        emergencyContact.setPhoneNumber( emergencyContactDTO.getPhoneNumber() );

        return emergencyContact;
    }

    protected List<EmergencyContact> emergencyContactDTOListToEmergencyContactList(List<EmergencyContactDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<EmergencyContact> list1 = new ArrayList<EmergencyContact>( list.size() );
        for ( EmergencyContactDTO emergencyContactDTO : list ) {
            list1.add( emergencyContactDTOToEmergencyContact( emergencyContactDTO ) );
        }

        return list1;
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

    private UUID userRoleParentRoleId(User user) {
        if ( user == null ) {
            return null;
        }
        Role role = user.getRole();
        if ( role == null ) {
            return null;
        }
        Role parentRole = role.getParentRole();
        if ( parentRole == null ) {
            return null;
        }
        UUID id = parentRole.getId();
        if ( id == null ) {
            return null;
        }
        return id;
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

    protected EmergencyContactDTO emergencyContactToEmergencyContactDTO(EmergencyContact emergencyContact) {
        if ( emergencyContact == null ) {
            return null;
        }

        EmergencyContactDTO emergencyContactDTO = new EmergencyContactDTO();

        emergencyContactDTO.setName( emergencyContact.getName() );
        emergencyContactDTO.setPhoneNumber( emergencyContact.getPhoneNumber() );

        return emergencyContactDTO;
    }

    protected List<EmergencyContactDTO> emergencyContactListToEmergencyContactDTOList(List<EmergencyContact> list) {
        if ( list == null ) {
            return null;
        }

        List<EmergencyContactDTO> list1 = new ArrayList<EmergencyContactDTO>( list.size() );
        for ( EmergencyContact emergencyContact : list ) {
            list1.add( emergencyContactToEmergencyContactDTO( emergencyContact ) );
        }

        return list1;
    }

    protected void userDTOToBusiness(UserDTO userDTO, Business mappingTarget) {
        if ( userDTO == null ) {
            return;
        }

        mappingTarget.setId( userDTO.getBusinessId() );
    }
}
