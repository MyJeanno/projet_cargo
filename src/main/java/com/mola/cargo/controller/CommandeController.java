package com.mola.cargo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommandeController {

    @GetMapping("commande/new")
    public String afficherFormCommande(){
        return "commande/formCommande";
    }
}
