package com.mola.cargo.controller;

import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.Commande;
import com.mola.cargo.service.*;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ColisAerienController {

    @Autowired
    private ColisAerienService colisAerienService;
    @Autowired
    private CommandeService commandeService;
    @Autowired
    private TarifService tarifService;
    @Autowired
    private EmballageService emballageService;
    @Autowired
    private ReductionService tarifAerienService;
    @Autowired
    private ProduitAerienService produitAerienService;
    @Autowired
    private TransportService transportService;

    @GetMapping("/colisAerien/form")
    public String afficherFormColisAerien(Model model){
        model.addAttribute("lastCommande", commandeService.showMaLastCommande());
        model.addAttribute("emballages", emballageService.showEmballages());
        commandeService.updateTypeCommande(Constante.ENVOI_AERIEN, commandeService.showMaLastCommande().getId());
        return "colis/formColisAerien";
    }

    @GetMapping("/colisAerien/listePoids")
    public String afficherColisAerien(Model model){
        model.addAttribute("lesColis", colisAerienService.showColisAerienCommande(commandeService.showMaLastCommande().getId()));
        model.addAttribute("lastCommande", commandeService.showMaLastCommande());
        model.addAttribute("lastColisAerien", colisAerienService.showMaLastColisAerien());
        //model.addAttribute("prixTotal", String.format("% ,.2f", prixTotal));
        return "colis/poidsColisAerien";
    }

    @PostMapping("/colisAerien/poids")
    public String ajouterPoids(@RequestParam List<Double> poids){
      List<ColisAerien> ListeColisAeriens = colisAerienService.showColisAerienCommande(commandeService.showMaLastCommande().getId());
      int i =0;
      for (ColisAerien ca:ListeColisAeriens){
          if(ca.getCommande().getTransport().equals("Oui")){
              colisAerienService.updateToutColisAerien(colisAerienService.arrondirPoids(poids.get(i)),
                      produitAerienService.showMaxPrixProduit(ca.getId()),
                       colisAerienService.arrondirPoids(poids.get(i))*produitAerienService.showMaxPrixProduit(ca.getId()),
                      transportService.calculerPrixTransportAllemangne(colisAerienService.arrondirPoids(poids.get(i))),
                      ca.getId());
          }else {
              colisAerienService.updatePoidsColisAerien(colisAerienService.arrondirPoids(poids.get(i)),
                      produitAerienService.showMaxPrixProduit(ca.getId()),
                      colisAerienService.arrondirPoids(poids.get(i))*produitAerienService.showMaxPrixProduit(ca.getId()),
                      ca.getId());
          }
          i++;
      }
     return "redirect:/envoi/detail";

    }

    @PostMapping("/colisAerien/nouveau")
    public String creerColis(ColisAerien colisAerien){
        List<ColisAerien> listeAerienne = new ArrayList<>();
        Commande commande = new Commande();
        listeAerienne = colisAerienService.showColisAerien();
        String numero = commande.getPREFIX_COLIS()+""+commandeService.genererNbre(commande.getNBRE_INITIAL(), commande.getNBRE_FINAL());
        while(colisAerienService.testerAppartenance(listeAerienne, numero)){
            numero = commande.getPREFIX_COLIS()+""+commandeService.genererNbre(commande.getNBRE_INITIAL(), commande.getNBRE_FINAL());
        }
        colisAerien.setNumeroColis(numero);
        colisAerien.setStatut(Constante.INITIAL);
        colisAerien.setCommandeid(commandeService.showMaLastCommande().getId());
        colisAerien.setPoids(0.0);
        colisAerienService.saveColisAerien(colisAerien);
        return "redirect:/colisAerien/produits";
    }

    //Liste des colis par voie aérienne
    @GetMapping("/colisAerien/listes")
    public String afficherListeColisAerien(Model model){
        model.addAttribute("colisAerien", colisAerienService.showColisAerienCommande(commandeService.showMaLastCommande().getId()));
        model.addAttribute("lastCommande", commandeService.showMaLastCommande());
        return "colis/listeColisAerien";
    }

    //Renvoie le formulaire de saisie des colis par voie aérienne
    @GetMapping("/colisAerien/formUpdate/{id}")
    public String showFormUpdateColisAerien(@PathVariable("id") Long id, Model model){
        model.addAttribute("unColisAerien", colisAerienService.showOneColisAerien(id));
        model.addAttribute("emballages", emballageService.showEmballages());
        return "colis/formUpdateColisAerien";
    }

    @PostMapping("/colisAerien/update")
    public String updateColisAerien(@ModelAttribute("colisAerien") ColisAerien colisAerien){
        colisAerienService.saveColisAerien(colisAerien);
        return "redirect:/colisAerien/listes";
    }

    //Suppression d'un colis par voie aérienne
    @GetMapping("/colisAerien/delete")
    public String supprimerColis(Long id){
        colisAerienService.deleteColisAerien(id);
        return "redirect:/colisAerien/listes";
    }

    //Formulaire de choix d'une commande
    @GetMapping("/choixAerien/commande")
    public String showFormChoixCommande(){
        return "colis/choixCommandeAerien";
    }

    @GetMapping("/colisAerien/commande")
    public String showColisAerienCommande(@RequestParam String numCom, Model model){
        Long id = commandeService.showCommandePin(numCom).getId();
        //System.out.println("L'id de la commande est : "+id);
        model.addAttribute("colisAerien", colisAerienService.showColisAerienCommande(id));
        model.addAttribute("lastCommande", commandeService.showCommandePin(numCom));
        return "colis/listeColisAerien";
    }

}
