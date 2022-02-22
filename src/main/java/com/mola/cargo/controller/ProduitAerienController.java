package com.mola.cargo.controller;

import com.mola.cargo.model.ProduitAerien;
import com.mola.cargo.model.ProduitMaritime;
import com.mola.cargo.model.Tarif;
import com.mola.cargo.service.ColisAerienService;
import com.mola.cargo.service.CommandeService;
import com.mola.cargo.service.ProduitAerienService;
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
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProduitAerienController {

    @Autowired
    private ProduitAerienService produitAerienService;
    @Autowired
    private TarifService tarifService;
    @Autowired
    private ColisAerienService colisAerienService;
    @Autowired
    private CommandeService commandeService;

    @GetMapping("/colisAerien/produits")
    public String afficherProduitAerien(Model model){
        model.addAttribute("produitsAerien", produitAerienService.findProduitColisAerien(commandeService.showMaLastCommande().getId()));
        model.addAttribute("tarifs", tarifService.showTarifs());
        model.addAttribute("lastColisAerien", colisAerienService.showMaLastColisAerien());
        return "produit/produitAerien";
    }

    //Pour enregistrer un nouveau produit
    @PostMapping("/produitAerien/nouveau")
    public String enregistrerPAerien(@RequestParam Long colisAerienid,
                                     @RequestParam Long tarifid,
                                     @RequestParam String designation,
                                     @RequestParam int quantite,
                                     @RequestParam double poids){
        ProduitAerien produitAerien = new ProduitAerien();
        produitAerien.setColisAerienid(colisAerienid);
        produitAerien.setTarifid(tarifid);
        produitAerien.setDesignation(designation);
        produitAerien.setQuantite(quantite);
        produitAerien.setPoids(poids);
        List<Tarif> listPrix = new ArrayList<>();
        listPrix = tarifService.showTarifs();
        for(Tarif t : listPrix){
            if(produitAerien.getTarifid() == t.getId()){
                if(produitAerienService.estentier(produitAerien.getPoids())){
                    produitAerien.setPrixProduit(t.getPrixkilo()*produitAerien.getPoids());
                }else{
                    produitAerien.setPrixProduit(t.getPrixkilo()*((int)produitAerien.getPoids()+1));
                }
            }
        }
        produitAerienService.saveProduitAerien(produitAerien);
        return "redirect:/colisAerien/produits";
    }

    @GetMapping("/produitAerien/formUpdate/{id}")
    public String showFormUpdateColisAerien(@PathVariable("id") Long id, Model model){
        model.addAttribute("unProduit",produitAerienService.showOneProduitAerien(id));
        model.addAttribute("tarifs", tarifService.showTarifs());
        return "produit/formUpdateProduitAerien";
    }

    @PostMapping("/produitAerien/update")
    public String updateProduitAerien(@ModelAttribute("produitAerien") ProduitAerien produitAerien){
        List<Tarif> listPrix = new ArrayList<>();
        listPrix = tarifService.showTarifs();
        for(Tarif t : listPrix){
            if(produitAerien.getTarifid() == t.getId()){
                if(produitAerienService.estentier(produitAerien.getPoids())){
                    produitAerien.setPrixProduit(t.getPrixkilo()*produitAerien.getPoids());
                }else{
                    produitAerien.setPrixProduit(t.getPrixkilo()*((int)produitAerien.getPoids()+1));
                }
            }
        }
        produitAerienService.saveProduitAerien(produitAerien);
        return "redirect:/colisAerien/produits";
    }

    @GetMapping("/produitAerien/delete")
    public String supprimerProduitAerien(Long id){
        produitAerienService.deleteProduitAerien(id);
        return "redirect:/colisAerien/produits";
    }

    //Fonction pour générer la facture aerienne
    @GetMapping("/colisAerien/facture")
    public ResponseEntity<byte[]> factureAerienne() throws FileNotFoundException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(commandeService.showMaLastCommande().getId());
        File file = ResourceUtils.getFile("classpath:factureAerienne.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("nbre_colis", produitAerienService.nbreColisAerien(commandeService.showMaLastCommande().getId()));
        parameter.put("taxe", produitAerienService.taxe(listeProdAerien));
        parameter.put("frais_emballage", produitAerienService.fraisEmballage(colisAerienService.showColisAerienCommande(commandeService.showMaLastCommande().getId())));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }


}
