package com.mola.cargo.controller;

import com.mola.cargo.model.InfoClient;
import com.mola.cargo.service.EmetteurService;
import com.mola.cargo.service.InfoClientService;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class InfoClientController {

    @Autowired
    private InfoClientService infoClientService;
    @Autowired
    private EmetteurService emetteurService;

    @GetMapping("/emetteur/listeInfo")
    public String afficherInfoClient(Model model){
        model.addAttribute("infos", infoClientService.showAllInfoClients());
        return "personne/listeInfoEmetteur";
    }

    @PostMapping("/personne/infoEmetteur/nouveau")
    public String enregistrerInfoEmetteur(InfoClient infoClient){
        infoClient.setDateInfo(LocalDate.now());
        infoClientService.saveInfoClient(infoClient);
        emetteurService.updateInfoEmetteur(Constante.CLIENT_BLOQUE, infoClient.getEmetteurId());
       return "redirect:/personne/emetteur/info";
    }

}
