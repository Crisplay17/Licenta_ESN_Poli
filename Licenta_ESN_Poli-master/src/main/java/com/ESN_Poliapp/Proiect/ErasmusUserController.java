package com.ESN_Poliapp.Proiect;


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
import java.util.*;

@Controller
@RequestMapping("/erasmus")
public class ErasmusUserController {

    private final ErasmusUserService erasmusUserService;

    @Autowired
    private EventService eventService; // Dacă este necesar

    @Autowired
    private PasswordEncoder passwordEncoder; // Dacă este necesar

    @Autowired
    public ErasmusUserController(ErasmusUserService erasmusUserService) {
        this.erasmusUserService = erasmusUserService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("erasmusUser", new ErasmusUser());
        return "register_erasmus"; // Numele paginii HTML pentru formularul de înregistrare
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerErasmusUser(@ModelAttribute ErasmusUser erasmusUser) {
        try {

            // Creează o listă cu rolurile utilizatorului Erasmus și adaugă rolul specific
            List<UserRole> roles = new ArrayList<>();
            roles.add(UserRole.ERASMUS_USER);

            // Setează rolurile pe utilizatorul Erasmus
            erasmusUser.setRoles(roles);

            // Validați datele utilizatorului Erasmus și apelați serviciul pentru înregistrarea acestuia în baza de date
            erasmusUserService.registerErasmusUser(erasmusUser);

            // În caz de succes, returnați un răspuns JSON cu un mesaj de succes
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Erasmus registered successfully");
            response.put("redirectMessage", "Redirecting to login...");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // În cazul în care apar erori în procesul de înregistrare, returnați un răspuns JSON cu mesajul de eroare
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Registration error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/login_erasmus")
    public String showLoginForm(Model model) {
        model.addAttribute("erasmusUser", new ErasmusUser());
        return "login_erasmus"; // Numele paginii HTML pentru formularul de autentificare
    }

    @PostMapping("/login_erasmus")
    @ResponseBody
    public ResponseEntity<?> loginErasmusUser(@ModelAttribute ErasmusUser erasmusUser, HttpSession session) {
        try {
            // Încercați să obțineți utilizatorul Erasmus din baza de date utilizând metoda de serviciu
            ErasmusUser authenticatedUser = erasmusUserService.getErasmusUserByUsernameAndPassword(erasmusUser.getUsername(), erasmusUser.getPassword());

            // Verificați dacă autentificarea a fost cu succes
            if (authenticatedUser != null) {
                // Actualizați sesiunea cu numele de utilizator și ID-ul
                session.setAttribute("loggedInUserId", authenticatedUser.getId());
                session.setAttribute("loggedInUsername", authenticatedUser.getUsername());
                session.setAttribute("isErasmus", true);
                session.setAttribute("isVolunteer", false);

                // Construiți răspunsul JSON de succes
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Erasmus logged in successfully");
                return ResponseEntity.ok().body(response);
            } else {
                // În cazul în care autentificarea a eșuat, returnați un răspuns JSON cu un mesaj de eroare
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Login error. Incorrect username or password.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            // În cazul în care apar erori în procesul de autentificare, returnați un răspuns JSON cu mesajul de eroare
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error during login process.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/home")
    public String showHomePage(Model model) {

        // Obținem evenimentele care au loc astăzi și le adăugăm în model
        List<Event> eventsHappeningToday = eventService.getEventsHappeningToday();
        model.addAttribute("eventsHappeningToday", eventsHappeningToday);

        // Obținem lista de evenimente viitoare și o adăugăm în model
        List<Event> upcomingEvents = eventService.getUpcomingEvents();
        model.addAttribute("upcomingEvents", upcomingEvents);

        // Obținem lista de evenimente trecute și o adăugăm în model
        List<Event> pastEvents = eventService.getPastEvents();
        model.addAttribute("pastEvents", pastEvents);

        // Returnăm numele paginii HTML pentru afișarea paginii principale
        return "Main";
    }

    @GetMapping("/logout")
    public String logoutErasmusUser(HttpSession session) {
        // Ștergeți toate atributele din sesiune la deconectare
        session.invalidate();
        return "redirect:/erasmus/home";
    }

    @GetMapping("/profile")
    public String showUserProfile(Model model, HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUsername");
        ErasmusUser user = erasmusUserService.getErasmusUserByUsername(loggedInUsername);
        model.addAttribute("user", user);
        return "user_profile"; // Numele paginii HTML pentru profilul utilizatorului
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute ErasmusUser updatedUser, HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUsername");
        ErasmusUser existingUser = erasmusUserService.getErasmusUserByUsername(loggedInUsername);
        // Actualizați datele utilizatorului existent cu noile date primite din formular
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setNationality(updatedUser.getNationality());
        // Apelați serviciul pentru a actualiza datele utilizatorului în baza de date
        erasmusUserService.updateErasmusUser(existingUser);
        // Redirecționați către pagina principală sau altă pagină după actualizare
        return "redirect:/erasmus/home";
    }

    @GetMapping("/profilePicture")
    public ResponseEntity<byte[]> getProfilePicture(HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUsername");
        if (loggedInUsername != null) {
            ErasmusUser loggedInUser1 = erasmusUserService.getErasmusUserByUsername(loggedInUsername);
            byte[] profilePictureBytes1 = loggedInUser1.getProfilePicture();
            if (profilePictureBytes1 != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(profilePictureBytes1, headers, HttpStatus.OK);
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
            ErasmusUser loggedInUser = erasmusUserService.getErasmusUserByUsername(loggedInUsername);
            loggedInUser.setProfilePicture(profilePictureBytes); // Setează imaginea de profil în obiectul utilizatorului

            // Introduce o întârziere de 2 secunde pentru a simula procesarea imaginii
            Thread.sleep(2000); // 2000 milisecunde = 2 secunde

            erasmusUserService.updateErasmusUser(loggedInUser); // Actualizează utilizatorul în baza de date
            return "redirect:/erasmus/profile"; // Redirecționează către pagina de profil
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            // Tratează orice excepții legate de manipularea fișierului
            return "redirect:/erasmus/profile"; // Redirecționează cu mesaj de eroare în caz de eșec
        }
    }
}
