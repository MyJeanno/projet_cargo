package com.mola.cargo.controller;

import com.mola.cargo.model.*;
import com.mola.cargo.service.*;
import com.mola.cargo.util.Constante;
import groovy.transform.AutoClone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private ReductionService reductionService;
    @Autowired
    private ColisAerienService colisAerienService;
    @Autowired
    private ColisMaritimeService colisMaritimeService;
    @Autowired
    private ProduitAerienService produitAerienService;
    @Autowired
    private ProduitMaritimeService produitMaritimeService;

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
        model.addAttribute("uneReduction", reductionService.lareduction());
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
        commande.setEtatCommande(Constante.STATUT_COMMANDE_INACHEVE);
        if(commandeService.commandeSelonEtat(Constante.STATUT_COMMANDE_INACHEVE).size()==0){
            commandeService.saveCommande(commande);
            return "redirect:/mode/envoi";
        }else{
            return "redirect:/commande/info";
        }
    }
    @GetMapping("/enregistrements/inacheves")
    public String commandeInacheve(Model model){
        model.addAttribute("commande_inacheve", commandeService.commandeSelonEtat(Constante.STATUT_COMMANDE_INACHEVE));
        return "commande/inacheve";
    }

    @GetMapping("/commande/info")
    public String warningReprise(){
        return "commande/erreurEnregistrement";
    }

    @GetMapping("/reprise/impossible")
    public String infoReprise(){
        return "commande/erreurReprise";
    }
    /*@GetMapping("/commande2/reprise/{id}")
    public String reprendreCommande(@PathVariable("id") Long id){
        List<ColisAerien> liste_colis_aerien = colisAerienService.showColisAerien();
        List<ColisMaritime> liste_colis_maritime = colisMaritimeService.showColisMaritime();
        List<ProduitAerien> liste_produit_aerien = produitAerienService.showProduitsAerien();
        List<ProduitMaritime> liste_produit_maritime = produitMaritimeService.showProduitsMaritime();
        //System.out.println("**************Id = "+id+"****************************");
        if(commandeService.showOnecommande(id).getTypeEnvoi().equals(Constante.ENVOI_AERIEN)){
            if(colisAerienService.appartenanceColisAerien(liste_colis_aerien, id)){
                return "redirect:/colisAerien/produits";
            }else {
                return "redirect:/commande/info";
            }
        }else{
            if(colisMaritimeService.appartenanceColisMaritime(liste_colis_maritime,id)){
                return "redirect:/colisMaritime/produits";
            }else {
                return "redirect:/commande/info";
            }
        }
    }*/

    @GetMapping("/commande/annuler/{id}")
    public String reprendreCommande(@PathVariable("id") Long id) {
        List<ColisAerien> liste_colis_aerien = colisAerienService.showColisAerien();
        List<ColisMaritime> liste_colis_maritime = colisMaritimeService.showColisMaritime();
        List<ProduitAerien> liste_produit_aerien = produitAerienService.showProduitsAerien();
        List<ProduitMaritime> liste_produit_maritime = produitMaritimeService.showProduitsMaritime();
       // System.out.println("***********************IDCOM = "+commandeService.showOnecommande(id).getTypeEnvoi()+"***************************");
       if(produitAerienService.appartenanceProduitAerien(liste_produit_aerien, id)){
            produitAerienService.supprimerProduitCommande(id);
            colisAerienService.supprimerColisCommande(id);
        }else if(produitMaritimeService.appartenanceProduitMaritime(liste_produit_maritime, id)){
            produitMaritimeService.supprimerProduitCommande(id);
            colisMaritimeService.supprimerColisCommande(id);
        }else if(colisAerienService.appartenanceColisAerien(liste_colis_aerien, id)){
           colisAerienService.supprimerColisCommande(id);
       }else {
           colisMaritimeService.supprimerColisCommande(id);
       }
       ;commandeService.supprimerCommande(id);
        return "redirect:/enregistrements/inacheves";
    }


}
