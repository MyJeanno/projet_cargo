package com.mola.cargo.controller;

import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.Commande;
import com.mola.cargo.model.Inventaire;
import com.mola.cargo.model.ProduitAerien;
import com.mola.cargo.service.*;
import com.mola.cargo.util.Constante;
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
import java.time.LocalDate;
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
    @Autowired
    private InventaireService inventaireService;
    @Autowired
    private ReductionService tarifAerienService;
    @Autowired
    private RecepteurService recepteurService;

    private double getTotalApayer(){
        return  colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(commandeService.showMaLastCommande().getId()),
                commandeService.showMaLastCommande().getReduction())
                + produitAerienService.showMaxTaxeAerienne(commandeService.showMaLastCommande().getId())
                +colisAerienService.prixTransportColisAerien(commandeService.showMaLastCommande().getId());
    }

    @GetMapping("/colisAerien/produits")
    public String afficherProduitAerien(Model model){
        //System.out.println("Frais = "+produitAerienService.fraisEmballage());
        model.addAttribute("produitsAerien", produitAerienService.findProduitColisAerien(commandeService.showMaLastCommande().getId()));
        model.addAttribute("tarifs", tarifService.showTarifs());
        model.addAttribute("lastColisAerien", colisAerienService.showMaLastColisAerien());
        return "produit/produitAerien";
    }

    @GetMapping("/colisAerienReprise/produits/{id}")
    public String afficherProduitAerienReprise(@PathVariable("id") Long id, Model model){
        List<ColisAerien> liste_colis_aerien = colisAerienService.showColisAerien();
        model.addAttribute("produitsAerien", produitAerienService.findProduitColisAerien(colisAerienService.showOneColisAerien(id).getCommandeid()));
        model.addAttribute("tarifs", tarifService.showTarifs());
        model.addAttribute("lastColisAerien", colisAerienService.showOneColisAerien(id));
        if(colisAerienService.appartenanceColisAerien(liste_colis_aerien, colisAerienService.showOneColisAerien(id).getCommandeid())){
            return "produit/produitAerien";
        }else {
            return "redirect:/reprise/impossible";
        }
    }

    //Pour enregistrer un nouveau produit
    @PostMapping("/produitAerien/nouveau")
    public String enregistrerPAerien(@RequestParam Long colisAerienid,
                                     @RequestParam Long tarifid,
                                     @RequestParam String designation,
                                     @RequestParam int quantite,
                                     @RequestParam double poids,
                                     @RequestParam double valeurMarchande){
        ProduitAerien produitAerien = new ProduitAerien();
        produitAerien.setColisAerienid(colisAerienid);
        produitAerien.setTarifid(tarifid);
        produitAerien.setDesignation(designation);
        produitAerien.setQuantite(quantite);
        produitAerien.setPoids(poids);
        produitAerien.setValeurMarchande(valeurMarchande);

        /*List<Tarif> listPrix = new ArrayList<>();
        listPrix = tarifService.showTarifs();
        for(Tarif t : listPrix){
            if(produitAerien.getTarifid() == t.getId()){
                if(produitAerienService.estentier(produitAerien.getPoids())){
                    produitAerien.setPrixProduit(t.getPrixkilo()*produitAerien.getPoids());
                }else{
                    produitAerien.setPrixProduit(t.getPrixkilo()*((int)produitAerien.getPoids()+1));
                }
            }
        }*/

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
        /*List<Tarif> listPrix = new ArrayList<>();
        listPrix = tarifService.showTarifs();
        for(Tarif t : listPrix){
            if(produitAerien.getTarifid() == t.getId()){
                if(produitAerienService.estentier(produitAerien.getPoids())){
                    produitAerien.setPrixProduit(t.getPrixkilo()*produitAerien.getPoids());
                }else{
                    produitAerien.setPrixProduit(t.getPrixkilo()*((int)produitAerien.getPoids()+1));
                }
            }
        }*/
        produitAerienService.saveProduitAerien(produitAerien);
        return "redirect:/colisAerien/produits";
    }

    @GetMapping("/produitAerien/delete")
    public String supprimerProduitAerien(Long id){
        produitAerienService.deleteProduitAerien(id);
        return "redirect:/colisAerien/produits";
    }
    @GetMapping("/envoi/detail")
    public String afficherDetailCommande(Model model){
        model.addAttribute("lastCommande", commandeService.showMaLastCommande());
        model.addAttribute("nbColis", colisAerienService.nbreColisAerien(commandeService.showMaLastCommande().getId()));
        model.addAttribute("transport", String.format("% ,.2f", colisAerienService.prixTransportColisAerien(commandeService.showMaLastCommande().getId())));
        model.addAttribute("taxe", String.format("% ,.2f", produitAerienService.showMaxTaxeAerienne(commandeService.showMaLastCommande().getId())));
        model.addAttribute("prixColis", String.format("% ,.2f", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(commandeService.showMaLastCommande().getId()),
                commandeService.showMaLastCommande().getReduction())));
        model.addAttribute("prixTotal", String.format("% ,.2f", getTotalApayer()));
        return "commande/detailEnvoiAerien";
    }
    @PostMapping("/envoi/fin")
    public String finaliserCommande(@RequestParam String paye){
        commandeService.updatePaiementCommande(getTotalApayer(), Double.parseDouble(paye), commandeService.showMaLastCommande().getId());
        commandeService.updateEtatCommande(Constante.STATUT_COMMANDE_ACHEVE, commandeService.showMaLastCommande().getId());
        recepteurService.updateSoldeClient(getTotalApayer()-Double.parseDouble(paye), commandeService.showMaLastCommande().getRecepteur().getId());
        if(commandeService.showMaLastCommande().getLieuPaiement().equals(Constante.LIEU_TOGO)){
            return "redirect:/colisAerien/facture";
        }else{
            return "redirect:/colisAerien/facture_non_paye";
        }
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

    //Fonction pour générer la facture aerienne payée au Togo
    @GetMapping("/colisAerien/facture")
    public ResponseEntity<byte[]> factureAerienne() throws FileNotFoundException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(commandeService.showMaLastCommande().getId());
        File file = ResourceUtils.getFile("classpath:factureAeriennePaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", getPrincipal());
        parameter.put("nbre_colis", colisAerienService.nbreColisAerien(commandeService.showMaLastCommande().getId()));
        parameter.put("taxe", produitAerienService.showMaxTaxeAerienne(commandeService.showMaLastCommande().getId()));
        parameter.put("poids", colisAerienService.poidsTotalColisAerien(commandeService.showMaLastCommande().getId()));
        parameter.put("montantTotal", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(commandeService.showMaLastCommande().getId()),
                                      commandeService.showMaLastCommande().getReduction()));
        parameter.put("transportTotal", colisAerienService.prixTransportColisAerien(commandeService.showMaLastCommande().getId()));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+commandeService.getIdentitePersonnePaye(commandeService.showMaLastCommande().getId())+"-"+LocalDate.now()+".pdf");
        //Création d'un inventaire
        Inventaire inventaire = new Inventaire();
        double prixTotal = getTotalApayer();
        inventaire.setCommandeid(commandeService.showMaLastCommande().getId());
        inventaire.setStatus(Constante.INVENTAIRE_NON_ENCAISSE);
        inventaire.setNombreColis(colisAerienService.nbreColisAerien(commandeService.showMaLastCommande().getId()));
        inventaire.setPrixTotal(prixTotal);
        inventaire.setCommercial(getPrincipal());
        if(!inventaireService.testerAppartenance(commandeService.showMaLastCommande().getId())){
            inventaireService.saveInventaire(inventaire);
        }
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture aerienne non payée
    @GetMapping("/colisAerien/facture_non_paye")
    public ResponseEntity<byte[]> factureAerienneNonPaye() throws FileNotFoundException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(commandeService.showMaLastCommande().getId());
        File file = ResourceUtils.getFile("classpath:factureAerienneNonPaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", getPrincipal());
        parameter.put("nbre_colis", colisAerienService.nbreColisAerien(commandeService.showMaLastCommande().getId()));
        parameter.put("taxe", produitAerienService.showMaxTaxeAerienne(commandeService.showMaLastCommande().getId()));
        parameter.put("poids", colisAerienService.poidsTotalColisAerien(commandeService.showMaLastCommande().getId()));
        parameter.put("montantTotal", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(commandeService.showMaLastCommande().getId()),
                                    commandeService.showMaLastCommande().getReduction()));
        parameter.put("transportTotal", colisAerienService.prixTransportColisAerien(commandeService.showMaLastCommande().getId()));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+commandeService.getIdentitePersonneNonPaye(commandeService.showMaLastCommande().getId())+"-"+LocalDate.now()+".pdf");
        //Création d'un inventaire
        Inventaire inventaire = new Inventaire();
        double prixTotal = getTotalApayer();
        inventaire.setCommandeid(commandeService.showMaLastCommande().getId());
        inventaire.setStatus(Constante.INVENTAIRE_NON_ENCAISSE);
        inventaire.setNombreColis(colisAerienService.nbreColisAerien(commandeService.showMaLastCommande().getId()));
        inventaire.setPrixTotal(prixTotal);
        inventaire.setCommercial(getPrincipal());
        if(!inventaireService.testerAppartenance(commandeService.showMaLastCommande().getId())){
            inventaireService.saveInventaire(inventaire);
        }
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

}
