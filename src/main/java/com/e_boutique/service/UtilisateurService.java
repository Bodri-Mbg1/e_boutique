package com.e_boutique.service;

import com.e_boutique.model.Utilisateur;
import com.e_boutique.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepo;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;




    public Utilisateur inscription(Utilisateur utilisateur) {
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateur.setRole("ROLE_USER");
        utilisateur.setActif(false);
        return utilisateurRepo.save(utilisateur);
    }

    public Utilisateur chercherParEmail(String email) {
        return utilisateurRepo.findByEmail(email).orElse(null);
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElse(null);
    }

    public void sauvegarder(Utilisateur utilisateur) {
        utilisateurRepository.save(utilisateur);
    }


}
