package com.mola.cargo.controller;

import com.mola.cargo.model.*;
import com.mola.cargo.service.*;
import com.mola.cargo.util.Constante;
import groovy.transform.AutoClone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

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
    @Autowired
    private TarifService tarifService;
    @Autowired
    private SortieAerienService sortieAerienService;
    @Autowired
    private SortieMaritimeService sortieMaritimeService;

    @GetMapping("/mode/envoi")
    public String choisirModeEnvoi(){
        return "commande/choix";
    }

    @GetMapping("commande/new")
    public String afficherFormCommande(){
        return "commande/formCommande";
    }

    @GetMapping("/commande/formReprise")
    public String formRepriseCommande(){
        return "commande/FormRepriseCommande";
    }

    @GetMapping("/commande/formSuppression")
    public String formSuppressionCommande(){
        return "commande/formSuppressionCommande";
    }
    @PostMapping("/commande/facture")
    public String showOneCommande(@RequestParam("pin") String pin, Model model){
        model.addAttribute("uneCommande", commandeService.showCommandePin(pin));
        return "commande/uneCommande";
    }

    @GetMapping("/commande/suppression/{id}")
    public String supprimerCommande(@PathVariable("id") Long id) {
        if(commandeService.showOnecommande(id).getTypeEnvoi().equals(Constante.ENVOI_AERIEN)){
            sortieAerienService.supprimerColisSortieAerien(colisAerienService.showColisAerienCommande(id));
            produitAerienService.supprimerProduitCommande(colisAerienService.showColisAerienCommande(id));
            colisAerienService.supprimerColisCommande(id);
            commandeService.supprimerCommande(id);
        }else{
            sortieMaritimeService.supprimerColisSortieMaritime(colisMaritimeService.showColisMaritimeCommande(id));
            produitMaritimeService.supprimerProduitCommande(colisMaritimeService.showColisMaritimeCommande(id));
            colisMaritimeService.supprimerColisCommande(id);
            commandeService.supprimerCommande(id);
        }
        return "redirect:/commande/formSuppression";
    }

    @PostMapping("/commande/reprise")
    public String listeColisCommande(@RequestParam("pin") String pin, Model model){
        model.addAttribute("numCom", pin);
        if(commandeService.showCommandePin(pin).getTypeEnvoi().equals(Constante.ENVOI_AERIEN)){
        model.addAttribute("colisCommande", colisAerienService.showColisAerienCommande(commandeService.showCommandePin(pin).getId()));
        }else {
            model.addAttribute("colisCommande", colisMaritimeService.showColisMaritimeCommande(commandeService.showCommandePin(pin).getId()));
        }
        return "commande/formColisCommande";
    }

    @GetMapping("/commande/colis")
    public String ajouterProduitColis(@RequestParam("num") String num, @RequestParam("pin") String pin, Model model){
        Commande commande = commandeService.showCommandePin(pin);
        if(commande.getTypeEnvoi().equals(Constante.ENVOI_AERIEN)){
            model.addAttribute("produitsAerien", produitAerienService.findProduitColisAerien(commande.getId()));
            model.addAttribute("tarifs", tarifService.showTarifs());
            model.addAttribute("lastColisAerien", colisAerienService.showOneColisAerien(Long.parseLong(num)));
            model.addAttribute("reprise", "OUI");
            model.addAttribute("pin", pin);
            model.addAttribute("idColis", Long.parseLong(num));
            return "produit/produitAerien";
        }else {
            model.addAttribute("produitsMaritime", produitMaritimeService.findProduitColisMaritime(commande.getId()));
            model.addAttribute("tarifs", tarifService.showTarifs());
            model.addAttribute("lastColisMaritime", colisMaritimeService.showOneColisMaritime(Long.parseLong(num)));
            model.addAttribute("reprise", "OUI");
            model.addAttribute("pin", pin);
            model.addAttribute("idColis", Long.parseLong(num));
            return "produit/produitMaritime";
        }
    }

    @GetMapping("/commande/colisbis")
    public String ajouterProduitColisBis(String pin, Long num, Model model){
        Commande commande = commandeService.showCommandePin(pin);
        if(commande.getTypeEnvoi().equals(Constante.ENVOI_AERIEN)){
            model.addAttribute("produitsAerien", produitAerienService.findProduitColisAerien(commande.getId()));
            model.addAttribute("tarifs", tarifService.showTarifs());
            model.addAttribute("lastColisAerien", colisAerienService.showOneColisAerien(num));
            model.addAttribute("reprise", "OUI");
            model.addAttribute("pin", pin);
            model.addAttribute("idColis", num);
            return "produit/produitAerien";
        }else {
            model.addAttribute("produitsMaritime", produitMaritimeService.findProduitColisMaritime(commande.getId()));
            model.addAttribute("tarifs", tarifService.showTarifs());
            model.addAttribute("lastColisMaritime", colisMaritimeService.showOneColisMaritime(num));
            model.addAttribute("reprise", "OUI");
            model.addAttribute("pin", pin);
            model.addAttribute("idColis", num);
            return "produit/produitMaritime";
        }
    }

    //Renvoie le formulaire de la nouvelle commande
   /* @GetMapping("/commande/formCommande")
    public String showNouvelleCommande(Long idE, Long idR, Model model){
        if(idE!=null && idR!=null){
            model.addAttribute("unEmetteur", emetteurService.showOneEmetteur(idE));
            model.addAttribute("unRecepteur", recepteurService.showOneRecepteur(idR));
        }else {
            model.addAttribute("unEmetteur", emetteurService.showMonDernierEmetteur(Constante.showUserConnecte().getId()));
            model.addAttribute("unRecepteur", recepteurService.showMonDernierRecepteur(Constante.showUserConnecte().getId()));
        }

        model.addAttribute("pieces", pieceService.showPiece());
        model.addAttribute("modePaiments", paiementService.showPaiement());
        model.addAttribute("uneReduction", reductionService.lareduction());
        return "commande/formNewCommande";
    }*/

    @GetMapping("/commande/formCommande")
    public String showNouvelleCommande(Long idE, Long idR, Model model){

        model.addAttribute("unEmetteur", emetteurService.showOneEmetteur(idE));
        model.addAttribute("unRecepteur", recepteurService.showOneRecepteur(idR));
        model.addAttribute("pays", paysService.showPays());
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
        commande.setEtatCommande(Constante.STATUT_COMMANDE_CREE);
        commande.setUserid(Constante.showUserConnecte().getId());
        commande.setStatutCommande(Constante.INVENTAIRE_NON_ENCAISSE);
        if(commandeService.showCommandeInacheve(Constante.STATUT_COMMANDE_ACHEVE, Constante.showUserConnecte().getId()).size()==0){
            commandeService.saveCommande(commande);
            return "redirect:/mode/envoi";
        }else{
            return "redirect:/commande/info";
        }
    }
    @GetMapping("/enregistrements/inacheves")
    public String commandeInacheve(Model model){
        model.addAttribute("commande_inacheve", commandeService.showCommandeInacheve(Constante.STATUT_COMMANDE_ACHEVE, Constante.showUserConnecte().getId()));
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
        if(commandeService.showOnecommande(id).getEtatCommande().equals(Constante.STATUT_COMMANDE_CREE)) {
            commandeService.supprimerCommande(id);
            return "redirect:/enregistrements/inacheves";
        }
        if(commandeService.showOnecommande(id).getTypeEnvoi().equals(Constante.ENVOI_AERIEN)){
            if(commandeService.showOnecommande(id).getEtatCommande().equals(Constante.STATUT_COLIS_CREE)){
                colisAerienService.supprimerColisCommande(id);
                commandeService.supprimerCommande(id);
            }else if(commandeService.showOnecommande(id).getEtatCommande().equals(Constante.STATUT_PRODUIT_CREE)){
                produitAerienService.supprimerProduitCommande(colisAerienService.showColisAerienCommande(id));
                colisAerienService.supprimerColisCommande(id);
                commandeService.supprimerCommande(id);
            }
        }else{
            if(commandeService.showOnecommande(id).getEtatCommande().equals(Constante.STATUT_COLIS_CREE)){
                colisMaritimeService.supprimerColisCommande(id);
                commandeService.supprimerCommande(id);
            }else if(commandeService.showOnecommande(id).getEtatCommande().equals(Constante.STATUT_PRODUIT_CREE)){
                produitMaritimeService.supprimerProduitCommande(colisMaritimeService.showColisMaritimeCommande(id));
                colisMaritimeService.supprimerColisCommande(id);
                commandeService.supprimerCommande(id);
            }
        }
        return "redirect:/enregistrements/inacheves";
    }

}
