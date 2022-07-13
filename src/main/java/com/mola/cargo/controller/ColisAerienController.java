package com.mola.cargo.controller;

import com.mola.cargo.model.*;
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
        model.addAttribute("lastCommande", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()));
        model.addAttribute("emballages", emballageService.showEmballages());
        commandeService.updateTypeCommande(Constante.ENVOI_AERIEN, commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        return "colis/formColisAerien";
    }

    @GetMapping("/colisAerien/listePoids")
    public String afficherColisAerien(Model model){
        model.addAttribute("lesColis", colisAerienService.showColisAerienCommande(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        model.addAttribute("lastCommande", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()));
        model.addAttribute("lastColisAerien", colisAerienService.showMaLastColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        //model.addAttribute("prixTotal", String.format("% ,.2f", prixTotal));
        return "colis/poidsColisAerien";
    }

    @PostMapping("/colisAerien/poids")
    public String ajouterPoids(@RequestParam List<Double> poids){
      List<ColisAerien> ListeColisAeriens = colisAerienService.showColisAerienCommande(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
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
        colisAerien.setCommandeid(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        colisAerien.setPoids(0.0);
        if(!commandeService.showOnecommande(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()).getEtatCommande().equals(Constante.STATUT_PRODUIT_CREE)){
            commandeService.updateEtatCommande(Constante.STATUT_COLIS_CREE, commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        }
        colisAerienService.saveColisAerien(colisAerien);
        return "redirect:/colisAerien/produits";
    }

    //Liste des colis par voie aérienne
    @GetMapping("/colisAerien/listes")
    public String afficherListeColisAerien(Model model){
        model.addAttribute("colisAerien", colisAerienService.showColisAerienCommande(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        model.addAttribute("lastCommande", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()));
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
    @GetMapping("/commande/reprise/{id}")
    public String colisInacheve(@PathVariable("id") Long id, Model model){
        model.addAttribute("liste_colis", colisAerienService.showColisAerienCommande(id));
        return "colis/colisInacheve";
    }

    @GetMapping("/colis/reprise/{id}")
    public String reprendreCommande(@PathVariable("id") Long id){
        if(commandeService.showOnecommande(id).getTypeEnvoi().equals(Constante.ENVOI_AERIEN)){
            return "redirect:/colisAerienReprise/produits/{id}";
        }else{
                return "redirect:/colisMaritimeReprise/produits{id}";
        }
    }

}
