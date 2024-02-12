package com.ESN_Poliapp.Proiect;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/volunteers")
public class VolunteerUserController {

    private final VolunteerUserService volunteerUserService;

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
    public ResponseEntity<?> registerVolunteerUser(@ModelAttribute VolunteerUser volunteerUser) {
        try {
            // Validați datele voluntarului și apelați serviciul pentru înregistrarea voluntarului în baza de date
            volunteerUserService.registerVolunteerUser(volunteerUser);

            // În caz de succes, returnați un răspuns JSON cu un mesaj de succes
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Volunteer registered successfully");
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

    @PostMapping("/login")
    public String loginVolunteerUser(@RequestParam String username, @RequestParam String password, Model model) {
        // Verifică autentificarea voluntarului aici (poți utiliza Spring Security sau o altă logică personalizată).

        boolean authenticationSuccessful = true;
        if (authenticationSuccessful) {
            return "redirect:/Main"; // Redirecționează către pagina principală după autentificare cu succes.
        } else {
            // Dacă autentificarea eșuează, adaugă un mesaj de eroare în model.
            model.addAttribute("errorMessage", "Autentificare eșuată. Verificați datele introduse.");
            return "login_volunteer"; // Afișează din nou pagina de login cu mesajul de eroare.
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {

        return "login_volunteer"; // Numele paginii HTML pentru formularul de autentificare
    }
    @GetMapping("/home")
    public String showHomePage() {
        return "Main"; // Numele paginii principale pe care ai creat-o.
    }

}



/*
package com.ESN_Poliapp.Proiect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/volunteers")
public class VolunteerUserController {

    private final VolunteerUserService volunteerUserService;

    @Autowired
    public VolunteerUserController(VolunteerUserService volunteerUserService) {
        this.volunteerUserService = volunteerUserService;
    }

    @GetMapping
    public List<VolunteerUser> getAllVolunteerUsers() {
        return volunteerUserService.getAllVolunteerUsers();
    }

    @GetMapping("/{id}")
    public VolunteerUser getVolunteerUserById(@PathVariable Long id) {
        return volunteerUserService.getVolunteerUserById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<String> createVolunteerUser(@RequestBody VolunteerUser volunteerUser) {
        try {
            // Validați datele voluntarului, verificați dacă există erori de validare.
            // De exemplu, puteți verifica dacă toate câmpurile sunt completate corect.

            // Apelați serviciul pentru înregistrarea voluntarului în baza de date
            VolunteerUser registeredUser = volunteerUserService.registerVolunteerUser(volunteerUser);

            // Returnați un mesaj de succes sau un cod de stare 200 OK dacă înregistrarea a fost reușită
            return ResponseEntity.ok("Volunteer registered successfully");
        } catch (Exception e) {
            // În cazul în care apar erori în procesul de înregistrare, puteți returna un mesaj de eroare
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering volunteer: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public VolunteerUser updateVolunteerUser(@PathVariable Long id, @RequestBody VolunteerUser volunteerUser) {
        return volunteerUserService.updateVolunteerUserData(id, volunteerUser);
    }

    @DeleteMapping("/{id}")
    public void deleteVolunteerUser(@PathVariable Long id) {
        volunteerUserService.deleteVolunteerUser(id);
    }
}




@Controller
@RequestMapping("/volunteer")
public class VolunteerUserController {

    @Autowired
    private VolunteerUserService volunteerUserService;

    // Endpoint pentru afișarea paginii de autentificare a voluntarilor
    @GetMapping("/login")
    public String showVolunteerLoginForm() {
        return "login_volunteer"; // Numele fișierului HTML pentru autentificarea voluntarilor
    }

    // Endpoint pentru afișarea paginii de înregistrare a voluntarilor
    @GetMapping("/register")
    public String showVolunteerRegistrationForm(Model model) {
        model.addAttribute("volunteerUser", new VolunteerUser());
        return "register_volunteer"; // Numele fișierului HTML pentru înregistrarea voluntarilor
    }

    // Endpoint pentru procesarea formularului de înregistrare a voluntarilor
    @PostMapping("/register")
    public String registerVolunteerUser(@ModelAttribute VolunteerUser volunteerUser) {
        // Implementați aici logica de înregistrare a voluntarilor și salvare în baza de date
        volunteerUserService.registerVolunteerUser(volunteerUser);
        return "redirect:/volunteer/login"; // Redirecționează către pagina de autentificare
    }
}

// Endpoint pentru obținerea unui voluntar după ID
    @GetMapping("/{id}")
    public String getVolunteerUser(@PathVariable Long id, Model model) {
        VolunteerUser volunteerUser = volunteerUserService.getVolunteerUserById(id);
        model.addAttribute("volunteerUser", volunteerUser);
        return "profile_volunteer"; // Numele fișierului HTML pentru profilul voluntarului
    }

    // Endpoint pentru afișarea formularului de actualizare a datelor voluntarului
    @GetMapping("/{id}/edit")
    public String showEditVolunteerForm(@PathVariable Long id, Model model) {
        VolunteerUser volunteerUser = volunteerUserService.getVolunteerUserById(id);
        model.addAttribute("volunteerUser", volunteerUser);
        return "edit_volunteer"; // Numele fișierului HTML pentru editarea datelor voluntarului
    }

    // Endpoint pentru procesarea formularului de actualizare a datelor voluntarului
    @PostMapping("/{id}/edit")
    public String updateVolunteerUserData(@PathVariable Long id, @ModelAttribute VolunteerUser updatedUserData) {
        volunteerUserService.updateVolunteerUserData(id, updatedUserData);
        return "redirect:/volunteer/" + id; // Corectat formatul pentru redirecționare
    }

    // Endpoint pentru ștergerea unui voluntar după ID
    @GetMapping("/{id}/delete")
    public String deleteVolunteerUser(@PathVariable Long id) {
        volunteerUserService.deleteVolunteerUser(id);
        return "redirect:/volunteer/login"; // Redirecționează către pagina de autentificare
    }
    */