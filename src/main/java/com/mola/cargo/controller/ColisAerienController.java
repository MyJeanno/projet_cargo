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

    @GetMapping("/colisAerien/new")
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

    /*
    //Liste des colis par voie aérienne
    @GetMapping("/colisAerien/listes")
    public String afficherListeColisAerien(Model model){
        model.addAttribute("colis", colisAerienService.showColisAerienCommande(commandeService.showMaLastCommande().getId()));
        model.addAttribute("lastCommande", commandeService.showMaLastCommande());
        return "colis/listeColisAerien";
    }

    //Renvoie le formulaire de saisie des colis par voie aérienne
    @GetMapping("/colis/formColisAerien")
    public String showFormColisAerien(Model model){
        model.addAttribute("tarifs", tarifService.showTarifs());
        model.addAttribute("lastCommande", commandeService.showMaLastCommande());
        return "colis/formAddColisAerien";
    }

    private boolean estentier(double n){
        if(n==(int)n)
            return true;
        return false;
    }

    @PostMapping("/colis/nouveau/aerien")
    public String enregistrerColisAerien(ColisAerien colisAerien){
        List<Tarif> listPrix = new ArrayList<>();
        listPrix = tarifService.showTarifs();
        for(Tarif t : listPrix){
            if(colisAerien.getTarifid() == t.getId()){
                if(estentier(colisAerien.getPoids())){
                    colisAerien.setPrixColis(t.getPrixkilo()*colisAerien.getPoids());
                }else{
                    colisAerien.setPrixColis(t.getPrixkilo()*((int)colisAerien.getPoids()+1));
                }
            }
        }
        colisAerienService.saveColisAerien(colisAerien);
        return "redirect:/colisAerien/listes";
    }

    //Formulaire de mise à jour d'un colis par voie aérienne
    @GetMapping("/colis/formUpdate/{id}")
    public String showFormUpdateColis(@PathVariable("id") Long id, Model model){
        model.addAttribute("unColis",colisAerienService.showOneColisAerien(id));
        model.addAttribute("tarifs", tarifService.showTarifs());
        return "colis/formUpdateColisAerien";
    }

    //Mise à jour d'un colis par voie aérienne
    @PostMapping("/colis/update/aerien")
    public String updateColisAerien(@ModelAttribute("colis") ColisAerien colisAerien){
        List<Tarif> listPrix = new ArrayList<>();
        listPrix = tarifService.showTarifs();
        for(Tarif t : listPrix){
            if(colisAerien.getTarifid() == t.getId()){
                colisAerien.setPrixColis(t.getPrixkilo());
            }
        }
        colisAerienService.saveColisAerien(colisAerien);
        return "redirect:/colisAerien/listes";
    }

    //Suppression d'un colis par voie aérienne
    @GetMapping("/colis/delete/aerien")
    public String supprimerColis(Long id){
        colisAerienService.deleteColisAerien(id);
        return "redirect:/colisAerien/listes";
    }

    //Fonction pour générer la facture aerienne
    @GetMapping("/colis/facture/aerien")
    public ResponseEntity<byte[]> factureAerienne() throws FileNotFoundException, JRException {
        List<ColisAerien> listeColis = colisAerienService.showColisAerienCommande(commandeService.showMaLastCommande().getId());
        File file = ResourceUtils.getFile("classpath:factureAerienne.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeColis);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }*/

}
