package com.mola.cargo.controller;

import com.mola.cargo.model.*;
import com.mola.cargo.service.*;
import com.mola.cargo.util.Constante;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
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
    @Autowired
    private InventaireService inventaireService;
    @Autowired
    private RecepteurService recepteurService;
    @Autowired
    private ResourceLoader resourceLoader;

    private double getTotalApayer(){
        return  colisMaritimeService.appliquerReduction(colisMaritimeService.montantTotalPrixCarton(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()),
                commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getReduction())
                + produitMaritimeService.MaxTaxeCommandeMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())
                + colisMaritimeService.montantTotalTransport(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
    }

    @GetMapping("/colisMaritime/produits")
    public String afficherProduitMaritime(Model model){
        model.addAttribute("produitsMaritime", produitMaritimeService.findProduitColisMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        model.addAttribute("tarifs", tarifService.showTarifs());
        model.addAttribute("lastColisMaritime", colisMaritimeService.showMaLastColisMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        model.addAttribute("reprise", "NON");
        return "produit/produitMaritime";
    }

    @GetMapping("/colisMaritimeReprise/produits{id}")
    public String afficherProduitMaritimeCommande(@PathVariable("id") Long id, Model model){
        List<ColisMaritime> liste_colis_maritime = colisMaritimeService.showColisMaritime();
        model.addAttribute("produitsMaritime", produitMaritimeService.findProduitColisMaritime(colisMaritimeService.showOneColisMaritime(id).getCommandeid()));
        model.addAttribute("tarifs", tarifService.showTarifs());
        model.addAttribute("lastColisMaritime", colisMaritimeService.showOneColisMaritime(id));
        if(colisMaritimeService.appartenanceColisMaritime(liste_colis_maritime, colisMaritimeService.showOneColisMaritime(id).getCommandeid())){
            return "produit/produitMaritime";
        }else{
            return "redirect:/reprise/impossible";
        }
    }

    //Pour enregistrer un nouveau produit
    @PostMapping("/produitMaritime/nouveau")
    public String enregistrerPMaritime(@RequestParam Long colisMaritimeid,
                                       @RequestParam Long tarifid,
                                       @RequestParam String designation,
                                       @RequestParam int quantite,
                                       @RequestParam double poids,
                                       @RequestParam String reprise,
                                       @RequestParam String num,
                                       @RequestParam String pin,
                                       @RequestParam double valeurMarchande){
        ProduitMaritime produitMaritime = new ProduitMaritime();
        produitMaritime.setColisMaritimeid(colisMaritimeid);
        produitMaritime.setTarifid(tarifid);
        produitMaritime.setDesignation(designation);
        produitMaritime.setQuantite(quantite);
        produitMaritime.setPoids(poids);
        produitMaritime.setValeurMarchande(valeurMarchande);
        produitMaritimeService.saveProduitMaritime(produitMaritime);
        //colisMaritimeService.updateTransportColisMaritime(transportService.calculerPrixTransportAllemangne(produitMaritimeService.sommePoidsColisMaritime(colisMaritimeid)),colisMaritimeid);
        if(reprise.equals("OUI")){
            commandeService.updateEtatCommande(Constante.STATUT_PRODUIT_CREE, commandeService.showCommandePin(pin).getId());
            return "redirect:/commande/colisbis/?pin="+pin+"&num="+num;
        }else {
            commandeService.updateEtatCommande(Constante.STATUT_PRODUIT_CREE, commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
            return "redirect:/colisMaritime/produits";
        }
    }

    //Formulaire de mise à jour d'un produit par voie maritime
    @GetMapping("/produitMaritime/formUpdate/{id}")
    public String showFormUpdateColisMaritime(@PathVariable("id") Long id, Model model){
        model.addAttribute("unProduit",produitMaritimeService.showOneProduitMaritime(id));
        model.addAttribute("tarifs", tarifService.showTarifs());
        return "produit/formUpdateProduitMaritime";
    }

    @GetMapping("/produitMaritime/formUpdateReprise/{id}")
    public String showFormUpdateColisMaritimeReprise(@PathVariable("id") Long id, Model model){
        model.addAttribute("unProduit",produitMaritimeService.showOneProduitMaritime(id));
        model.addAttribute("tarifs", tarifService.showTarifs());
        return "produit/formUpdateProduitMaritimeReprise";
    }

    @PostMapping("/produitMaritime/update")
    public String updateProduitMaritime(@ModelAttribute("produitMaritime") ProduitMaritime produitMaritime){
        produitMaritimeService.saveProduitMaritime(produitMaritime);
        return "redirect:/colisMaritime/produits";
    }

    @PostMapping("/produitMaritime/updateReprise")
    public String updateProduitMaritimeReprise(@ModelAttribute("produitMaritime") ProduitMaritime produitMaritime){
        ColisMaritime colisMaritime = (ColisMaritime) colisMaritimeService.showOneColisMaritime(produitMaritime.getColisMaritimeid());
        produitMaritimeService.saveProduitMaritime(produitMaritime);
        return "redirect:/commande/colisbis/?pin="+colisMaritime.getCommande().getPin()+"&num="+colisMaritime.getId();
    }

    @GetMapping("/produitMaritime/delete")
    public String supprimerProduit(Long id){
        produitMaritimeService.deleteProduitMaritime(id);
        return "redirect:/colisMaritime/produits";
    }

    @GetMapping("/produitMaritime/deleteReprise")
    public String supprimerProduitReprise(Long id){
        ProduitMaritime produitMaritime = produitMaritimeService.showOneProduitMaritime(id);
        ColisMaritime colisMaritime = (ColisMaritime) colisMaritimeService.showOneColisMaritime(produitMaritime.getColisMaritimeid());
        produitMaritimeService.deleteProduitMaritime(id);
        return "redirect:/commande/colisbis/?pin="+colisMaritime.getCommande().getPin()+"&num="+colisMaritime.getId();
    }

    @GetMapping("/envoiMaritime/detail")
    public String afficherDetailCommande(Model model){
        model.addAttribute("lastCommande", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()));
        model.addAttribute("nbColis", colisMaritimeService.nbreColisMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        model.addAttribute("transport", String.format("% ,.2f", colisMaritimeService.montantTotalTransport(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())));
        model.addAttribute("taxe", String.format("% ,.2f", produitMaritimeService.MaxTaxeCommandeMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())));
        model.addAttribute("prixColis", String.format("% ,.2f", colisMaritimeService.appliquerReduction(colisMaritimeService.montantTotalPrixCarton(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()),
                commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getReduction())));
        model.addAttribute("prixTotal", String.format("% ,.2f", getTotalApayer()));
        model.addAttribute("prixTotal2", getTotalApayer());
        return "commande/detailEnvoiMaritime";
    }

    @GetMapping("/envoiMaritime/detailReprise")
    public String afficherDetailCommandeReprise(String pin, Model model){
        Commande commande = commandeService.showCommandePin(pin);
        double total = colisMaritimeService.appliquerReduction(colisMaritimeService.montantTotalPrixCarton(commande.getId()), commande.getReduction())
                + produitMaritimeService.MaxTaxeCommandeMaritime(commande.getId())
                + colisMaritimeService.montantTotalTransport(commande.getId());

        model.addAttribute("lastCommande", commande);
        model.addAttribute("nbColis", colisMaritimeService.nbreColisMaritime(commande.getId()));
        //model.addAttribute("transport", String.format("% ,.2f", colisMaritimeService.montantTotalTransport(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())));
        model.addAttribute("taxe", String.format("% ,.2f", produitMaritimeService.MaxTaxeCommandeMaritime(commande.getId())));
        model.addAttribute("prixColis", String.format("% ,.2f", colisMaritimeService.appliquerReduction(colisMaritimeService.montantTotalPrixCarton(commande.getId()), commande.getReduction())));
        model.addAttribute("prixTotal", String.format("% ,.2f", total));
        model.addAttribute("prixTotal2", total);
        return "commande/detailEnvoiMaritimeReprise";
    }
    @PostMapping("/envoiMaritime/fin")
    public String finaliserCommande(@RequestParam String paye){
        commandeService.updatePaiementCommande(getTotalApayer(), Double.parseDouble(paye), commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        commandeService.updateEtatCommande(Constante.STATUT_COMMANDE_ACHEVE, commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        recepteurService.updateSoldeClient(getTotalApayer()-Double.parseDouble(paye), commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getRecepteur().getId());
        if(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getLieuPaiement().equals(Constante.LIEU_TOGO)){
            return "redirect:/colisMaritime/facture";
        }else{
            return "redirect:/colisMaritime/facture_non_paye";
        }
    }

    @PostMapping("/envoiMaritime/finReprise")
    public String finaliserCommandeReprise(@RequestParam String paye, @RequestParam("pin") String pin){
        Commande commande = commandeService.showCommandePin(pin);
        double total = colisMaritimeService.appliquerReduction(colisMaritimeService.montantTotalPrixCarton(commande.getId()), commande.getReduction())
                + produitMaritimeService.MaxTaxeCommandeMaritime(commande.getId())
                + colisMaritimeService.montantTotalTransport(commande.getId());
        commandeService.updatePaiementCommande(total, Double.parseDouble(paye), commande.getId());
        commandeService.updateEtatCommande(Constante.STATUT_COMMANDE_ACHEVE, commande.getId());
        recepteurService.updateSoldeClient(total-Double.parseDouble(paye), commande.getRecepteur().getId());
        if(commande.getLieuPaiement().equals(Constante.LIEU_TOGO)){
            return "redirect:/colisMaritime/factureReprise/?id="+commande.getId();
        }else{
            return "redirect:/colisMaritime/facture_non_payeReprise/?id="+commande.getId();
        }
    }

    //Fonction pour générer la facture maritime payé
    @GetMapping("/colisMaritime/facture")
    public ResponseEntity<byte[]> factureMaritime() throws IOException, JRException {
        List<ProduitMaritime> listeProdMaritime = produitMaritimeService.findProduitColisMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        Resource resource = resourceLoader.getResource("classpath:factureMaritimePaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdMaritime);
        int petit = colisMaritimeService.nbreSelonCarton(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId(), "PC");
        int grand = colisMaritimeService.nbreSelonCarton(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId(), "GC");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("chemin_logo", "head.png");
        parameter.put("tampon_paye", "tampon_paye.PNG");
        parameter.put("user", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getUser().getPrenom());
        parameter.put("nbre_colis", colisMaritimeService.nbreColisMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        parameter.put("taxe_maritime", produitMaritimeService.MaxTaxeCommandeMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        parameter.put("nb_petit_carton", petit);
        parameter.put("nb_grand_carton", grand);
        parameter.put("total_transport", colisMaritimeService.montantTotalTransport(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        if(petit!=0){
            parameter.put("montant_petit_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId(), "PC"), commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getReduction()));
        }else{
            parameter.put("montant_petit_carton", 0.0);
        }
        if(grand!=0){
            parameter.put("montant_grand_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId(), "GC"),commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getReduction()));
        }else {
            parameter.put("montant_grand_carton", 0.0);
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+commandeService.getIdentitePersonnePaye(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())+"-"+ LocalDate.now()+".pdf");
        /*Création d'un inventaire
        Inventaire inventaire = new Inventaire();
        double prixTotal = colisMaritimeService.appliquerReduction(colisMaritimeService.montantTotalPrixCarton(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()),
                           commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getReduction())
                           + produitMaritimeService.MaxTaxeCommandeMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())
                           + colisMaritimeService.montantTotalTransport(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        inventaire.setCommandeid(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        inventaire.setStatus(Constante.INVENTAIRE_NON_ENCAISSE);
        inventaire.setNombreColis(colisMaritimeService.nbreColisMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        inventaire.setPrixTotal(prixTotal);
        inventaire.setCommercial(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getUser().getPrenom());
        if(!inventaireService.testerAppartenance(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())){
            inventaireService.saveInventaire(inventaire);
        }*/
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    @GetMapping("/colisMaritime/factureReprise")
    public ResponseEntity<byte[]> factureMaritimeReprise(Long id) throws IOException, JRException {
        List<ProduitMaritime> listeProdMaritime = produitMaritimeService.findProduitColisMaritime(id);
        Resource resource = resourceLoader.getResource("classpath:factureMaritimePaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdMaritime);
        int petit = colisMaritimeService.nbreSelonCarton(id, "PC");
        int grand = colisMaritimeService.nbreSelonCarton(id, "GC");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("chemin_logo", "head.png");
        parameter.put("tampon_paye", "tampon_paye.PNG");
        parameter.put("user", commandeService.showOnecommande(id).getUser().getPrenom());
        parameter.put("nbre_colis", colisMaritimeService.nbreColisMaritime(id));
        parameter.put("taxe_maritime", produitMaritimeService.MaxTaxeCommandeMaritime(id));
        parameter.put("nb_petit_carton", petit);
        parameter.put("nb_grand_carton", grand);
        parameter.put("total_transport", colisMaritimeService.montantTotalTransport(id));
        if(petit!=0){
            parameter.put("montant_petit_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(id, "PC"), commandeService.showOnecommande(id).getReduction()));
        }else{
            parameter.put("montant_petit_carton", 0.0);
        }
        if(grand!=0){
            parameter.put("montant_grand_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(id, "GC"),commandeService.showOnecommande(id).getReduction()));
        }else {
            parameter.put("montant_grand_carton", 0.0);
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+commandeService.getIdentitePersonnePaye(id)+"-"+ LocalDate.now()+".pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture maritime non payé
    @GetMapping("/colisMaritime/facture_non_paye")
    public ResponseEntity<byte[]> factureMaritimeNonPaye() throws IOException, JRException {
        List<ProduitMaritime> listeProdMaritime = produitMaritimeService.findProduitColisMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        Resource resource = resourceLoader.getResource("classpath:factureMaritimeNonPaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdMaritime);
        int petit = colisMaritimeService.nbreSelonCarton(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId(), "PC");
        int grand = colisMaritimeService.nbreSelonCarton(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId(), "GC");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("chemin_logo", "head.png");
        parameter.put("tampon_non_paye", "tamponAll.PNG");
        parameter.put("logo_paypal", "paypal.png");
        parameter.put("user", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getUser().getPrenom());
        parameter.put("nbre_colis", colisMaritimeService.nbreColisMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        parameter.put("taxe_maritime", produitMaritimeService.MaxTaxeCommandeMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        parameter.put("nb_petit_carton", petit);
        parameter.put("nb_grand_carton", grand);
        parameter.put("total_transport", colisMaritimeService.montantTotalTransport(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        if(petit!=0){
            parameter.put("montant_petit_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId(), "PC"), commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getReduction()));
        }else{
            parameter.put("montant_petit_carton", 0.0);
        }
        if(grand!=0){
            parameter.put("montant_grand_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId(), "GC"),commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getReduction()));
        }else {
            parameter.put("montant_grand_carton", 0.0);
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+commandeService.getIdentitePersonneNonPaye(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())+"-"+LocalDate.now()+".pdf");
        /*Création d'un inventaire
        Inventaire inventaire = new Inventaire();
        double prixTotal = colisMaritimeService.appliquerReduction(colisMaritimeService.montantTotalPrixCarton(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()),
                commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getReduction())
                + produitMaritimeService.MaxTaxeCommandeMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())
                + colisMaritimeService.montantTotalTransport(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        inventaire.setCommandeid(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        inventaire.setStatus(Constante.INVENTAIRE_NON_ENCAISSE);
        inventaire.setNombreColis(colisMaritimeService.nbreColisMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        inventaire.setPrixTotal(prixTotal);
        inventaire.setCommercial(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getUser().getPrenom());
        if(!inventaireService.testerAppartenance(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())){
            inventaireService.saveInventaire(inventaire);
        }*/
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    @GetMapping("/colisMaritime/facture_non_payeReprise")
    public ResponseEntity<byte[]> factureMaritimeNonPayeReprise(Long id) throws IOException, JRException {
        List<ProduitMaritime> listeProdMaritime = produitMaritimeService.findProduitColisMaritime(id);
        Resource resource = resourceLoader.getResource("classpath:factureMaritimeNonPaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdMaritime);
        int petit = colisMaritimeService.nbreSelonCarton(id, "PC");
        int grand = colisMaritimeService.nbreSelonCarton(id, "GC");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("chemin_logo", "head.png");
        parameter.put("tampon_non_paye", "tamponAll.PNG");
        parameter.put("logo_paypal", "paypal.png");
        parameter.put("user", commandeService.showOnecommande(id).getUser().getPrenom());
        parameter.put("nbre_colis", colisMaritimeService.nbreColisMaritime(id));
        parameter.put("taxe_maritime", produitMaritimeService.MaxTaxeCommandeMaritime(id));
        parameter.put("nb_petit_carton", petit);
        parameter.put("nb_grand_carton", grand);
        parameter.put("total_transport", colisMaritimeService.montantTotalTransport(id));
        if(petit!=0){
            parameter.put("montant_petit_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(id, "PC"), commandeService.showOnecommande(id).getReduction()));
        }else{
            parameter.put("montant_petit_carton", 0.0);
        }
        if(grand!=0){
            parameter.put("montant_grand_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(id, "GC"),commandeService.showOnecommande(id).getReduction()));
        }else {
            parameter.put("montant_grand_carton", 0.0);
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+commandeService.getIdentitePersonneNonPaye(id)+"-"+LocalDate.now()+".pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }
}
