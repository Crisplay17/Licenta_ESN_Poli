package com.ESN_Poliapp.Proiect;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.modeler.BaseAttributeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private VolunteerUserService volunteerUserService;
    @Autowired
    private ErasmusUserService erasmusUserService;



    @GetMapping("/create")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }


    @GetMapping("/events_page")
    public String showEventsPage(Model model, HttpSession session, @RequestParam(defaultValue = "0") String pageIndex) {
        int pageInt = parsePageParameter(pageIndex);
        return showEventsPageWithPageIndex(model, session, pageInt);
    }

    private int parsePageParameter(String pageStr) {
        try {
            return Integer.parseInt(pageStr);
        } catch (NumberFormatException e) {
            // În cazul în care conversia eșuează, returnăm 0
            return 0;
        }
    }

    public String showEventsPageWithPageIndex(Model model, HttpSession session, int pageIndex) {
        // Setăm numărul de evenimente pe pagină
        int pageSize = 10; // Numărul de evenimente pe pagină

        // Calculăm pagina curentă
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        // Obținem pagina de evenimente din serviciu
        Page<Event> eventsPage = eventService.getEventsPage(pageable);

        // Obținem ID-ul utilizatorului autentificat din sesiunea curentă
        Long loggedInUserId = (Long) session.getAttribute("loggedInUserId");

        // Iterăm prin lista de evenimente și setăm proprietatea joined pentru fiecare eveniment
        for (Event event : eventsPage.getContent()) {
            // Obținem lista de participanți pentru eveniment
            List<VolunteerUser> participants = event.getParticipants();

            // Verificăm dacă ID-ul utilizatorului este în lista de participanți
            boolean isJoined = false;
            for (VolunteerUser participant : participants) {
                if (participant != null && participant.getId() != null && participant.getId().equals(loggedInUserId)) {
                    isJoined = true;
                    break;
                }
            }

            // Setăm proprietatea joined a evenimentului în funcție de rezultatul verificării
            event.setJoined(isJoined ? Boolean.TRUE : Boolean.FALSE);
        }

        // Iterăm prin lista de evenimente și setăm proprietatea joined pentru fiecare eveniment
        for (Event event : eventsPage.getContent()) {
            // Obținem lista de participanți pentru eveniment
            List<ErasmusUser> participants = event.getErasmusParticipants(); // Modificare aici

            // Verificăm dacă ID-ul utilizatorului este în lista de participanți
            boolean isJoined = false;
            for (ErasmusUser participant : participants) { // Modificare aici
                if (participant != null && participant.getId() != null && participant.getId().equals(loggedInUserId)) {
                    isJoined = true;
                    break;
                }
            }

            // Setăm proprietatea joined a evenimentului în funcție de rezultatul verificării
            event.setJoined(isJoined ? Boolean.TRUE : Boolean.FALSE);
        }


        // Adăugăm lista de evenimente și informații despre paginare în model
        // Calculăm pagina următoare
        int nextPageIndex = pageIndex + 1;
        int PreviousPageIndex = pageIndex - 1;

        // Adăugăm pagina următoare la model
        model.addAttribute("nextPageIndex", nextPageIndex);
        model.addAttribute("PreviousPageIndex", PreviousPageIndex);
        model.addAttribute("events", eventsPage);
        model.addAttribute("currentPage", pageIndex);
        model.addAttribute("totalPages", eventsPage.getTotalPages());

        // Returnăm numele paginii HTML pentru afișarea evenimentelor
        return "events_page";
    }




    @PostMapping("/events_page")
    @ResponseBody
    public ResponseEntity<?> createEvent(@RequestParam("eventName") String eventName,
                                         @RequestParam("eventDate") String eventDateStr,
                                         @RequestParam("eventLocation") String eventLocation,
                                         @RequestParam("eventCause") String eventCause,
                                         @RequestParam("eventParticipants") int eventParticipants,
                                         @RequestParam("eventDescription") String eventDescription,
                                         @RequestParam("eventGoal") String eventGoal,
                                         @RequestParam("eventOrganizer") String eventOrganizer,
                                         @RequestParam("eventLink") String eventLink,
                                         @RequestParam("backgroundImage") MultipartFile backgroundImage) {
        try {
            // Convertim stringul evenimentului într-un obiect de tip Date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date eventDate = formatter.parse(eventDateStr);

            // Verificați dacă datele trimise sunt valide
            if (eventName == null || eventDate == null || eventLocation == null || eventCause == null ||
                    eventGoal == null || eventDescription == null || eventParticipants == 0 || backgroundImage.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Missing required fields or background image");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Dacă toate datele necesare sunt prezente, citiți conținutul imaginii și salvați-o ca byte array
            byte[] backgroundImageBytes = backgroundImage.getBytes();

            // Dacă totul este în regulă, creați evenimentul utilizând serviciul
            Event event = new Event(eventName, eventDate, eventLocation, eventCause, eventParticipants, eventDescription, eventGoal);
            event.setEventOrganizer(eventOrganizer);
            event.setEventLink(eventLink);
            event.setBackgroundImage(backgroundImageBytes); // Setăm imaginea de fundal a evenimentului
            eventService.createEvent(event);

            // Returnați un răspuns de succes
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Event created successfully");
            return ResponseEntity.ok(response);
        } catch (ParseException e) {
            // În cazul în care nu se poate converti data, returnați un mesaj de eroare
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid date format");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            // În caz de eroare, returnați un mesaj de eroare
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error creating event");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }



    @PostMapping("/join-event")
    public ResponseEntity<?> joinEvent(@RequestBody Map<String, Long> requestData, HttpSession session) {
        try {
            // Obținem ID-ul utilizatorului logat din sesiune
            Long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
            System.out.println("Logged in user ID: " + loggedInUserId);

            // Verificăm dacă datele primite sunt în formatul corect și nu sunt nule
            if (requestData.containsKey("eventId") && loggedInUserId != null) {
                Long eventId = requestData.get("eventId");

                // Găsim evenimentul și utilizatorul din baza de date
                Event event = eventService.findEventById(eventId);
                VolunteerUser user = volunteerUserService.getVolunteerUserById(loggedInUserId);
                ErasmusUser user1 = erasmusUserService.getErasmusUserById(loggedInUserId);
                // Verificăm dacă evenimentul și utilizatorul există
                if (event != null && user != null) {
                    // Adăugăm utilizatorul la lista de participanți a evenimentului
                    event.getParticipants().add(user);
                    eventService.saveEvent(event);
                    // Adăugăm evenimentul la lista de evenimente la care participă utilizatorul
                    user.getEvents().add(event);
                    volunteerUserService.updateVolunteerUser(user);

                    // Returnăm un răspuns de succes
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Joined event successfully");
                    return ResponseEntity.ok(response);

                }else if (event != null && user1 != null) {
                    // Adăugăm utilizatorul la lista de participanți a evenimentului
                    event.getErasmusParticipants().add(user1);

                    eventService.saveEvent(event);
                    // Adăugăm evenimentul la lista de evenimente la care participă utilizatorul

                    user1.getEvents().add(event);
                    erasmusUserService.updateErasmusUser(user1);

                    // Returnăm un răspuns de succes
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Joined event successfully");
                    return ResponseEntity.ok(response);
                }
                else {
                    // Returnăm un mesaj de eroare dacă evenimentul sau utilizatorul nu există
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Event or user not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            } else {
                // Returnăm un mesaj de eroare dacă datele primite sunt incomplete sau invalide
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid request data");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            // În caz de eroare, returnăm un mesaj de eroare
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error joining event");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @GetMapping("/event_details")
    public String showEventDetailsPage(@RequestParam Long eventId, Model model) {
        // Obține detaliile evenimentului cu ID-ul specificat din serviciu
        Event event = eventService.findEventById(eventId);
        // Adaugă detaliile evenimentului în model pentru a fi afișate în HTML
        model.addAttribute("event", event);
        // Returnează numele paginii HTML pentru afișarea detaliilor evenimentului
        return "details";
    }

    @GetMapping("/background-image")
    @ResponseBody
    public ResponseEntity<byte[]> getBackgroundImage(@RequestParam Long id) {
        // Obțineți imaginea de fundal din baza de date folosind ID-ul evenimentului
        byte[] backgroundImage = eventService.getBackgroundImageById(id);

        // Returnați imaginea de fundal și setați antetul corespunzător
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Sau MediaType.IMAGE_PNG, în funcție de formatul imaginii
        headers.setContentLength(backgroundImage.length);

        return new ResponseEntity<>(backgroundImage, headers, HttpStatus.OK);
    }


    @GetMapping("/home")
    public String showMainPage(Model model) {
        // Obținem lista de evenimente viitoare și o adăugăm în model
        List<Event> upcomingEvents = eventService.getUpcomingEvents();
        model.addAttribute("upcomingEvents", upcomingEvents);

        // Obținem lista de evenimente trecute și o adăugăm în model
        List<Event> pastEvents = eventService.getPastEvents();
        model.addAttribute("pastEvents", pastEvents);

        // Obținem evenimentele care au loc astăzi și le adăugăm în model
        List<Event> eventsHappeningToday = eventService.getEventsHappeningToday();
        model.addAttribute("eventsHappeningToday", eventsHappeningToday);

        // Returnăm numele paginii HTML pentru afișarea paginii principale
        return "Main";
    }


    // Alte endpoint-uri pentru CRUD, cum ar fi getEventById, updateEvent, deleteEvent etc.
}

