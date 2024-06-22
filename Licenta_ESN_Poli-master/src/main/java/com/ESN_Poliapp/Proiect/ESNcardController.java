package com.ESN_Poliapp.Proiect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class ESNcardController {

    @GetMapping("/esncard")
    public String showESNCardPage() {
        return "esncard"; // Returnează numele șablonului HTML pentru pagina ESNcard
    }
}
