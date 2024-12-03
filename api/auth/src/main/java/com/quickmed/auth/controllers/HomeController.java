package com.quickmed.auth.controllers;

import com.quickmed.auth.dto.LoginRequest;
import com.quickmed.auth.services.OTPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    OTPService otpService;

    @GetMapping("/home")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Home !!!");
    }

    @PostMapping("/api/auth/public/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        log.info("HomeController > login");

        String otp = this.otpService.generateOTP(loginRequest.getMobileNumber());

        return ResponseEntity.ok("Please enter the OTP : " + otp);
    }

    @PostMapping("/api/auth/public/login-validate")
    public ResponseEntity<String> loginValidation(@RequestBody LoginRequest loginRequest) {
        log.info("HomeController > loginValidation");

        try {
            Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getMobileNumber(), loginRequest.getOtp()));

            if(authentication.isAuthenticated()) {
                return ResponseEntity.ok("Valid OTP.");
            } else throw new BadCredentialsException("Invalid OTP.");
        } catch (BadCredentialsException exception) {
            log.error("HomeController > loginValidation > Catch Block");
            log.error(exception.getMessage());

            return ResponseEntity.badRequest().body("Invalid OTP");
        }
    }
}
