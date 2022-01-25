package com.mola.cargo.controller;

import com.mola.cargo.model.Emetteur;
import com.mola.cargo.service.EmetteurService;
import com.mola.cargo.service.PaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmetteurController {

    @Autowired
    private EmetteurService emetteurService;
    @Autowired
    private PaysService paysService;

    //Renvoie la liste des clients expéditeurs
    @GetMapping("/emetteurs")
    public String showEmetteur(Model model){
        model.addAttribute("emetteurs", emetteurService.showEmetteur());
        return "personne/emetteur";
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
        return "redirect:/emetteurs";
    }


   /* //Renvoie le formulaire du destinataire
    @GetMapping("/recepteur/formRecepteur")
    public String showFormRecepteur(Model model){
        model.addAttribute("pays", paysService.showPays());
        return "personne/formAddRecepteur";
    }*/

    //Enregistrer un expéditeur
    @PostMapping("/emetteur/nouveau")
    public String enregistrerEmetteur(Emetteur emetteur){
        emetteur.setSituation(emetteur.getSITUATION_EMETTEUR());
        emetteur.setNumeroPersonne(emetteurService.numeroClient(emetteur, emetteur.getNomPersonne()));
        emetteurService.saveEmetteur(emetteur);
        //Long id = emetteur.getId();
        return "redirect:/emetteurs";
    }

    /*Enregistrer un expéditeur
    @PostMapping("/emetteur/nouveau")
    public String enregistrerEmetteur(Emetteur emetteur){
        emetteur.setSituation(emetteur.getSITUATION_EMETTEUR());
        emetteur.setNumeroPersonne(emetteurService.numeroClient(emetteur, emetteur.getNomPersonne()));
        emetteurService.saveEmetteur(emetteur);
        //Long id = emetteur.getId();
        return "redirect:/recepteur/formRecepteur";
    }*/
}
