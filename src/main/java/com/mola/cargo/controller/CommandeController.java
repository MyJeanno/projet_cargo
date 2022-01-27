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

import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/commande/nouveau")
    public String enregistrerCommande(Commande commande){
        List<Commande> listeCommande = new ArrayList<>();
        String pin = commandeService.genererNbre(commande.getNBRE_INITIAL(), commande.getNBRE_FINAL());
        listeCommande = commandeService.showCommande();
        while(commandeService.testerAppartenance(listeCommande, pin)==true){
            pin = commandeService.genererNbre(commande.getNBRE_INITIAL(), commande.getNBRE_FINAL());
        }
        commande.setPin(pin);
        commandeService.saveCommande(commande);
        return "redirect:/mode/envoi";
    }
}
