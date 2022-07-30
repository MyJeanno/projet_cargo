package com.mola.cargo.controller;

import com.mola.cargo.model.Emetteur;
import com.mola.cargo.model.InfoClient;
import com.mola.cargo.service.CommandeService;
import com.mola.cargo.service.EmetteurService;
import com.mola.cargo.service.InfoClientService;
import com.mola.cargo.service.PaysService;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/personne")
public class EmetteurController {

    @Autowired
    private EmetteurService emetteurService;
    @Autowired
    private PaysService paysService;
    @Autowired
    private InfoClientService infoClientService;
    @Autowired
    private CommandeService commandeService;

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

    @GetMapping("/emetteur/formEmetteur2")
    public String showFormEmetteur2(){
        return "personne/formAddEmetteur2";
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
        List<Emetteur> listeEmetteur = new ArrayList<>();
        String numero = emetteur.getNomPersonne().substring(0,1)+""+emetteur.getPrenomPersonne().substring(0,1)+""+commandeService.genererNbre(Constante.BORNE_INF_CLIENT, Constante.BORNE_SUP_CLIENT);
        listeEmetteur = emetteurService.showEmetteur();
        while(emetteurService.testerAppartenance(listeEmetteur, numero)==true){
            numero = emetteur.getNumeroPersonne().substring(0,1)+""+emetteur.getPrenomPersonne().substring(0,1)+""+commandeService.genererNbre(Constante.BORNE_INF_CLIENT, Constante.BORNE_SUP_CLIENT);
        }
        emetteur.setNumeroPersonne(numero);
        emetteur.setUserid(Constante.showUserConnecte().getId());
        emetteur.setEtatPersonne(Constante.CLIENT_AUTORISE);
        emetteurService.saveEmetteur(emetteur);
        //return "redirect:/personne/recepteur/formRecepteur";
        return "redirect:/personne/emetteurs";
    }

    @PostMapping("/emetteur/nouveau2")
    public String enregistrerEmetteur2(Emetteur emetteur){
        List<Emetteur> listeEmetteur = new ArrayList<>();
        String numero = emetteur.getNomPersonne().substring(0,1)+""+emetteur.getPrenomPersonne().substring(0,1)+""+commandeService.genererNbre(Constante.BORNE_INF_CLIENT, Constante.BORNE_SUP_CLIENT);
        listeEmetteur = emetteurService.showEmetteur();
        while(emetteurService.testerAppartenance(listeEmetteur, numero)==true){
            numero = emetteur.getNumeroPersonne().substring(0,1)+""+emetteur.getPrenomPersonne().substring(0,1)+""+commandeService.genererNbre(Constante.BORNE_INF_CLIENT, Constante.BORNE_SUP_CLIENT);
        }
        emetteur.setNumeroPersonne(numero);
        emetteur.setUserid(Constante.showUserConnecte().getId());
        emetteur.setEtatPersonne(Constante.CLIENT_AUTORISE);
        emetteurService.saveEmetteur(emetteur);
        //return "redirect:/personne/recepteur/formRecepteur";
        return "redirect:/personne/emetteur/envoi";
    }
}
