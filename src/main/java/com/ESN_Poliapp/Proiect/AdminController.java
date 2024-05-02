package com.ESN_Poliapp.Proiect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final VolunteerUserService volunteerUserService;
    private final EventService eventService;

    @Autowired
    public AdminController(VolunteerUserService volunteerUserService, EventService eventService) {
        this.volunteerUserService = volunteerUserService;
        this.eventService = eventService;
    }


    @GetMapping
    public String showAdminPage(Model model) {
        List<VolunteerUser> volunteerUsers = volunteerUserService.getAllVolunteerUsers();
        List<Event> events = eventService.getAllEvents();
        model.addAttribute("volunteerUsers", volunteerUsers);
        model.addAttribute("events", events);
        return "admin"; // Pagina HTML pentru administrare
    }

    @PostMapping("/add-event")
    @ResponseBody
    public ResponseEntity<?> addEvent(@RequestBody Event event) {
        try {
            eventService.createEvent(event);
            return ResponseEntity.ok("Event added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding event");
        }
    }

    @DeleteMapping("/delete-event/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok("Event deleted successfully");
    }

    @PutMapping("/update-event/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody Event updatedEvent) {
        Event event = eventService.updateEvent(eventId, updatedEvent);
        if (event != null) {
            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        volunteerUserService.deleteVolunteerUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<VolunteerUser> updateUser(@PathVariable Long userId, @RequestBody VolunteerUser updatedUser) {
        VolunteerUser user = volunteerUserService.updateVolunteerUser(userId, updatedUser);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add-user")
    @ResponseBody
    public ResponseEntity<?> addUser(@RequestBody VolunteerUser user) {
        try {
            volunteerUserService.registerVolunteerUser(user);
            return ResponseEntity.ok("User added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding user");
        }
    }

    @DeleteMapping("/delete-event-participants/{eventId}")
    public ResponseEntity<String> deleteEventParticipants(@PathVariable Long eventId) {
        eventService.deleteEventParticipants(eventId);
        return ResponseEntity.ok("Event participants deleted successfully");
    }



    // Alte metode pentru gestionarea utilizatorilor și evenimentelor (ex: adăugare, ștergere, modificare)
}

