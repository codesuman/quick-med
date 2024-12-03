package com.quickmed.auth.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OTPService {
    public String generateOTP(String mobileNumber) {
        log.info("OTPService > generateOTP");

        return "1234";
    }

    public boolean isValid(Long mobileNumber, String otp){
        log.info("OTPService > isValid");

        return true;
    }
}
