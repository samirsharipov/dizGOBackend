package uz.dizgo.erp.configuration.jpaConfig;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.dizgo.erp.entity.User;


import java.util.Optional;
import java.util.UUID;

public class AuditorAwareImpl implements AuditorAware<UUID> {

    @NotNull
    @Override
    public Optional<UUID> getCurrentAuditor() {
        // Foydalanuvchi ID sini SecurityContext’dan olish
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        User user = (User) authentication.getPrincipal();
        return Optional.of(user.getId()); // yoki xodim ID
    }
}
