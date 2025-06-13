package com.e_boutique.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void envoyerOtp(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Votre code OTP");
            helper.setText("Voici votre code de vérification : " + otp, true);

            mailSender.send(message);
            System.out.println("✅ Email envoyé à " + toEmail);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Échec de l'envoi d'email : " + e.getMessage());
        }
    }
}
