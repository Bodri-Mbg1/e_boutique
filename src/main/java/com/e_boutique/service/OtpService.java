package com.e_boutique.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, String> otpMap = new HashMap<>();
    private final Random random = new Random();

    @Autowired
    private EmailService emailService;

    public String generateOtp(String email) {
        String otp = String.format("%06d", random.nextInt(999999));
        otpMap.put(email, otp);

        emailService.envoyerOtp(email, otp); // ✉️ envoi par email

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpMap.get(email);
        return storedOtp != null && storedOtp.equals(otp);
    }

    public void clearOtp(String email) {
        otpMap.remove(email);
    }


}
