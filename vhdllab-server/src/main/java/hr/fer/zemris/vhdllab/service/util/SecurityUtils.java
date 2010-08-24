package hr.fer.zemris.vhdllab.service.util;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;

public abstract class SecurityUtils {

    public static String getUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

}
