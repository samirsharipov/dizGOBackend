package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.annotations.CurrentUser;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.ProfileDto;
import uz.dizgo.erp.payload.UserDTO;
import uz.dizgo.erp.service.UserService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResponseEntityHelper responseEntityHelper;

    @CheckPermission("ADD_USER")
    @PostMapping()
    public HttpEntity<?> add(@Valid @RequestBody UserDTO userDto) {
        ApiResponse apiResponse = userService.add(userDto, false);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

//    @CheckPermission("EDIT_USER")
    @PutMapping("/{id}")
    public HttpEntity<?> editUser(@PathVariable UUID id, @RequestBody UserDTO userDto) {
        ApiResponse apiResponse = userService.edit(id, userDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

//    @CheckPermission("VIEW_USER")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = userService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_USER")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteById(@PathVariable UUID id) {
        ApiResponse apiResponse = userService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }

//    @CheckPermission("EDIT_MY_PROFILE")
    @PutMapping()
    public ResponseEntity<?> editMyProfile(@CurrentUser User user, @Valid @RequestBody ProfileDto profileDto) {
        ApiResponse apiResponse = userService.editMyProfile(user, profileDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("VIEW_USER")
    @GetMapping("/get-by-role/{role_id}")
    public HttpEntity<?> getByRole(@PathVariable UUID role_id) {
        ApiResponse apiResponse = userService.getByRole(role_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_USER_ADMIN")
    @GetMapping("/get-by-business/{business_id}")
    public HttpEntity<?> getAllByBusinessId(@PathVariable UUID business_id,
                                            @RequestParam String nameOrPhoneNumber,
                                            @RequestParam int size,
                                            @RequestParam int page) {
        ApiResponse apiResponse = userService.getAllByBusinessId(business_id, nameOrPhoneNumber, size, page);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_USER")
    @GetMapping("/get-by-branchId/{branch_id}")
    public HttpEntity<?> getAllByBranchId(@PathVariable UUID branch_id) {
        ApiResponse apiResponse = userService.getAllByBranchId(branch_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_USER")
    @GetMapping("/get-user-by-name/{branch_id}")
    public HttpEntity<?> getAllByUserName(@PathVariable UUID branch_id,
                                          @RequestParam String name,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        ApiResponse apiResponse = userService.getAllByName(branch_id, name, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_USER")
    @GetMapping("/get-by-patron/{user_id}")
    public HttpEntity<?> getByPatron(@PathVariable UUID user_id) {
        ApiResponse apiResponse = userService.getByPatron(user_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_USER")
    @GetMapping("/search-by-username/{username}")
    public HttpEntity<?> search(@PathVariable String username) {
        ApiResponse apiResponse = userService.search(username);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_USER")
    @PutMapping("/edit-password/{id}")
    public HttpEntity<?> editPassword(@PathVariable UUID id,
                                      @RequestBody String password) {
        ApiResponse apiResponse = userService.editPassword(id, password);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    //  new api
    @GetMapping("/userMe")
    public User getMeUser(@CurrentUser User user) {
        return user;
    }

    @GetMapping("/forGrossPriceControl/{userId}")
    public HttpEntity<?> forGrossPriceControl(@PathVariable UUID userId) {
        return userService.forGrossPriceControlFuncService(userId);
    }

    @PutMapping("/forGrossPriceControlEditeState/{userId}")
    public HttpEntity<?> forGrossPriceControlEditeState(@PathVariable UUID userId, @RequestParam Boolean checked) {
        ApiResponse apiResponse = userService.forGrossPriceControlEditeOneState(userId, checked);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @PutMapping("/forgot-password/{phoneNumber}")
    public HttpEntity<?> forgotPassword(@PathVariable String phoneNumber, @RequestParam String password) {
        ApiResponse apiResponse = userService.forgotPassword(phoneNumber, password);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/ember/{phoneNumber}")
    public HttpEntity<?> getByPhoneNumber(@PathVariable String phoneNumber) {
        return responseEntityHelper.buildResponse(userService.getByPhoneNumber(phoneNumber));
    }

    @GetMapping("/get-by-business-or-branch/{businessId}")
    public ResponseEntity<?> getBusinessOrBranch(@PathVariable UUID businessId,
                                                 @RequestParam(required = false) UUID branchId) {
        return responseEntityHelper.buildResponse(userService.getBusinessOrBranch(businessId, branchId));
    }
}