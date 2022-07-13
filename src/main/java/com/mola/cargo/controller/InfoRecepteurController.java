package com.mola.cargo.controller;

import com.mola.cargo.model.InfoRecepteur;
import com.mola.cargo.service.InfoRecepteurService;
import com.mola.cargo.service.RecepteurService;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class InfoRecepteurController {

    @Autowired
    private InfoRecepteurService infoRecepteurService;
    @Autowired
    private RecepteurService recepteurService;

    @GetMapping("/recepteur/listeInfo")
    public String afficherInfoRecepteur(Model model){
        model.addAttribute("infos", infoRecepteurService.showAllInfoRecepteur());
        return "personne/listeInfoRecepteur";
    }

    @PostMapping("/personne/infoRecepteur/nouveau")
    public String enregistrerInfo(InfoRecepteur infoRecepteur){
        infoRecepteur.setDateInfo(LocalDate.now());
        infoRecepteurService.saveInfoRecepteur(infoRecepteur);
        recepteurService.updateInfoRecepteur(Constante.CLIENT_BLOQUE, infoRecepteur.getRecepteurId());
        infoRecepteurService.saveInfoRecepteur(infoRecepteur);
        return "redirect:/personne/recepteur/info";
    }
}
