package com.mola.cargo.controller;

import com.mola.cargo.model.Commande;
import com.mola.cargo.service.CartonService;
import com.mola.cargo.service.CommandeService;
import com.mola.cargo.service.PaysService;
import groovy.transform.AutoClone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommandeController {

    @Autowired
    private CommandeService commandeService;
    @Autowired
    private CartonService cartonService;
    @Autowired
    private PaysService paysService;

    @GetMapping("/mode/envoi")
    public String choisirModeEnvoi(){
        return "commande/choix";
    }

    @GetMapping("commande/new")
    public String afficherFormCommande(){
        return "commande/formCommande";
    }

    //Renvoie le formulaire de saisie des colis
    @GetMapping("/colis/formColis")
    public String showFormColis(Model model){
        model.addAttribute("cartons", cartonService.showCarton());
        model.addAttribute("pays", paysService.showPays());
        model.addAttribute("commande", commandeService.showCommande());
        return "colis/formAddColis";
    }

    @PostMapping("/commande/nouveau")
    public String enregistrerCommande(Commande commande){
        commandeService.saveCommande(commande);
        return "redirect:/colis/formColis";
    }
}
