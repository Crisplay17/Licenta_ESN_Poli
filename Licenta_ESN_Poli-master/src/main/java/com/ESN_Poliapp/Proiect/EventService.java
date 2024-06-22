package com.ESN_Poliapp.Proiect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Metoda pentru a obține toate evenimentele din baza de date
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Metoda pentru a crea un eveniment și a-l salva în baza de date
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }


    public Event findEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }


    public Page<Event> getEventsPage(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public byte[] getBackgroundImageById(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            // Assume that backgroundImage is a byte[] field in your Event entity
            return event.getBackgroundImage();
        } else {
            // Tratarea cazului în care nu există evenimentul cu ID-ul specificat
            // Poateți arunca o excepție sau returna null, în funcție de cazul dvs. de utilizare
            return null;
        }
    }

    public List<Event> getUpcomingEvents() {
        // Obținem data curentă
        Date currentDate = new Date();
        // Obținem lista de evenimente viitoare folosind metoda din repository
        return eventRepository.findByEventDateAfterOrderByEventDate(currentDate);
    }

    public List<Event> getPastEvents() {
        // Obținem data curentă
        LocalDate currentDate = LocalDate.now();
        // Convertim LocalDate în Date
        Date currentUtilDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Obținem lista de evenimente trecute folosind metoda din repository
        // Excluzând evenimentele care au loc astăzi
        return eventRepository.findByEventDateBeforeAndEventDateNotOrderByEventDateDesc(currentUtilDate, currentUtilDate);
    }


    public List<Event> getEventsHappeningToday() {
        // Obținem data curentă
        LocalDate currentDate = LocalDate.now();
        // Convertim LocalDate în Date
        Date currentUtilDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        // Obținem lista de evenimente care au loc astăzi folosind metoda din repository
        return eventRepository.findByEventDate(currentUtilDate);
    }

    // Metoda pentru a șterge un eveniment din baza de date
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    // Metoda pentru a actualiza un eveniment existent în baza de date
    public Event updateEvent(Long eventId, Event updatedEvent) {
        Optional<Event> existingEventOptional = eventRepository.findById(eventId);

        if (existingEventOptional.isPresent()) {
            Event existingEvent = existingEventOptional.get();
            // Actualizăm detaliile evenimentului existent cu cele furnizate în evenimentul actualizat
            existingEvent.setEventName(updatedEvent.getEventName());
            existingEvent.setEventDate(updatedEvent.getEventDate());
            existingEvent.setEventLocation(updatedEvent.getEventLocation());
            // Continuați actualizarea cu alte câmpuri, dacă este necesar

            // Salvăm și returnăm evenimentul actualizat
            return eventRepository.save(existingEvent);
        } else {
            // Tratarea cazului în care evenimentul nu a fost găsit
            // Aici puteți arunca o excepție sau returna null, în funcție de cazul dvs. de utilizare
            return null;
        }
    }

    public void deleteEventParticipants(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            List<VolunteerUser> participants = event.getParticipants();

            for (VolunteerUser participant : participants) {
                participant.getEvents().remove(event);
                // Alternativ, puteți folosi metoda removeEvent din clasa User, dacă există
                // participant.removeEvent(event);
            }

            // Ștergeți referințele către participanți din eveniment
            event.getParticipants().clear();

            // Salvați evenimentul actualizat în baza de date
            eventRepository.save(event);
        }
    }


    // Alte metode pentru a obține, actualiza și șterge evenimente, în funcție de nevoi
}

