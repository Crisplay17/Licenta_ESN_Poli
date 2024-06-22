package com.ESN_Poliapp.Proiect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SuccessController {

    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }
}

