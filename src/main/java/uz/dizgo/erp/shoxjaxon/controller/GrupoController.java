package uz.dizgo.erp.shoxjaxon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import uz.dizgo.erp.shoxjaxon.repository.GrupoResponse;

@RestController
@RequestMapping("/api")
public class GrupoController {

    @GetMapping("/request")
    public ResponseEntity<GrupoResponse> getRequest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://chat.optimit.pro/api_request/";
        ResponseEntity<GrupoResponse> responseEntity = restTemplate.getForEntity(url, GrupoResponse.class);
        return ResponseEntity.ok(responseEntity.getBody());
    }
}
