package com.mola.cargo.controller;

import com.mola.cargo.model.Recepteur;
import com.mola.cargo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    //Renvoie le formulaire de la nouvelle commande
    @GetMapping("commande/formCommande")
    public String showNouvelleCommande(Model model){
        model.addAttribute("emetteurs", emetteurService.showEmetteur());
        model.addAttribute("recepteurs", recepteurService.showRecepteur());
        model.addAttribute("pieces", pieceService.showPiece());
        model.addAttribute("modePaiments", paiementService.showPaiement());
        return "commande/formNewCommande";
    }

    //Enregistrement du recpteur
    @PostMapping("/recepteur/nouveau")
    public String enregistrerRecepteur(Recepteur recepteur){
        recepteur.setSituation(recepteur.getSITUATION_RECEPTEUR());
        recepteurService.saveRecepteur(recepteur);
        return "redirect:/commande/formCommande";
    }
}
