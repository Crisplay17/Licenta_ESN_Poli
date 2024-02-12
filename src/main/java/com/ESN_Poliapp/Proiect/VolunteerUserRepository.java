package com.ESN_Poliapp.Proiect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerUserRepository extends JpaRepository<VolunteerUser, Long> {
    // Puteți adăuga aici metode suplimentare de căutare sau filtrare dacă este necesar.
}

