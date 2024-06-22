package com.ESN_Poliapp.Proiect;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/volunteers")
public class VolunteerUserController {

    private final VolunteerUserService volunteerUserService;

    @Autowired
    private EventService eventService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    public VolunteerUserController(VolunteerUserService volunteerUserService) {
        this.volunteerUserService = volunteerUserService;
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("volunteerUser", new VolunteerUser());
        return "register_volunteer"; // Numele paginii HTML pentru formularul de înregistrare
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerVolunteerUser(@ModelAttribute VolunteerUser volunteerUser, HttpServletRequest request) {
        try {

            // Atribuie rolurile utilizatorului în funcție de tipul de înregistrare

            volunteerUser.setRoles(Collections.singletonList(UserRole.VOLUNTEER_USER)); // Atribuie rolul de voluntar

            // Înregistrează voluntarul în baza de date și trimite e-mailul de verificare
            volunteerUserService.registerVolunteerUser(volunteerUser, getSiteURL(request));

            // În caz de succes, returnează un răspuns JSON cu un mesaj de succes
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Volunteer registered successfully");
            response.put("redirectMessage", "Redirecting to login...");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            // În cazul în care apar erori, returnează un răspuns JSON cu un mesaj de eroare
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Registration error");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("code") String code, Model model) {
        boolean verificationResult = volunteerUserService.verifyUser(code);
        model.addAttribute("verificationResult", verificationResult);
        return "verification_result"; // Numele paginii HTML pentru afișarea rezultatului verificării
    }


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("volunteerUser", new VolunteerUser());
        return "login_volunteer"; // Numele paginii HTML pentru formularul de autentificare
    }


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> loginVolunteerUser(@ModelAttribute VolunteerUser volunteerUser, HttpSession session) {
        try {
            // Încercați să obțineți utilizatorul voluntar din baza de date utilizând metoda de serviciu
            VolunteerUser authenticatedUser = volunteerUserService.getVolunteerUserByUsernameAndPassword(volunteerUser.getUsername(), volunteerUser.getPassword());

            // Verificați dacă autentificarea a fost cu succes
            if (authenticatedUser != null) {
                // Verificați dacă contul utilizatorului este activat
                if (authenticatedUser.isEnabled()) {
                    // Verificați dacă utilizatorul este admin și atribuiți acest lucru în sesiune
                    boolean isAdmin = volunteerUserService.isUserAdmin(authenticatedUser.getId());

                    // Actualizați sesiunea cu numele de utilizator, rolul și ID-ul
                    session.setAttribute("loggedInUserId", authenticatedUser.getId());
                    session.setAttribute("loggedInUsername", authenticatedUser.getUsername());
                    session.setAttribute("isAdmin", isAdmin);
                    session.setAttribute("isErasmus", false);
                    session.setAttribute("isVolunteer", true);

                    // Construiți răspunsul JSON de succes
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Volunteer logged in successfully");
                    return ResponseEntity.ok().body(response);
                } else {
                    // Dacă contul nu este activat, returnați un mesaj de eroare
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Login error. Account not activated.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } else {
                // În cazul în care autentificarea a eșuat sau contul nu există, returnați un mesaj de eroare
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Login error. Incorrect username or password.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            // În cazul în care apar erori în procesul de autentificare, returnați un mesaj de eroare
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error during login process.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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

    @GetMapping("/logout")
    public String logoutVolunteerUser(HttpSession session) {
        // Ștergeți toate atributele din sesiune la deconectare
        session.invalidate();
        return "redirect:/volunteers/home";
    }


    @GetMapping("/profile")
    public String showUserProfile(Model model, HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUsername");
        VolunteerUser user = volunteerUserService.getVolunteerUserByUsername(loggedInUsername);
        model.addAttribute("user", user);
        return "user_profile"; // Numele paginii HTML pentru profilul utilizatorului
    }


    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute VolunteerUser updatedUser, HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUsername");
        VolunteerUser existingUser = volunteerUserService.getVolunteerUserByUsername(loggedInUsername);
        // Actualizați datele utilizatorului existent cu noile date primite din formular
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setNationality(updatedUser.getNationality());
        // Apelați serviciul pentru a actualiza datele utilizatorului în baza de date
        volunteerUserService.updateVolunteerUser(existingUser);
        // Redirecționați către pagina principală sau altă pagină după actualizare
        return "redirect:/volunteers/home";
    }

    @GetMapping("/profilePicture")
    public ResponseEntity<byte[]> getProfilePicture(HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUsername");
        if (loggedInUsername != null) {
            VolunteerUser loggedInUser = volunteerUserService.getVolunteerUserByUsername(loggedInUsername);
            byte[] profilePictureBytes = loggedInUser.getProfilePicture();
            if (profilePictureBytes != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(profilePictureBytes, headers, HttpStatus.OK);
            } else {
                // Returnează imaginea implicită de profil
                try {
                    Resource resource = new ClassPathResource("/static/css/image/esn.png");
                    byte[] defaultProfilePictureBytes = Files.readAllBytes(resource.getFile().toPath());
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG);
                    return new ResponseEntity<>(defaultProfilePictureBytes, headers, HttpStatus.OK);
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.notFound().build();
                }
            }
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/profilePicture")
    public String updateProfilePicture(@RequestParam("profilePicture") MultipartFile profilePictureFile, HttpSession session) {
        try {
            byte[] profilePictureBytes = profilePictureFile.getBytes();
            String loggedInUsername = (String) session.getAttribute("loggedInUsername");
            VolunteerUser loggedInUser = volunteerUserService.getVolunteerUserByUsername(loggedInUsername);
            loggedInUser.setProfilePicture(profilePictureBytes); // Setează imaginea de profil în obiectul utilizatorului

            // Introduce o întârziere de 5 secunde pentru a simula procesarea imaginii
            Thread.sleep(2000); // 5000 milisecunde = 5 secunde

            volunteerUserService.updateVolunteerUser(loggedInUser); // Actualizează utilizatorul în baza de date
            return "redirect:/volunteers/profile"; // Redirecționează către pagina de profil
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            // Tratează orice excepții legate de manipularea fișierului
            return "redirect:/volunteers/profile"; // Redirecționează cu mesaj de eroare în caz de eșec
        }
    }



}