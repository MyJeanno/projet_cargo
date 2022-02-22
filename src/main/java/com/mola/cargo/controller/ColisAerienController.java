package com.mola.cargo.controller;

import com.mola.cargo.model.Colis;
import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.Commande;
import com.mola.cargo.model.Tarif;
import com.mola.cargo.service.ColisAerienService;
import com.mola.cargo.service.CommandeService;
import com.mola.cargo.service.EmballageService;
import com.mola.cargo.service.TarifService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/colisAerien/form")
    public String afficherFormColisAerien(Model model){
        model.addAttribute("lastCommande", commandeService.showMaLastCommande());
        model.addAttribute("emballages", emballageService.showEmballages());
        return "colis/formColisAerien";
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
        colisAerien.setCommandeid(commandeService.showMaLastCommande().getId());
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

}
