package com.ESN_Poliapp.Proiect;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import java.util.UUID;

@Service
public class VolunteerUserService {

    @Autowired
    private VolunteerUserRepository volunteerUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public VolunteerUserService(VolunteerUserRepository volunteerUserRepository, PasswordEncoder passwordEncoder, JavaMailSender javaMailSender) {
        this.volunteerUserRepository = volunteerUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }


    // Metoda pentru a găsi un utilizator după ID și rol
    public VolunteerUser findUserByIdAndRole(Long userId, UserRole role) {
        return volunteerUserRepository.findByIdAndRoles(userId, role);
    }

// Metoda pentru a verifica dacă un utilizator are rolul de admin
public boolean isUserAdmin(Long userId) {
    VolunteerUser user = findUserByIdAndRole(userId, UserRole.ADMIN);
    return user != null;
}



    // Metodă pentru obținerea tuturor utilizatorilor voluntari
    public List<VolunteerUser> getAllVolunteerUsers() {
        return volunteerUserRepository.findAll();
    }

    // Metodă pentru a prelua un utilizator voluntar după numele de utilizator și parolă
    public VolunteerUser getVolunteerUserByUsernameAndPassword(String username, String password) {
        VolunteerUser volunteerUser = volunteerUserRepository.findByUsername(username);
        if (volunteerUser != null) {
            // Verifică dacă parolele corespund
            if (passwordEncoder.matches(password, volunteerUser.getPassword())) {
                return volunteerUser;
            }
        }
        return null;
    }
    // Metodă pentru înregistrarea unui utilizator voluntar
    // Metodă pentru înregistrarea unui utilizator voluntar
    public VolunteerUser registerVolunteerUser(VolunteerUser volunteerUser) {
        // Criptează parola utilizatorului înainte de a o salva în baza de date
        volunteerUser.setPassword(passwordEncoder.encode(volunteerUser.getPassword()));

        // Înregistrează voluntarul în baza de date
        VolunteerUser registeredVolunteer = volunteerUserRepository.save(volunteerUser);


        // Implementați logica de validare și salvare în baza de date
        return registeredVolunteer;
    }

    // Metodă pentru obținerea unui utilizator voluntar după ID
    public VolunteerUser getVolunteerUserById(Long id) {
        // Implementați logica pentru obținerea utilizatorului după ID
        return volunteerUserRepository.findById(id).orElse(null);
    }

    public VolunteerUser updateVolunteerUser(Long id, VolunteerUser updatedUserData) {
        VolunteerUser existingUser = volunteerUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilizatorul cu ID-ul " + id + " nu a fost găsit."));

        // Actualizați atributele utilizatorului cu cele primite în parametrul "updatedUserData"
        existingUser.setUsername(updatedUserData.getUsername());
        existingUser.setEmail(updatedUserData.getEmail());
        existingUser.setRoles(updatedUserData.getRoles());
//        existingUser.setPassword(updatedUserData.getPassword());
//        existingUser.setFirstName(updatedUserData.getFirstName());
//        existingUser.setLastName(updatedUserData.getLastName());
//        existingUser.setNationality(updatedUserData.getNationality());

        // Salvare actualizare în baza de date
        return volunteerUserRepository.save(existingUser);
    }


    // Metodă pentru ștergerea unui utilizator voluntar după ID
    public void deleteVolunteerUser(Long id) {
        // Verificați dacă utilizatorul există înainte de ștergere
        if (volunteerUserRepository.existsById(id)) {
            // Ștergeți utilizatorul din baza de date folosind ID-ul specificat
            volunteerUserRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("Utilizatorul cu ID-ul " + id + " nu a fost găsit.");
        }
    }


    public VolunteerUser getVolunteerUserByUsername(String username) {
        return volunteerUserRepository.findByUsername(username);
    }


    public void updateVolunteerUser(VolunteerUser user) {
        volunteerUserRepository.save(user);
    }

    public void registerVolunteerUser(VolunteerUser volunteerUser, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
        // Criptează parola utilizatorului înainte de a o salva în baza de date
        String encodedPassword = passwordEncoder.encode(volunteerUser.getPassword());
        volunteerUser.setPassword(encodedPassword);

        // Generează un cod de verificare aleatoriu
        String randomCode = UUID.randomUUID().toString();
        volunteerUser.setVerificationToken(randomCode);
        volunteerUser.setEnabled(false);

        // Setează data și ora expirării link-ului de verificare (de exemplu, peste 24 de ore)
        LocalDateTime expiryDateTime = LocalDateTime.now().plusHours(24);
        volunteerUser.setVerificationTokenExpiry(expiryDateTime);

        // Salvează utilizatorul în baza de date
        volunteerUserRepository.save(volunteerUser);

        // Trimite e-mailul de verificare către utilizator
        sendVerificationEmail(volunteerUser, siteURL);
    }



    // Metoda pentru a trimite e-mailul de verificare
    private void sendVerificationEmail(VolunteerUser volunteerUser, String token)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = volunteerUser.getEmail();
        String fromAddress = "wificristi103@gmail.com";
        String senderName = "ESN Poli.app";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "ESN Poli.app";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", volunteerUser.getFirstName());
        String verifyURL = token + "/volunteers/verify?code=" + volunteerUser.getVerificationToken();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

        }

    public boolean verifyUser(String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }

        VolunteerUser user = volunteerUserRepository.findByVerificationToken(code);
        if (user != null) {
            LocalDateTime expiryDateTime = user.getVerificationTokenExpiry();
            if (expiryDateTime != null && LocalDateTime.now().isBefore(expiryDateTime)) {
                user.setEnabled(true);
                volunteerUserRepository.save(user);
                return true;
            }
        }
        return false;
    }


}


