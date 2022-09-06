package com.mola.cargo.controller;

import com.mola.cargo.model.*;
import com.mola.cargo.service.*;
import com.mola.cargo.util.Constante;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
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
    @Autowired
    private ResourceLoader resourceLoader;

    private double getTotalApayer(){
        return  colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()),
                commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getReduction())
                + produitAerienService.showMaxTaxeAerienne(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())
                +colisAerienService.prixTransportColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
    }

    @GetMapping("/colisAerien/produits")
    public String afficherProduitAerien(Model model){
        //System.out.println("Frais = "+produitAerienService.fraisEmballage());
        model.addAttribute("produitsAerien", produitAerienService.findProduitColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        model.addAttribute("tarifs", tarifService.showTarifs());
        model.addAttribute("lastColisAerien", colisAerienService.showMaLastColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        model.addAttribute("reprise", "NON");
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
                                     @RequestParam String reprise,
                                     @RequestParam String num,
                                     @RequestParam String pin,
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
        //commandeService.updateEtatCommande(Constante.STATUT_PRODUIT_CREE, commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        produitAerienService.saveProduitAerien(produitAerien);
        if(reprise.equals("OUI")){
            commandeService.updateEtatCommande(Constante.STATUT_PRODUIT_CREE, commandeService.showCommandePin(pin).getId());
            return "redirect:/commande/colisbis/?pin="+pin+"&num="+num;
        }else {
            commandeService.updateEtatCommande(Constante.STATUT_PRODUIT_CREE, commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
            return "redirect:/colisAerien/produits";
        }
    }

    @GetMapping("/produitAerien/formUpdate/{id}")
    public String showFormUpdateColisAerien(@PathVariable("id") Long id, Model model){
        model.addAttribute("unProduit",produitAerienService.showOneProduitAerien(id));
        model.addAttribute("tarifs", tarifService.showTarifs());
        return "produit/formUpdateProduitAerien";
    }

    @GetMapping("/produitAerien/formUpdateReprise/{id}")
    public String showFormUpdateColisAerienReprise(@PathVariable("id") Long id, Model model){
        model.addAttribute("unProduit",produitAerienService.showOneProduitAerien(id));
        model.addAttribute("tarifs", tarifService.showTarifs());
        return "produit/formUpdateProduitAerienReprise";
    }

    @PostMapping("/produitAerien/update")
    public String updateProduitAerien(@ModelAttribute("produitAerien") ProduitAerien produitAerien){
        produitAerienService.saveProduitAerien(produitAerien);
        return "redirect:/colisAerien/produits";
    }

    @PostMapping("/produitAerien/updateReprise")
    public String updateProduitAerienReprise(@ModelAttribute("produitAerien") ProduitAerien produitAerien){
        ColisAerien colisAerien = (ColisAerien) colisAerienService.showOneColisAerien(produitAerien.getColisAerienid());
        produitAerienService.saveProduitAerien(produitAerien);
        return "redirect:/commande/colisbis/?pin="+colisAerien.getCommande().getPin()+"&num="+colisAerien.getId();
    }

    @GetMapping("/produitAerien/delete")
    public String supprimerProduitAerien(Long id){
        produitAerienService.deleteProduitAerien(id);
        return "redirect:/colisAerien/produits";
    }

    @GetMapping("/produitAerien/deleteReprise")
    public String supprimerProduitAerienReprise(Long id){
        ProduitAerien produitAerien = produitAerienService.showOneProduitAerien(id);
        ColisAerien colisAerien = (ColisAerien) colisAerienService.showOneColisAerien(produitAerien.getColisAerienid());
        produitAerienService.deleteProduitAerien(id);
        return "redirect:/commande/colisbis/?pin="+colisAerien.getCommande().getPin()+"&num="+colisAerien.getId();
    }

    @GetMapping("/envoi/detail")
    public String afficherDetailCommande(Model model){
        model.addAttribute("lastCommande", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()));
        model.addAttribute("nbColis", colisAerienService.nbreColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        model.addAttribute("transport", String.format("% ,.2f", colisAerienService.prixTransportColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())));
        model.addAttribute("taxe", String.format("% ,.2f", produitAerienService.showMaxTaxeAerienne(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())));
        model.addAttribute("prixColis", String.format("% ,.2f", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()),
                commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getReduction())));
        model.addAttribute("prixTotal", String.format("% ,.2f", getTotalApayer()));
        model.addAttribute("prixTotal2", getTotalApayer());
        return "commande/detailEnvoiAerien";
    }

    @GetMapping("/envoi/detailReprise")
    public String afficherDetailCommandeReprise(String pin, Model model){
        Commande commande = commandeService.showCommandePin(pin);
        double total = colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(commande.getId()),commande.getReduction())
                + produitAerienService.showMaxTaxeAerienne(commande.getId())
                +colisAerienService.prixTransportColisAerien(commande.getId());

        model.addAttribute("lastCommande", commande);
        model.addAttribute("nbColis", colisAerienService.nbreColisAerien(commande.getId()));
        model.addAttribute("transport", String.format("% ,.2f", colisAerienService.prixTransportColisAerien(commande.getId())));
        model.addAttribute("taxe", String.format("% ,.2f", produitAerienService.showMaxTaxeAerienne(commande.getId())));
        model.addAttribute("prixColis", String.format("% ,.2f", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(commande.getId()), commande.getReduction())));
        model.addAttribute("prixTotal", String.format("% ,.2f", total));
        model.addAttribute("prixTotal2", total);
        return "commande/detailEnvoiAerienReprise";
    }
    @PostMapping("/envoi/fin")
    public String finaliserCommande(@RequestParam String paye){
        commandeService.updatePaiementCommande(getTotalApayer(), Double.parseDouble(paye), commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        commandeService.updateEtatCommande(Constante.STATUT_COMMANDE_ACHEVE, commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        recepteurService.updateSoldeClient(getTotalApayer()-Double.parseDouble(paye), commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getRecepteur().getId());
        if(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getLieuPaiement().equals(Constante.LIEU_TOGO)){
            return "redirect:/colisAerien/facture";
        }else{
            return "redirect:/colisAerien/facture_non_paye";
        }
    }

    @PostMapping("/envoi/finReprise")
    public String finaliserCommandeReprise(@RequestParam String paye, @RequestParam("pin") String pin){
        Commande commande = commandeService.showCommandePin(pin);
        double total = colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(commande.getId()),commande.getReduction())
                + produitAerienService.showMaxTaxeAerienne(commande.getId())
                +colisAerienService.prixTransportColisAerien(commande.getId());
        commandeService.updatePaiementCommande(total, Double.parseDouble(paye), commande.getId());
        commandeService.updateEtatCommande(Constante.STATUT_COMMANDE_ACHEVE, commande.getId());
        recepteurService.updateSoldeClient(total-Double.parseDouble(paye), commande.getRecepteur().getId());
        if(commande.getLieuPaiement().equals(Constante.LIEU_TOGO)){
            return "redirect:/colisAerien/factureReprise/?id="+commande.getId();
        }else{
            return "redirect:/colisAerien/facture_non_payeReprise/?id="+commande.getId();
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
    public ResponseEntity<byte[]> factureAerienne() throws IOException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        /*En local
        File file = ResourceUtils.getFile("classpath:factureAeriennePaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        */
        //En déploiement
        Resource resource = resourceLoader.getResource("classpath:factureAeriennePaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("chemin_logo", "head.png");
        parameter.put("tampon_paye", "tampon_paye.PNG");
        parameter.put("user", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getUser().getPrenom());
        parameter.put("nbre_colis", colisAerienService.nbreColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        parameter.put("taxe", produitAerienService.showMaxTaxeAerienne(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        parameter.put("poids", colisAerienService.poidsTotalColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        parameter.put("montantTotal", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()),
                commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getReduction()));
        parameter.put("transportTotal", colisAerienService.prixTransportColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+commandeService.getIdentitePersonnePaye(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())+"-"+LocalDate.now()+".pdf");
        /*Création d'un inventaire
        Inventaire inventaire = new Inventaire();
        double prixTotal = getTotalApayer();
        inventaire.setCommandeid(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        inventaire.setStatus(Constante.INVENTAIRE_NON_ENCAISSE);
        inventaire.setNombreColis(colisAerienService.nbreColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        inventaire.setPrixTotal(prixTotal);
        inventaire.setCommercial(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getUser().getPrenom());
        if(!inventaireService.testerAppartenance(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())){
            inventaireService.saveInventaire(inventaire);
        }*/
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture aerienne non payée
    @GetMapping("/colisAerien/facture_non_paye")
    public ResponseEntity<byte[]> factureAerienneNonPaye() throws IOException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        Resource resource = resourceLoader.getResource("classpath:factureAerienneNonPaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("chemin_logo", "head.png");
        parameter.put("tampon_non_paye", "tamponAll.PNG");
        parameter.put("logo_paypal", "paypal.png");
        parameter.put("user", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getUser().getPrenom());
        parameter.put("nbre_colis", colisAerienService.nbreColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        parameter.put("taxe", produitAerienService.showMaxTaxeAerienne(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        parameter.put("poids", colisAerienService.poidsTotalColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        parameter.put("montantTotal", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()),
                                    commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getReduction()));
        parameter.put("transportTotal", colisAerienService.prixTransportColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+commandeService.getIdentitePersonneNonPaye(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())+"-"+LocalDate.now()+".pdf");
        /*Création d'un inventaire
        Inventaire inventaire = new Inventaire();
        double prixTotal = getTotalApayer();
        inventaire.setCommandeid(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        inventaire.setStatus(Constante.INVENTAIRE_NON_ENCAISSE);
        inventaire.setNombreColis(colisAerienService.nbreColisAerien(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        inventaire.setPrixTotal(prixTotal);
        inventaire.setCommercial(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getUser().getPrenom());
        if(!inventaireService.testerAppartenance(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId())){
            inventaireService.saveInventaire(inventaire);
        }*/
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    @GetMapping("/colisAerien/factureReprise")
    public ResponseEntity<byte[]> factureAerienne(Long id) throws IOException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(id);
        Resource resource = resourceLoader.getResource("classpath:factureAeriennePaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("chemin_logo", "head.png");
        parameter.put("tampon_paye", "tampon_paye.PNG");
        parameter.put("user", commandeService.showOnecommande(id).getUser().getPrenom());
        parameter.put("nbre_colis", colisAerienService.nbreColisAerien(id));
        parameter.put("taxe", produitAerienService.showMaxTaxeAerienne(id));
        parameter.put("poids", colisAerienService.poidsTotalColisAerien(id));
        parameter.put("montantTotal", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(id), commandeService.showOnecommande(id).getReduction()));
        parameter.put("transportTotal", colisAerienService.prixTransportColisAerien(id));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+commandeService.getIdentitePersonnePaye(id)+"-"+ LocalDate.now()+".pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture aerienne non payée
    @GetMapping("/colisAerien/facture_non_payeReprise")
    public ResponseEntity<byte[]> factureAerienneNonPaye(Long id) throws IOException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(id);
        Resource resource = resourceLoader.getResource("classpath:factureAerienneNonPaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("chemin_logo", "head.png");
        parameter.put("tampon_non_paye", "tamponAll.PNG");
        parameter.put("logo_paypal", "paypal.png");
        parameter.put("user", commandeService.showOnecommande(id).getUser().getPrenom());
        parameter.put("nbre_colis", colisAerienService.nbreColisAerien(id));
        parameter.put("taxe", produitAerienService.showMaxTaxeAerienne(id));
        parameter.put("poids", colisAerienService.poidsTotalColisAerien(id));
        parameter.put("montantTotal", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(id), commandeService.showOnecommande(id).getReduction()));
        parameter.put("transportTotal", colisAerienService.prixTransportColisAerien(id));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+commandeService.getIdentitePersonneNonPaye(id)+"-"+ LocalDate.now()+".pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }


}
