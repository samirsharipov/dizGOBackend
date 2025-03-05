package uz.dizgo.erp.annotations;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.exeptions.ForbiddenException;

@Aspect
@Component
public class CheckPermissionExecutor {
    @Before(value = "@annotation(checkPermission)")
    public void checkUserPermissionMyMethod(CheckPermission checkPermission) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // Get current user
        boolean exist = false;
        for (GrantedAuthority authority : principal.getAuthorities()) {
            if (authority.getAuthority().equals(checkPermission.value())) {
                exist = true;
                break;
            }
        }
        if (!exist) throw new ForbiddenException(checkPermission.value(), "Forbidden");
    }
}
