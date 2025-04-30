package uz.dizgo.erp.configuration;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class JpaConfig {

    @Bean
    public HibernatePropertiesCustomizer hibernateCustomizer() {
        return properties -> {
            String ddlAuto = properties.getOrDefault("hibernate.hbm2ddl.auto", "none").toString();
            if (!ddlAuto.equalsIgnoreCase("update") && !ddlAuto.equalsIgnoreCase("none")) {
                throw new IllegalStateException("❌ `spring.jpa.hibernate.ddl-auto` faqat `update` yoki `none` bo‘lishi kerak!");
            }
            properties.put("hibernate.hbm2ddl.auto", "update"); // ❗ Har doim `none` yoki `update`
        };
    }
}
