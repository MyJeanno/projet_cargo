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
    private ColisMaritimeService colisMaritimeService;
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
    private ProduitMaritimeService produitMaritimeService;
    @Autowired
    private TransportService transportService;
    @Autowired
    private SortieAerienService sortieAerienService;
    @Autowired
    private SortieMaritimeService sortieMaritimeService;
    @Autowired
    private ConvoiService convoiService;

    @GetMapping("/colisAerien/form")
    public String afficherFormColisAerien(Model model){
        model.addAttribute("lastCommande", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()));
        model.addAttribute("emballages", emballageService.showEmballages());
        commandeService.updateTypeCommande(Constante.ENVOI_AERIEN, commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        return "colis/formColisAerien";
    }

    @GetMapping("/colisAerien/formReprise/{pin}")
    public String afficherFormColisAerienReprise(@PathVariable("pin") String pin, Model model){
        Commande commande = commandeService.showCommandePin(pin);
        model.addAttribute("lastCommande", commande);
        model.addAttribute("emballages", emballageService.showEmballages());
        commandeService.updateTypeCommande(Constante.ENVOI_AERIEN, commande.getId());
        return "colis/formColisAerienReprise";
    }

    @GetMapping("/colis/poids")
    public String afficherStatPoidsDepot(Model model){
       //System.out.println("************************************ ID = "+convoiService.showMaLastConvoiAerien().getId());
        model.addAttribute("poidsAerien", colisAerienService.poidsTotalColisAerienDepot(Constante.INITIAL));
        model.addAttribute("poidsMaritime", colisMaritimeService.poidsTotalMaritimeDepot(Constante.INITIAL));
        model.addAttribute("quantiteM", colisMaritimeService.showColisMaritimeDepot(Constante.INITIAL).size());
        model.addAttribute("quantiteA", colisAerienService.showColisAerienDepot(Constante.INITIAL).size());
        /*if(convoiService.showMaLastConvoiAerien().getId()==null || convoiService.showMaLastConvoiMaritime().getId()==null){
            model.addAttribute("qteLotA", 0);
            model.addAttribute("qteLotM", 0);
            model.addAttribute("poidsLotA", 0);
            model.addAttribute("poidsLotM", 0);
            model.addAttribute("paLotCat", 0);
            model.addAttribute("pmLotCat", 0);
        }else {*/
            model.addAttribute("qteLotA", sortieAerienService.showSortieColisConvois(convoiService.showMaLastConvoiAerien().getId()).size());
            model.addAttribute("qteLotM", sortieMaritimeService.showSortieColisMaritimeConvois(convoiService.showMaLastConvoiMaritime().getId()).size());
            model.addAttribute("poidsLotA", sortieAerienService.poidsTotalColisAerienLot(convoiService.showMaLastConvoiAerien().getId()));
            model.addAttribute("poidsLotM", sortieMaritimeService.poidsTotalColisMaritimeLot(convoiService.showMaLastConvoiMaritime().getId()));
            model.addAttribute("produitCategorie", produitAerienService.PoidsParCategorieAlimentaire(Constante.INITIAL));
            model.addAttribute("pmcategorie", produitMaritimeService.PoidsParCategorieAlimentaire(Constante.INITIAL));
            model.addAttribute("paLotCat", sortieAerienService.PoidsParCategorieAlimentaire(convoiService.showMaLastConvoiAerien().getId()));
            model.addAttribute("pmLotCat", sortieMaritimeService.PoidsParCategorieAlimentaire(convoiService.showMaLastConvoiMaritime().getId()));
       // }
        return "colis/statistiquePoidsColis";
    }

    @GetMapping("/colisAerien/depot")
    public String afficherColisAerienDepot(Model model){
        model.addAttribute("colisAeriens", colisAerienService.showColisAerienDepot(Constante.INITIAL));
        model.addAttribute("quantite", colisAerienService.showColisAerienDepot(Constante.INITIAL).size());
        return "colis/listeColisAerienDepot";
    }

    @GetMapping("/colisAerien/listePoids")
    public String afficherColisAerien(Model model){
        model.addAttribute("lesColis", colisAerienService.showColisAerienCommande(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        model.addAttribute("lastCommande", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()));
        model.addAttribute("lastColisAerien", colisAerienService.showMaLastColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        //model.addAttribute("prixTotal", String.format("% ,.2f", prixTotal));
        return "colis/poidsColisAerien";
    }

    @GetMapping("/colisAerien/listePoidsBis/{pin}")
    public String afficherColisAerienReprise(@PathVariable("pin") String pin, Model model){
        Commande commande = commandeService.showCommandePin(pin);
        model.addAttribute("lesColis", colisAerienService.showColisAerienCommande(commande.getId()));
        model.addAttribute("lastCommande", commande);
        model.addAttribute("lastColisAerien", colisAerienService.showMaLastColisAerien(commande.getId()));
        //model.addAttribute("prixTotal", String.format("% ,.2f", prixTotal));
        return "colis/poidsColisAerienReprise";
    }

    @PostMapping("/colisAerien/poids")
    public String ajouterPoids(@RequestParam List<Double> poids){
      List<ColisAerien> ListeColisAeriens = colisAerienService.showColisAerienCommande(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
      commandeService.updateNbColisCommande(ListeColisAeriens.size(), commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
      //commandeService.updateEtatCommande(Constante.STATUT_COMMANDE_ACHEVE, commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
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

    @PostMapping("/colisAerien/poidsBis")
    public String ajouterPoidsBis(@RequestParam List<Double> poids, @RequestParam List<Double> frais){
        List<ColisAerien> ListeColisAeriens = colisAerienService.showColisAerienCommande(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        commandeService.updateNbColisCommande(ListeColisAeriens.size(), commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        //commandeService.updateEtatCommande(Constante.STATUT_COMMANDE_ACHEVE, commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        int i =0;
        for (ColisAerien ca:ListeColisAeriens){
                colisAerienService.updateToutColisAerien(colisAerienService.arrondirPoids(poids.get(i)),
                        produitAerienService.showMaxPrixProduit(ca.getId()),
                        colisAerienService.arrondirPoids(poids.get(i))*produitAerienService.showMaxPrixProduit(ca.getId()),
                        frais.get(i), ca.getId());
            i++;
        }
        return "redirect:/envoi/detail";
    }

    @PostMapping("/colisAerien/poidsReprise")
    public String ajouterPoidsReprise(@RequestParam List<Double> poids, @RequestParam("pin") String pin){
        Commande commande = commandeService.showCommandePin(pin);
        List<ColisAerien> ListeColisAeriens = colisAerienService.showColisAerienCommande(commande.getId());
        commandeService.updateNbColisCommande(ListeColisAeriens.size(), commande.getId());
        //commandeService.updateEtatCommande(Constante.STATUT_COMMANDE_ACHEVE, commande.getId());
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
        return "redirect:/envoi/detailReprise/?pin="+pin;
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

    @PostMapping("/colisAerien/nouveauReprise")
    public String creerColisReprise(@RequestParam("pin") String pin, ColisAerien colisAerien){
        List<ColisAerien> listeAerienne = new ArrayList<>();
        Commande commande = new Commande();
        listeAerienne = colisAerienService.showColisAerien();
        String numero = commande.getPREFIX_COLIS()+""+commandeService.genererNbre(commande.getNBRE_INITIAL(), commande.getNBRE_FINAL());
        while(colisAerienService.testerAppartenance(listeAerienne, numero)){
            numero = commande.getPREFIX_COLIS()+""+commandeService.genererNbre(commande.getNBRE_INITIAL(), commande.getNBRE_FINAL());
        }
        colisAerien.setNumeroColis(numero);
        colisAerien.setStatut(Constante.INITIAL);
        colisAerien.setCommandeid(commandeService.showCommandePin(pin).getId());
        colisAerien.setPoids(0.0);
        if(!commandeService.showOnecommande(commandeService.showCommandePin(pin).getId()).getEtatCommande().equals(Constante.STATUT_PRODUIT_CREE)){
            commandeService.updateEtatCommande(Constante.STATUT_COLIS_CREE, commandeService.showCommandePin(pin).getId());
        }
        colisAerienService.saveColisAerien(colisAerien);
        return "redirect:/commande/colisbis/?pin="+pin+"&num="+colisAerien.getId();
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
