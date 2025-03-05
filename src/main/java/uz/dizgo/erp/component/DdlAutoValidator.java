package uz.dizgo.erp.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DdlAutoValidator implements ApplicationRunner {

    @Value("${spring.jpa.hibernate.ddl-auto:}")
    private String ddlAuto;

    @Override
    public void run(ApplicationArguments args) {
        if (!"update".equalsIgnoreCase(ddlAuto) && !"none".equalsIgnoreCase(ddlAuto)) {
            throw new IllegalStateException("❌ `spring.jpa.hibernate.ddl-auto` faqat `update` yoki `none` bo‘lishi mumkin. Hozirgi qiymat: " + ddlAuto);
        }
    }
}
