package com.ESN_Poliapp.Proiect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pornire")
public class AutentificareController {

    @GetMapping("")
    public String afisarePaginaAutentificare() {
        return "pornire"; // Acesta este numele paginii HTML
    }

    @GetMapping("/volunteers/login")
    public String redirectToVolunteerLogin() {
        return "redirect:/volunteers/login"; // Redirecționează către pagina de autentificare pentru voluntari
    }

    @GetMapping("/erasmus/login_erasmus")
    public String redirectToErasmusLogin() {
        return "redirect:/erasmus/login_erasmus"; // Redirecționează către pagina de autentificare pentru Erasmus
    }
}


