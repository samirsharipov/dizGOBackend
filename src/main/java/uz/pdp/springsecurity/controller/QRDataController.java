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

    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody QRDataDto qrDataDto) {
        return responseEntityHelper.buildResponse(qrDataService.add(qrDataDto));
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Long id, @RequestBody QRDataDto qrDataDto) {
        return responseEntityHelper.buildResponse(qrDataService.edit(id, qrDataDto));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable Long id) {
        return responseEntityHelper.buildResponse(qrDataService.get(id));
    }

    @GetMapping
    public HttpEntity<?> getAll() {
        return responseEntityHelper.buildResponse(qrDataService.getAll());
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        return responseEntityHelper.buildResponse(qrDataService.delete(id));
    }
}