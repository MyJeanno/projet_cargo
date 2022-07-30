package com.mola.cargo.controller;

import com.mola.cargo.model.Emetteur;
import com.mola.cargo.model.Recepteur;
import com.mola.cargo.service.*;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/personne")
public class RecepteurController {

    @Autowired
    private RecepteurService recepteurService;
    @Autowired
    private EmetteurService emetteurService;
    @Autowired
    private EtatService etatService;
    @Autowired
    private PaysService paysService;
    @Autowired
    private CommandeService commandeService;

    @GetMapping("/recepteurs")
    public String afficherRecepteur(Model model){
        model.addAttribute("recepteurs", recepteurService.showRecepteur());
        return "personne/recepteur";
    }

    @GetMapping("/recepteur/info")
    public String showRecepteurInfo(Model model){
        model.addAttribute("recepteurs", recepteurService.showRecepteur());
        return "personne/recepteurInfo";
    }

    @GetMapping("/recepteur/info/form/{id}")
    public String formInfo(@PathVariable("id") Long id, Model model){
        model.addAttribute("unRecepteur", recepteurService.showOneRecepteur(id));
        return "personne/formRecepteurInfo";
    }

    @GetMapping("/recepteur/info/debloquer/{id}")
    public String debloquerEmetteur(@PathVariable("id") Long id){
        recepteurService.updateInfoRecepteur(Constante.CLIENT_AUTORISE, id);
        return "redirect:/personne/recepteur/info";
    }

    //Renvoie la liste des clients recepteurs pour l'envoi
    @GetMapping("/recepteur/liste/{id}")
    public String showRecepteurListe(@PathVariable("id") Long id, Model model){
        model.addAttribute("UnEmetteur", emetteurService.showOneEmetteur(id));
        model.addAttribute("recepteurs", recepteurService.showRecepteur());
        return "personne/recepteurEnvoi";
    }

    //Renvoie le formulaire du destinataire
    @GetMapping("/recepteur/formRecepteur")
    public String showFormRecepteur(Model model){
        model.addAttribute("etats", etatService.showStates());
        model.addAttribute("pays", paysService.showPays());
        return "personne/formAddRecepteur";
    }

    @GetMapping("/recepteur/formRecepteur2/{p}")
    public String showFormRecepteur2(@PathVariable("p") String p, Model model){
        model.addAttribute("etats", etatService.showStates());
        model.addAttribute("pays", paysService.showPays());
        model.addAttribute("unEmetteur", emetteurService.showOneEmetteur(Long.parseLong(p)));
        return "personne/formAddRecepteur2";
    }

    //Enregistrement du recpteur
    @PostMapping("/recepteur/nouveau")
    public String enregistrerRecepteur(Recepteur recepteur){
        List<Recepteur> listeRecepteur = new ArrayList<>();
        String numero = recepteur.getNomPersonne().substring(0,1)+""+recepteur.getPrenomPersonne().substring(0,1)+""+commandeService.genererNbre(Constante.BORNE_INF_CLIENT, Constante.BORNE_SUP_CLIENT);
        listeRecepteur = recepteurService.showRecepteur();
        while(recepteurService.testerAppartenance(listeRecepteur, numero)==true){
            numero = recepteur.getNumeroPersonne().substring(0,1)+""+recepteur.getPrenomPersonne().substring(0,1)+""+commandeService.genererNbre(Constante.BORNE_INF_CLIENT, Constante.BORNE_SUP_CLIENT);
        }
        recepteur.setNumeroPersonne(numero);
        if(recepteur.getEtatid()==null){
            recepteur.setEtatid(17L);
        }
        recepteur.setSolde(0);
        recepteur.setUserid(Constante.showUserConnecte().getId());
        recepteur.setEtatPersonne(Constante.CLIENT_AUTORISE);
        recepteurService.saveRecepteur(recepteur);
        //return "redirect:/commande/formCommande";
        return "redirect:/personne/recepteurs";
    }

    @PostMapping("/recepteur/nouveau2")
    public String enregistrerRecepteur2(Long p, Recepteur recepteur){
        List<Recepteur> listeRecepteur = new ArrayList<>();
        String numero = recepteur.getNomPersonne().substring(0,1)+""+recepteur.getPrenomPersonne().substring(0,1)+""+commandeService.genererNbre(Constante.BORNE_INF_CLIENT, Constante.BORNE_SUP_CLIENT);
        listeRecepteur = recepteurService.showRecepteur();
        while(recepteurService.testerAppartenance(listeRecepteur, numero)==true){
            numero = recepteur.getNumeroPersonne().substring(0,1)+""+recepteur.getPrenomPersonne().substring(0,1)+""+commandeService.genererNbre(Constante.BORNE_INF_CLIENT, Constante.BORNE_SUP_CLIENT);
        }
        recepteur.setNumeroPersonne(numero);
        if(recepteur.getEtatid()==null){
            recepteur.setEtatid(17L);
        }
        recepteur.setSolde(0);
        recepteur.setUserid(Constante.showUserConnecte().getId());
        recepteur.setEtatPersonne(Constante.CLIENT_AUTORISE);
        recepteurService.saveRecepteur(recepteur);

        return "redirect:/commande/formCommande/?idE="+p+"&idR="+recepteurService.showMonDernierRecepteur(Constante.showUserConnecte().getId()).getId();
        //return "redirect:/personne/recepteurs";
    }

    //renvoie le formulaire de mise à jour
    @GetMapping("recepteur/formUpdate/{id}")
    public String showFormUpdateRecepteur(@PathVariable("id") Long id, Model model){
        model.addAttribute("unRecepteur", recepteurService.showOneRecepteur(id));
        model.addAttribute("pays", paysService.showPays());
        model.addAttribute("etats",etatService.showStates());
        return "personne/formUpdateRecepteur";
    }

    //Mettre à jour un recepteur
    @PostMapping("/recepteur/update")
    public String updateRecepteur(@ModelAttribute("recepteur") Recepteur recepteur){
        recepteurService.saveRecepteur(recepteur);
        return "redirect:/personne/recepteurs";
    }



}
