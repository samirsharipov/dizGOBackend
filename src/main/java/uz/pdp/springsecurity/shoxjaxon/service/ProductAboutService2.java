package uz.pdp.springsecurity.shoxjaxon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.shoxjaxon.activity.ProductAbout2;
import uz.pdp.springsecurity.shoxjaxon.repository.ProductAboutRepository2;

import java.util.List;

@Service
public class ProductAboutService2 {

    private final ProductAboutRepository2 productAboutRepository2;

    @Autowired
    public ProductAboutService2(ProductAboutRepository2 productAboutRepository2) {
        this.productAboutRepository2 = productAboutRepository2;
    }

    public List<ProductAbout2> getAllDescriptionsWithKeyword(String keyword) {
        return productAboutRepository2.getAllDescriptionsWithKeyword(keyword);
    }

    public List<ProductAbout2> getAllProductAboutData() {
        return productAboutRepository2.getAllProductAboutData();
    }

    public List<ProductAbout2> getAllDescriptionsHomashyo(String keyword) {
        return productAboutRepository2.getAllDescriptionsHomashyo(keyword);
    }

    public List<ProductAbout2> getAllDescriptionsBrak(String keyword) {
        return productAboutRepository2.getAllDescriptionsBrak(keyword);
    }

}
