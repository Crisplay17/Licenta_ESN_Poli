package com.ESN_Poliapp.Proiect;

import com.ESN_Poliapp.Proiect.ErasmusUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErasmusUserRepository extends JpaRepository<ErasmusUser, Long> {
    ErasmusUser findByUsername(String username);

    ErasmusUser findByIdAndRoles(Long userId, UserRole role);

//    ErasmusUser findByIdAndRoles(Long userId, UserRole role);
    // Puteți adăuga aici metode suplimentare de căutare sau filtrare dacă este necesar.
}

