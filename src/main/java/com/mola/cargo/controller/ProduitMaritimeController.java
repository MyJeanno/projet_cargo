package com.mola.cargo.controller;

import com.mola.cargo.model.ProduitAerien;
import com.mola.cargo.model.ProduitMaritime;
import com.mola.cargo.model.Tarif;
import com.mola.cargo.service.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProduitMaritimeController {

    @Autowired
    private ProduitMaritimeService produitMaritimeService;
    @Autowired
    private ColisMaritimeService colisMaritimeService;
    @Autowired
    private TarifService tarifService;
    @Autowired
    private CommandeService commandeService;
    @Autowired
    private CartonService cartonService;

    @GetMapping("/colisMaritime/produits")
    public String afficherProduitMaritime(Model model){
        double prixTotal = colisMaritimeService.montantTotalPrixCarton(commandeService.showMaLastCommande().getId())+
                           produitMaritimeService.taxe(produitMaritimeService.findProduitColisMaritime(commandeService.showMaLastCommande().getId()));
        model.addAttribute("produitsMaritime", produitMaritimeService.findProduitColisMaritime(commandeService.showMaLastCommande().getId()));
        model.addAttribute("tarifs", tarifService.showTarifs());
        model.addAttribute("lastColisMaritime", colisMaritimeService.showMaLastColisMaritime());
        model.addAttribute("prixTotal", String.format("% ,.2f",prixTotal));
        return "produit/produitMaritime";
    }

    //Pour enregistrer un nouveau produit
    @PostMapping("/produitMaritime/nouveau")
    public String enregistrerPMaritime(@RequestParam Long colisMaritimeid,
                                       @RequestParam Long tarifid,
                                       @RequestParam String designation,
                                       @RequestParam int quantite,
                                       @RequestParam double poids,
                                       @RequestParam double valeurMarchande){
        ProduitMaritime produitMaritime = new ProduitMaritime();
        produitMaritime.setColisMaritimeid(colisMaritimeid);
        produitMaritime.setTarifid(tarifid);
        produitMaritime.setDesignation(designation);
        produitMaritime.setQuantite(quantite);
        produitMaritime.setPoids(poids);
        produitMaritime.setValeurMarchande(valeurMarchande);
        produitMaritimeService.saveProduitMaritime(produitMaritime);
        return "redirect:/colisMaritime/produits";
    }

    //Formulaire de mise à jour d'un produit par voie maritime
    @GetMapping("/produitMaritime/formUpdate/{id}")
    public String showFormUpdateColisMaritime(@PathVariable("id") Long id, Model model){
        model.addAttribute("unProduit",produitMaritimeService.showOneProduitMaritime(id));
        model.addAttribute("tarifs", tarifService.showTarifs());
        return "produit/formUpdateProduitMaritime";
    }

    @PostMapping("/produitMaritime/update")
    public String updateProduitMaritime(@ModelAttribute("produitMaritime") ProduitMaritime produitMaritime){
        produitMaritimeService.saveProduitMaritime(produitMaritime);
        return "redirect:/colisMaritime/produits";
    }

    @GetMapping("/produitMaritime/delete")
    public String supprimerProduit(Long id){
        produitMaritimeService.deleteProduitMaritime(id);
        return "redirect:/colisMaritime/produits";
    }

    private String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    //Fonction pour générer la facture maritime payé
    @GetMapping("/colisMaritime/facture")
    public ResponseEntity<byte[]> factureMaritime() throws FileNotFoundException, JRException {
        List<ProduitMaritime> listeProdMaritime = produitMaritimeService.findProduitColisMaritime(commandeService.showMaLastCommande().getId());
        File file = ResourceUtils.getFile("classpath:factureMaritimePaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdMaritime);
        int petit = colisMaritimeService.nbreSelonCarton(commandeService.showMaLastCommande().getId(), "PC");
        int grand = colisMaritimeService.nbreSelonCarton(commandeService.showMaLastCommande().getId(), "GC");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", getPrincipal());
        parameter.put("nbre_colis", colisMaritimeService.nbreColisMaritime(commandeService.showMaLastCommande().getId()));
        parameter.put("taxe_maritime", produitMaritimeService.taxe(listeProdMaritime));
        parameter.put("nb_petit_carton", petit);
        parameter.put("nb_grand_carton", grand);
        if(petit!=0){
            parameter.put("montant_petit_carton", colisMaritimeService.montantPrixCarton(commandeService.showMaLastCommande().getId(), "PC"));
        }else{
            parameter.put("montant_petit_carton", 0);
        }
        if(grand!=0){
            parameter.put("montant_grand_carton", (double)colisMaritimeService.montantPrixCarton(commandeService.showMaLastCommande().getId(), "GC"));
        }else {
            parameter.put("montant_grand_carton", 0);
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture maritime non payé
    @GetMapping("/colisMaritime/facture_non_paye")
    public ResponseEntity<byte[]> factureMaritimeNonPaye() throws FileNotFoundException, JRException {
        List<ProduitMaritime> listeProdMaritime = produitMaritimeService.findProduitColisMaritime(commandeService.showMaLastCommande().getId());
        File file = ResourceUtils.getFile("classpath:factureMaritimeNonPaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdMaritime);
        int petit = colisMaritimeService.nbreSelonCarton(commandeService.showMaLastCommande().getId(), "PC");
        int grand = colisMaritimeService.nbreSelonCarton(commandeService.showMaLastCommande().getId(), "GC");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", getPrincipal());
        parameter.put("nbre_colis", colisMaritimeService.nbreColisMaritime(commandeService.showMaLastCommande().getId()));
        parameter.put("taxe_maritime", produitMaritimeService.taxe(listeProdMaritime));
        parameter.put("nb_petit_carton", petit);
        parameter.put("nb_grand_carton", grand);
        if(petit!=0){
            parameter.put("montant_petit_carton", colisMaritimeService.montantPrixCarton(commandeService.showMaLastCommande().getId(), "PC"));
        }else{
            parameter.put("montant_petit_carton", 0);
        }
        if(grand!=0){
            parameter.put("montant_grand_carton", colisMaritimeService.montantPrixCarton(commandeService.showMaLastCommande().getId(), "GC"));
        }else {
            parameter.put("montant_grand_carton", 0);
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }
}
