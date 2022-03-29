package com.mola.cargo.controller;

import com.mola.cargo.model.Commande;
import com.mola.cargo.service.*;
import com.mola.cargo.util.Constante;
import groovy.transform.AutoClone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CommandeController {

    @Autowired
    private RecepteurService recepteurService;
    @Autowired
    private EmetteurService emetteurService;
    @Autowired
    private PieceService pieceService;
    @Autowired
    private PaiementService paiementService;
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

    //Renvoie le formulaire de la nouvelle commande
    @GetMapping("/commande/formCommande")
    public String showNouvelleCommande(Long idE, Long idR, Model model){
        model.addAttribute("unEmetteur", emetteurService.showOneEmetteur(idE));
        model.addAttribute("unRecepteur", recepteurService.showOneRecepteur(idR));
        model.addAttribute("pieces", pieceService.showPiece());
        model.addAttribute("modePaiments", paiementService.showPaiement());
        return "commande/formNewCommande";
    }

    @PostMapping("/commande/nouveau")
    public String enregistrerCommande(Commande commande){
        List<Commande> listeCommande = new ArrayList<>();
        String pin = commande.getPREFIX_COMMANDE()+""+commandeService.genererNbre(commande.getNBRE_INITIAL(), commande.getNBRE_FINAL());
        listeCommande = commandeService.showCommande();
        while(commandeService.testerAppartenance(listeCommande, pin)==true){
            pin = commande.getPREFIX_COMMANDE()+""+commandeService.genererNbre(commande.getNBRE_INITIAL(), commande.getNBRE_FINAL());
        }
        commande.setPin(pin);
        commande.setDateEnvoi(new Date());
       // commande.setStatut(Constante.INITIAL);
        commandeService.saveCommande(commande);
        return "redirect:/mode/envoi";
    }
}
