package com.ky.userservice.listener;

import com.ky.userservice.service.security.LoginService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthFailureListener {
    private final LoginService loginService;

    public AuthFailureListener(LoginService loginService) {
        this.loginService = loginService;
    }

    @EventListener
    public void authFailure(AuthenticationFailureBadCredentialsEvent event){
        Object principal = event.getAuthentication().getPrincipal();
        if(principal instanceof String){
            String email = (String)event.getAuthentication().getPrincipal();
            loginService.addLoginCache(email);
        }
    }
}
