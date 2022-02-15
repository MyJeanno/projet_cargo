package com.mola.cargo.controller;

import com.mola.cargo.model.Emetteur;
import com.mola.cargo.model.Recepteur;
import com.mola.cargo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RecepteurController {

    @Autowired
    private RecepteurService recepteurService;
    @Autowired
    private EmetteurService emetteurService;
    @Autowired
    private PieceService pieceService;
    @Autowired
    private PaiementService paiementService;
    @Autowired
    private EtatService etatService;
    @Autowired
    private PaysService paysService;

    @GetMapping("/recepteurs")
    public String afficherRecepteur(Model model){
        model.addAttribute("recepteurs", recepteurService.showRecepteur());
        return "personne/recepteur";
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

    //Enregistrement du recpteur
    @PostMapping("/recepteur/nouveau")
    public String enregistrerRecepteur(Recepteur recepteur){
        recepteur.setSituation(recepteur.getSITUATION_RECEPTEUR());
        recepteur.setNumeroPersonne(recepteurService.numeroClient(recepteur, recepteur.getNomPersonne()));
        if(recepteur.getEtatid()==null){
            recepteur.setEtatid(17L);
        }
        recepteurService.saveRecepteur(recepteur);
        return "redirect:/recepteurs";
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
        return "redirect:/recepteurs";
    }

    //Renvoie le formulaire de la nouvelle commande
    @GetMapping("/commande/formCommande")
    public String showNouvelleCommande(Long idE, Long idR, Model model){
        model.addAttribute("unEmetteur", emetteurService.showOneEmetteur(idE));
        model.addAttribute("unRecepteur", recepteurService.showOneRecepteur(idR));
        model.addAttribute("pieces", pieceService.showPiece());
        model.addAttribute("modePaiments", paiementService.showPaiement());
        return "commande/formNewCommande";
    }

    /*Renvoie le formulaire de la nouvelle commande
    @GetMapping("commande/formCommande")
    public String showNouvelleCommande(Model model){
        model.addAttribute("emetteurs", emetteurService.showEmetteur());
        model.addAttribute("recepteurs", recepteurService.showRecepteur());
        model.addAttribute("pieces", pieceService.showPiece());
        model.addAttribute("modePaiments", paiementService.showPaiement());
        return "commande/formNewCommande";
    }*/

}
