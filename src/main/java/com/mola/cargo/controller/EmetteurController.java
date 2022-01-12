package com.mola.cargo.controller;

import com.mola.cargo.model.Emetteur;
import com.mola.cargo.service.EmetteurService;
import com.mola.cargo.service.PaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmetteurController {

    @Autowired
    private EmetteurService emetteurService;
    @Autowired
    private PaysService paysService;

    @GetMapping("/emetteurs")
    public String showEmetteur(){
        emetteurService.showEmetteur();
        return "emetteur/emetteur";
    }
    //Renvoie le formulaire de l'expéditeur
    @GetMapping("/emetteur/formEmetteur")
    public String showFormEmetteur(){
        return "emetteur/formAddEmetteur";
    }

    //Renvoie le formulaire du destinataire
    @GetMapping("/recepteur/formRecepteur")
    public String showFormRecepteur(Model model){
        model.addAttribute("pays", paysService.showPays());
        return "recepteur/formAddRecepteur";
    }

    //Enregistrer un expéditeur
    @PostMapping("/emetteur/nouveau")
    public String enregistrerEmetteur(Emetteur emetteur){
        emetteur.setSituation(emetteur.getSITUATION_EMETTEUR());
        emetteurService.saveEmetteur(emetteur);
        //Long id = emetteur.getId();
        return "redirect:/recepteur/formRecepteur";
    }
}
