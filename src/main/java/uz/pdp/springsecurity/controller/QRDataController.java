package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;
import uz.pdp.springsecurity.payload.QRDataDto;
import uz.pdp.springsecurity.service.QRDataService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/qrdata")
@RequiredArgsConstructor
public class QRDataController {

    private final QRDataService qrDataService;
    private final ResponseEntityHelper responseEntityHelper;

    @CheckPermission("ADD_QRDATA")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody QRDataDto qrDataDto) {
        return responseEntityHelper.buildResponse(qrDataService.add(qrDataDto));
    }

    @CheckPermission("EDIT_QRDATA")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Long id, @RequestBody QRDataDto qrDataDto) {
        return responseEntityHelper.buildResponse(qrDataService.edit(id, qrDataDto));
    }

    @CheckPermission("VIEW_QRDATA")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable Long id) {
        return responseEntityHelper.buildResponse(qrDataService.get(id));
    }

    @CheckPermission("VIEW_QRDATA")
    @GetMapping
    public HttpEntity<?> getAll() {
        return responseEntityHelper.buildResponse(qrDataService.getAll());
    }

    @CheckPermission("DELETE_QRDATA")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        return responseEntityHelper.buildResponse(qrDataService.delete(id));
    }
}