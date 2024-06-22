package com.ESN_Poliapp.Proiect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Aici puteți adăuga metode suplimentare pentru interacțiunea cu baza de date

    List<Event> findByEventDateAfterOrderByEventDate(Date date);

    List<Event> findByEventDateBeforeOrderByEventDateDesc(Date date);

    List<Event> findByEventDateBeforeAndEventDateNotOrderByEventDateDesc(Date currentDate, Date futureDate);
    List<Event> findByEventDate(Date eventDate);


}

