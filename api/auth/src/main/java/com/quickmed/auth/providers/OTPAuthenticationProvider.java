package com.quickmed.auth.providers;

import com.quickmed.auth.services.OTPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;

@Slf4j
public class OTPAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private OTPService otpService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("OTPAuthenticationProvider > authenticate");

        Long mobileNumber = Long.valueOf((String) authentication.getPrincipal());
        String otp = (String) authentication.getCredentials();

        if(otpService.isValid(mobileNumber, otp)) {
            return new UsernamePasswordAuthenticationToken(mobileNumber, otp, Collections.emptyList());
        }

        throw new BadCredentialsException("Invalid OTP");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
