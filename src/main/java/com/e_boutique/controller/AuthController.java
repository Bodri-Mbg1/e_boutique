package com.e_boutique.controller;

import com.e_boutique.dto.LoginRequest;
import com.e_boutique.dto.OtpVerificationRequest;
import com.e_boutique.dto.RegisterRequest;
import com.e_boutique.model.Utilisateur;
import com.e_boutique.security.JwtUtil;
import com.e_boutique.service.OtpService;
import com.e_boutique.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private OtpService otpService;
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;




    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (utilisateurService.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setActif(false);

        utilisateurService.sauvegarder(utilisateur);
        otpService.generateOtp(utilisateur.getEmail());

        return ResponseEntity.ok("OTP envoyé à " + utilisateur.getEmail());
    }


    @PostMapping("/verify")
    public ResponseEntity<?> verifierOtp(@RequestBody OtpVerificationRequest request) {
        String email = request.getEmail();
        String otp = request.getOtp();

        if (otpService.verifyOtp(email, otp)) {
            Utilisateur utilisateur = utilisateurService.findByEmail(email);
            if (utilisateur == null) {
                return ResponseEntity.badRequest().body("Utilisateur introuvable.");
            }

            utilisateur.setActif(true);
            utilisateurService.sauvegarder(utilisateur);  // méthode à créer si nécessaire
            otpService.clearOtp(email);

            return ResponseEntity.ok("✅ Compte activé avec succès !");
        } else {
            return ResponseEntity.badRequest().body("❌ Code OTP invalide.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Utilisateur utilisateur = utilisateurService.findByEmail(request.getEmail());

        if (utilisateur == null) {
            return ResponseEntity.badRequest().body("Email introuvable");
        }

        if (!utilisateur.isActif()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Compte non activé");
        }

        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect");
        }

        // ✅ GÉNÉRATION DU TOKEN
        String token = jwtUtil.generateToken(utilisateur.getEmail());

        // ✅ RÉPONSE AVEC LE TOKEN
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", utilisateur.getEmail());
        response.put("role", utilisateur.getRole());

        return ResponseEntity.ok(response);
    }



}
