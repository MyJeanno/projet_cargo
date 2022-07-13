package com.mola.cargo.controller;

import com.mola.cargo.model.Emetteur;
import com.mola.cargo.model.InfoClient;
import com.mola.cargo.service.EmetteurService;
import com.mola.cargo.service.InfoClientService;
import com.mola.cargo.service.PaysService;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/personne")
public class EmetteurController {

    @Autowired
    private EmetteurService emetteurService;
    @Autowired
    private PaysService paysService;
    @Autowired
    private InfoClientService infoClientService;

    //Renvoie la liste des clients expéditeurs
    @GetMapping("/emetteurs")
    public String showEmetteur(Model model){
        model.addAttribute("emetteurs", emetteurService.showEmetteur());
        return "personne/emetteur";
    }

    //Renvoie la liste des clients expéditeurs pour un envoi de colis
    @GetMapping("/emetteur/envoi")
    public String showEmetteurEnvoi(Model model){
        model.addAttribute("emetteurs", emetteurService.showEmetteur());
        return "personne/emetteurEnvoi";
    }

    @GetMapping("/emetteur/info")
    public String showEmetteurInfo(Model model){
        model.addAttribute("emetteurs", emetteurService.showEmetteur());
        //model.addAttribute("info", emetteurService.showEmetteur());
        return "personne/emetteurInfo";
    }

    @GetMapping("/emetteur/info/form/{id}")
    public String formInfo(@PathVariable("id") Long id, Model model){
        model.addAttribute("unEmetteur", emetteurService.showOneEmetteur(id));
        return "personne/formEmetteurInfo";
    }

    @GetMapping("emetteur/info/debloquer/{id}")
    public String debloquerEmetteur(@PathVariable("id") Long id){
        emetteurService.updateInfoEmetteur(Constante.CLIENT_AUTORISE, id);
        return "redirect:/personne/emetteur/info";
    }

    //Renvoie le formulaire de l'expéditeur
    @GetMapping("/emetteur/formEmetteur")
    public String showFormEmetteur(){
        return "personne/formAddEmetteur";
    }

    //renvoie le formulaire de mise à jour
    @GetMapping("emetteur/formUpdate/{id}")
    public String showFormUpdateEmetteur(@PathVariable("id") Long id, Model model){
        model.addAttribute("unEmetteur", emetteurService.showOneEmetteur(id));
        return "personne/formUpdateEmetteur";
    }

    //Mettre à jour un émetteur
    @PostMapping("/emetteur/update")
    public String updateEmetteur(@ModelAttribute("emetteur") Emetteur emetteur){
        emetteurService.saveEmetteur(emetteur);
        return "redirect:/personne/emetteurs";
    }

    //Enregistrer un expéditeur
    @PostMapping("/emetteur/nouveau")
    public String enregistrerEmetteur(Emetteur emetteur){
        emetteur.setNumeroPersonne(emetteurService.numeroClient(emetteur, emetteur.getNomPersonne()));
        emetteur.setUserid(Constante.showUserConnecte().getId());
        emetteur.setEtatPersonne(Constante.CLIENT_AUTORISE);
        emetteurService.saveEmetteur(emetteur);
        return "redirect:/personne/recepteur/formRecepteur";
    }
}
