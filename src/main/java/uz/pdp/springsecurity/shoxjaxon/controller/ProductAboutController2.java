package uz.pdp.springsecurity.shoxjaxon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springsecurity.shoxjaxon.activity.ProductAbout2;
import uz.pdp.springsecurity.shoxjaxon.service.ProductAboutService2;

import java.util.List;

@RestController
@RequestMapping("/api/productAbout2")
public class ProductAboutController2 {

    private final ProductAboutService2 productAboutService2;


    @Autowired
    public ProductAboutController2(ProductAboutService2 productAboutService2) {
        this.productAboutService2 = productAboutService2;
    }

//    @GetMapping("/getAllData")
//    public List<ProductAbout2> getAllProductAboutData() {
//        return productAboutService2.getAllProductAboutData();
//    }

    @GetMapping("/getAllDescriptionsIshlab")
    public List<ProductAbout2> getAllDescriptionsWithKeyword() {
        String keyword = "ISHLAB CHIQARILDI";
        return productAboutService2.getAllDescriptionsWithKeyword(keyword);
    }

    @GetMapping("/getAllDescriptionsHomashyo")
    public List<ProductAbout2> getAllDescriptionsHomashyo() {
        String keyword = "XOMASHYO";
        return productAboutService2.getAllDescriptionsHomashyo(keyword);
    }

    @GetMapping("/getAllDescriptionsBrak")
    public List<ProductAbout2> getAllDescriptionsBrak() {
        String keyword = "BRAKDAN CHIQDI";
        return productAboutService2.getAllDescriptionsBrak(keyword);
    }

    @GetMapping("/getAllProductAboutData")
    public List<ProductAbout2> getAllProductAboutData() {
        return productAboutService2.getAllProductAboutData();
    }

}
