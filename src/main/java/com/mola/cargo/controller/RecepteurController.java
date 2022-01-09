package com.mola.cargo.controller;

import com.mola.cargo.service.RecepteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RecepteurController {

    @Autowired
    private RecepteurService recepteurService;

    @GetMapping("emetteur/formRecepteur")
    public String showFormRecepteur(){
        return "recepteur/formAddRecepteur";
    }

    @PostMapping("recepteur/nouveau")
    public String enregistrerRecepteur(){
        return "";
    }
}
