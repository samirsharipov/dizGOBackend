package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.SubscriptionPostDto;
import uz.pdp.springsecurity.service.SubscriptionService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @CheckPermission("CREATE_SUBSCRIPTION")
    @PostMapping
    public HttpEntity<?> create(@RequestBody SubscriptionPostDto subscriptionPostDto) {
        ApiResponse apiResponse = subscriptionService.create(subscriptionPostDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_SUBSCRIPTION")
    @GetMapping("/getAll")
    public HttpEntity<?> getAllSubscription() {
        ApiResponse apiResponse = subscriptionService.getAllSubscription();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_SUBSCRIPTION")
    @PutMapping("/confirm/{subscriptionId}")
    public HttpEntity<?> confirmSubscription(@PathVariable UUID subscriptionId, @RequestBody String statusTariff) {
        ApiResponse apiResponse = subscriptionService.confirmSubscription(subscriptionId, statusTariff);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("ACTIVE_MY_SUBSCRIPTION")
    @PutMapping("/active/{subscriptionId}")
    public HttpEntity<?> activeSubscription(@PathVariable UUID subscriptionId) {
        ApiResponse apiResponse = subscriptionService.active(subscriptionId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_SUBSCRIPTION")
    @GetMapping("/getAllHistory")
    public HttpEntity<?> getAllHistory() {
        ApiResponse apiResponse = subscriptionService.getAllHistory();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_SUBSCRIPTION")
    @GetMapping("/getByBusinessId/{id}")
    public HttpEntity<?> getByBusinessId(@PathVariable UUID id) {
        ApiResponse apiResponse = subscriptionService.getByBusinessId(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
