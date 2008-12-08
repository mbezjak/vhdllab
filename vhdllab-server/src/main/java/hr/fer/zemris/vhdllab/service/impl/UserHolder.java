package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.entities.Caseless;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;

public abstract class UserHolder {

    public static Caseless getUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String username;
        if(principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return new Caseless(username);
    }

}
