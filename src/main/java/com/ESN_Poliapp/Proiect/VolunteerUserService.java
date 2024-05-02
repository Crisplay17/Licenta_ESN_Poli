package com.ESN_Poliapp.Proiect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VolunteerUserService {

    @Autowired
    private VolunteerUserRepository volunteerUserRepository;
    private final PasswordEncoder passwordEncoder;

    public VolunteerUserService(VolunteerUserRepository volunteerUserRepository, PasswordEncoder passwordEncoder) {
        this.volunteerUserRepository = volunteerUserRepository;
        this.passwordEncoder = passwordEncoder;
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
        // Implementați logica de validare și salvare în baza de date
        return volunteerUserRepository.save(volunteerUser);
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


}


