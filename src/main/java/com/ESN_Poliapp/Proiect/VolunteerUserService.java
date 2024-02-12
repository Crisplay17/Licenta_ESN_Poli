package com.ESN_Poliapp.Proiect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerUserService {

    @Autowired
    private VolunteerUserRepository volunteerUserRepository;


    // Metodă pentru obținerea tuturor utilizatorilor voluntari
    public List<VolunteerUser> getAllVolunteerUsers() {
        return volunteerUserRepository.findAll();
    }
    // Metodă pentru înregistrarea unui utilizator voluntar
    public VolunteerUser registerVolunteerUser(VolunteerUser volunteerUser) {
        // Implementați logica de validare și salvare în baza de date
        return volunteerUserRepository.save(volunteerUser);
    }

    // Metodă pentru obținerea unui utilizator voluntar după ID
    public VolunteerUser getVolunteerUserById(Long id) {
        // Implementați logica pentru obținerea utilizatorului după ID
        return volunteerUserRepository.findById(id).orElse(null);
    }

    // Metodă pentru actualizarea datelor unui utilizator voluntar
    public VolunteerUser updateVolunteerUserData(Long id, VolunteerUser updatedUserData) {
        // Căutați utilizatorul existent în baza de date după ID
        VolunteerUser existingUser = volunteerUserRepository.findById(id).orElse(null);

        if (existingUser != null) {
            // Actualizați atributele utilizatorului cu cele primite în parametrul "updatedUserData"
            existingUser.setUsername(updatedUserData.getUsername());
            existingUser.setEmail(updatedUserData.getEmail());
            existingUser.setPassword(updatedUserData.getPassword());
            existingUser.setFirstName(updatedUserData.getFirstName());
            existingUser.setLastName(updatedUserData.getLastName());
            existingUser.setNationality(updatedUserData.getNationality());

            // Salvare actualizare în baza de date
            return volunteerUserRepository.save(existingUser);
        }

        throw new UserNotFoundException("Utilizatorul cu ID-ul " + id + " nu a fost găsit.");
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


}


