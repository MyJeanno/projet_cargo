package com.mola.cargo.controller;

import com.mola.cargo.model.Emetteur;
import com.mola.cargo.service.EmetteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmetteurController {

    @Autowired
    private EmetteurService emetteurService;

    @GetMapping
    public String showEmetteur(){
        emetteurService.showEmetteur();
        return "emetteur/emetteur";
    }
    @GetMapping("emetteur/formEmetteur")
    public String showFormEmetteur(){
        return "emetteur/formAddEmetteur";
    }

    @PostMapping("/Emetteur/nouveau")
    public String enregistrerEmetteur(Emetteur emetteur){
        emetteurService.saveEmetteur(emetteur);
        return "redirect:/emetteur/formRecepteur";
    }
}
