package api.why.uz.api.why.uz.util;

import api.why.uz.api.why.uz.config.CustomUserDetails;
import api.why.uz.api.why.uz.enums.ProfileRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SpringSecurityUtil {

    private SpringSecurityUtil() {}

    public static CustomUserDetails getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }

    public static Integer getCurrentUserId() {
        CustomUserDetails user = getCurrentProfile();
        return user.getId();
    }

    public static boolean hasRole(ProfileRole role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role.name()));
    }

}
