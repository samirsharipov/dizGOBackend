package uz.dizgo.erp.shoxjaxon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.dizgo.erp.shoxjaxon.activity.ProductAbout2;
import uz.dizgo.erp.shoxjaxon.service.ProductAboutService2;

import java.util.List;
import java.util.UUID;

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
    public List<ProductAbout2> getAllDescriptionsWithKeywordAndBranchId(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UUID branchId
    ) {
        return productAboutService2.getAllDescriptionsWithKeywordAndBranchId(keyword, branchId);
    }





    @GetMapping("/getAllDescriptionsHomashyo")
    public List<ProductAbout2> getAllDescriptionsHomashyo(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UUID branchId
    ) {

        return productAboutService2.getAllDescriptionsHomashyo(keyword, branchId);
    }

    @GetMapping("/getAllDescriptionsBrak")
    public List<ProductAbout2> getAllDescriptionsBrak(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UUID branchId
    ) {

        return productAboutService2.getAllDescriptionsBrak(keyword, branchId);
    }





    @GetMapping("/getAllProductAboutData")
    public List<ProductAbout2> getAllProductAboutData() {
        return productAboutService2.getAllProductAboutData();
    }

}
