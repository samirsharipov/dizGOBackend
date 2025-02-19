package uz.pdp.springsecurity.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.repository.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class ProductEntityHelper {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final MeasurementRepository measurementRepository;
    private final ProductTranslateRepository productTranslateRepository;

    public Product cloneProduct(UUID productId, Branch branch) {
        Product originalProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Business business = branch.getBusiness();

        Category category = null;
        if (originalProduct.getCategory() != null) {
            category = findOrCreateEntity(
                    () -> categoryRepository.findByBusiness_IdAndName(business.getId(), originalProduct.getCategory().getName()),
                    () -> createCategory(originalProduct.getCategory(), business),
                    categoryRepository::save);
        }

        Brand brand = null;
        if (originalProduct.getBrand() != null) {
            brand = findOrCreateEntity(
                    () -> brandRepository.findByBusiness_IdAndName(business.getId(), originalProduct.getBrand().getName()),
                    () -> createBrand(originalProduct.getBrand(), business),
                    brandRepository::save);
        }

        Measurement measurement = null;
        if (originalProduct.getMeasurement() != null) {
            measurement = findOrCreateEntity(
                    () -> measurementRepository.findByBusinessIdAndName(business.getId(), originalProduct.getMeasurement().getName()),
                    () -> createMeasurement(originalProduct.getMeasurement(), business),
                    measurementRepository::save);
        }

        Product clonedProduct = new Product();
        cloneProductFields(originalProduct, clonedProduct, branch, business, category, brand, measurement);

        Optional.ofNullable(originalProduct.getTranslations()).ifPresent(translations ->
                translations.forEach(translation -> {
                    ProductTranslate clonedTranslation = new ProductTranslate();
                    cloneTranslationFields(clonedProduct, translation, clonedTranslation);
                    productTranslateRepository.save(clonedTranslation);
                })
        );

        return clonedProduct;
    }

    private <T> T findOrCreateEntity(Supplier<Optional<T>> finder, Supplier<T> creator, Function<T, T> saver) {
        return finder.get().orElseGet(() -> saver.apply(creator.get()));
    }

    private void cloneProductFields(Product originalProduct, Product clonedProduct, Branch branch, Business business,
                                    Category category, Brand brand, Measurement measurement) {
        clonedProduct.setName(originalProduct.getName());
        clonedProduct.setDescription(originalProduct.getDescription());
        clonedProduct.setLongDescription(originalProduct.getLongDescription());
        clonedProduct.setBarcode(originalProduct.getBarcode());
        clonedProduct.setPhoto(originalProduct.getPhoto());
        clonedProduct.setSalePrice(originalProduct.getSalePrice());
        clonedProduct.setBuyPrice(originalProduct.getBuyPrice());
        clonedProduct.setActive(true);
        clonedProduct.setClone(true);

        clonedProduct.setBranch(new ArrayList<>(Collections.singletonList(branch)));
        clonedProduct.setBusiness(business);
        Optional.ofNullable(category).ifPresent(clonedProduct::setCategory);
        Optional.ofNullable(brand).ifPresent(clonedProduct::setBrand);
        Optional.ofNullable(measurement).ifPresent(clonedProduct::setMeasurement);
        productRepository.save(clonedProduct);
    }

    private void cloneTranslationFields(Product clonedProduct, ProductTranslate originalTranslation, ProductTranslate clonedTranslation) {
        clonedTranslation.setProduct(clonedProduct);
        clonedTranslation.setLanguage(originalTranslation.getLanguage());
        clonedTranslation.setName(originalTranslation.getName());
        clonedTranslation.setDescription(originalTranslation.getDescription());
        clonedTranslation.setLongDescription(originalTranslation.getLongDescription());
    }

    private Category createCategory(Category originalCategory, Business business) {
        return categoryRepository.save(new Category(originalCategory.getName(), business));
    }

    private Brand createBrand(Brand originalBrand, Business business) {
        return brandRepository.save(new Brand(originalBrand.getName(), business));
    }

    private Measurement createMeasurement(Measurement originalMeasurement, Business business) {
        return measurementRepository.save(new Measurement(originalMeasurement.getName(), business));
    }
}