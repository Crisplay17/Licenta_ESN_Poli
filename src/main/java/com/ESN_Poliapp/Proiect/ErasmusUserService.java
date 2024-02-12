package com.ESN_Poliapp.Proiect;

import com.ESN_Poliapp.Proiect.ErasmusUser;
import com.ESN_Poliapp.Proiect.ErasmusUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ErasmusUserService {

    @Autowired
    private ErasmusUserRepository erasmusUserRepository;

    // Metodă pentru crearea unui utilizator Erasmus
    public ErasmusUser createErasmusUser(ErasmusUser erasmusUser) {
        // Implementați logica de validare și salvare în baza de date
        return erasmusUserRepository.save(erasmusUser);
    }

    // Metodă pentru obținerea unui utilizator Erasmus după ID
    public ErasmusUser getErasmusUserById(Long id) {
        // Implementați logica pentru obținerea utilizatorului după ID
        return erasmusUserRepository.findById(id).orElse(null);
    }

    // Metodă pentru actualizarea unui utilizator Erasmus
    public ErasmusUser updateErasmusUser(Long id, ErasmusUser updatedUser) {
        // Căutați utilizatorul existent în baza de date după ID
        ErasmusUser existingUser = erasmusUserRepository.findById(id).orElse(null);

        if (existingUser != null) {
            // Actualizați atributele utilizatorului cu cele primite în parametrul "updatedUser"
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setNationality(updatedUser.getNationality());

            // Salvare actualizare în baza de date
            return erasmusUserRepository.save(existingUser);
        }

        throw new UserNotFoundException("Utilizatorul Erasmus cu ID-ul " + id + " nu a fost găsit.");
    }

    // Metodă pentru ștergerea unui utilizator Erasmus după ID
    public void deleteErasmusUser(Long id) {
        // Verificați dacă utilizatorul există înainte de ștergere
        if (erasmusUserRepository.existsById(id)) {
            // Ștergeți utilizatorul din baza de date folosind ID-ul specificat
            erasmusUserRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("Utilizatorul Erasmus cu ID-ul " + id + " nu a fost găsit.");
        }
    }

    public void registerErasmusUser(ErasmusUser erasmusUser) {
        // Aici puteți implementa logica pentru validarea datelor și înregistrarea utilizatorului în baza de date
        // De exemplu, puteți verifica dacă datele sunt valide și apoi folosiți repository pentru a salva utilizatorul
        ErasmusUser savedUser = erasmusUserRepository.save(erasmusUser);
        // Dacă este necesar, puteți returna sau trata un mesaj de succes sau eroare în funcție de rezultatul înregistrării.
    }
}


