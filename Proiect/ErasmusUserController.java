package com.ESN_Poliapp.Proiect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/erasmus")
public class ErasmusUserController {

    private final ErasmusUserService erasmusUserService;

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

    @GetMapping("/login")
    public String showLoginForm() {
        return "login_erasmus"; // Numele paginii HTML pentru formularul de autentificare
    }
}
