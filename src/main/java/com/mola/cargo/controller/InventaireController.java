package com.mola.cargo.controller;

import com.mola.cargo.model.Annulation;
import com.mola.cargo.model.ProduitAerien;
import com.mola.cargo.model.ProduitMaritime;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/stat")
public class InventaireController {

    @Autowired
    private InventaireService inventaireService;
    @Autowired
    private ProduitMaritimeService produitMaritimeService;
    @Autowired
    private ProduitAerienService produitAerienService;
    @Autowired
    private ColisMaritimeService colisMaritimeService;
    @Autowired
    private ColisAerienService colisAerienService;
    @Autowired
    private CommandeService commandeService;
    @Autowired
    private AnnulationService annulationService;
    @Autowired
    private RecepteurService recepteurService;
    @Autowired
    private ResourceLoader resourceLoader;

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/inventaires")
    public String afficherPaiement(Model model){
        double prixTotal = commandeService.sommeFactureNonEncaisseTogo(Constante.INVENTAIRE_NON_ENCAISSE,
                Constante.LIEU_TOGO);
        model.addAttribute("inventaires", commandeService.commandeByStatusAndByLieu(Constante.INVENTAIRE_NON_ENCAISSE,
                Constante.LIEU_TOGO));
        model.addAttribute("totalSomme", String.format("% ,.2f",prixTotal));
        return "sortie/inventaire";
    }

    @GetMapping("/listeFactures/all")
    public String afficherFacrureTogo(Model model){
        //model.addAttribute("inventaires", inventaireService.showInventaires());
        model.addAttribute("inventaires", commandeService.showCommande());
        return "sortie/factures";
    }

    /*@GetMapping("/listeFactures/all")
    public String afficherFacrureAll(Model model){
        model.addAttribute("inventaires", inventaireService.findByTypeEnvoi(Constante.ENVOI_MARITIME));
        return "sortie/listeFactureMaritime";
    }*/

    @GetMapping("/inventaires/encaisser")
    public String afficherPaiementEncaisser(Model model){
        //model.addAttribute("inventaires", inventaireService.showInventaireParStatut(Constante.INVENTAIRE_ENCAISSE));
        model.addAttribute("commandes", commandeService.commandeSelonStatut(Constante.INVENTAIRE_ENCAISSE));
        return "sortie/encaiser";
    }

    @GetMapping("/inventaires/non_paye")
    public String afficherFactureNonPayer(Model model){
        Double prixTotal = commandeService.sommeFactureAllemagneNonEncaisse(Constante.INVENTAIRE_NON_ENCAISSE,
                Constante.LIEU_ALL);
        model.addAttribute("inventaires", commandeService.commandeByStatusAndByLieu(Constante.INVENTAIRE_NON_ENCAISSE,
                Constante.LIEU_ALL));
        model.addAttribute("totalSomme",String.format("% ,.2f",prixTotal));
        return "sortie/paiementAllemagne";
    }

    @GetMapping("inventaire/valider/{id}")
    public String updateInventaireEncaissement(@PathVariable("id") Long id){
       commandeService.updateStatutCommandeEncaisse(Constante.INVENTAIRE_ENCAISSE, id);
       return "redirect:/stat/inventaires";
    }

    @GetMapping("/annuler/encaissement/{id}")
    public String annulerEncaissement(@PathVariable("id") Long id, Model model){
        model.addAttribute("unEncaissement", commandeService.showOnecommande(id));
        return "sortie/formAnnuler";
    }
    @PostMapping("/encaissement/annuler")
    public String validerAnnulation(Annulation annulation){
        annulationService.saveAnnulation(annulation);
        commandeService.updateStatutCommandeEncaisse(Constante.INVENTAIRE_ATTENTE, annulation.getCommande_id());
        return "redirect:/stat/inventaires";
    }

    @GetMapping("/inventaires/annulation")
    public String afficherListeAnnulation(Model model){
        model.addAttribute("annulationAttente", commandeService.commandeSelonStatut(Constante.INVENTAIRE_ATTENTE));
        return "sortie/listeAnnulation";
    }

    @GetMapping("/validation/annulation/{id}")
    public String updateEncaissementAttente(@PathVariable("id") Long id){
        inventaireService.updateStatutInventaire(Constante.INVENTAIRE_NON_ENCAISSE, id);
        return "redirect:/stat/inventaires/annulation";
    }
    @GetMapping("inventaireAerienLien/facture/{id}")
    public String afficherDiverseFacture(@PathVariable("id") Long id){
        if(commandeService.showOnecommande(id).getLieuPaiement().equals("Togo")){
            return "redirect:/stat/inventaireAerien/facture/"+id;
        }else {
            return "redirect:/stat/inventaireAerienNP/facture/"+id;
        }
    }

    @GetMapping("inventaireMaritimeLien/facture/{id}")
    public String afficherDiverseFactureM(@PathVariable("id") Long id){
        if(commandeService.showOnecommande(id).getLieuPaiement().equals("Togo")){
            return "redirect:/stat/inventaireMaritime/facture/"+id;
        }else {
            return "redirect:/stat/inventaireMaritimeNP/facture/"+id;
        }
    }

    //Fonction pour générer la facture maritime payé
    @GetMapping("/inventaireMaritime/facture/{id}")
    public ResponseEntity<byte[]> factureMaritime(@PathVariable("id") Long id) throws IOException, JRException {
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
        parameter.put("nbre_colis", commandeService.showOnecommande(id).getNbColis());
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
            parameter.put("montant_grand_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(id, "GC"), commandeService.showOnecommande(id).getReduction()));
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
    @GetMapping("/inventaireMaritimeNP/facture/{id}")
    public ResponseEntity<byte[]> factureMaritimeNonPaye(@PathVariable("id") Long id) throws IOException, JRException {
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
        parameter.put("nbre_colis", commandeService.showOnecommande(id).getNbColis());
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
            parameter.put("montant_grand_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(id, "GC"), commandeService.showOnecommande(id).getReduction()));
        }else {
            parameter.put("montant_grand_carton", 0.0);
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+commandeService.getIdentitePersonneNonPaye(id)+"-"+ LocalDate.now()+".pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture aerienne payée au Togo
    @GetMapping("/inventaireAerien/facture/{id}")
    public ResponseEntity<byte[]> factureAerienne(@PathVariable("id") Long id) throws IOException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(id);
        Resource resource = resourceLoader.getResource("classpath:factureAeriennePaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("chemin_logo", "head.png");
        parameter.put("tampon_paye", "tampon_paye.PNG");
        parameter.put("user", commandeService.showOnecommande(id).getUser().getPrenom());
        parameter.put("nbre_colis", commandeService.showOnecommande(id).getNbColis());
        parameter.put("taxe", produitAerienService.showMaxTaxeAerienne(id));
        parameter.put("poids", colisAerienService.poidsTotalColisAerien(id));
        parameter.put("montantTotal", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(id), commandeService.showOnecommande(id).getReduction()));
        parameter.put("transportTotal", colisAerienService.prixTransportColisAerien(id));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+commandeService.getIdentitePersonnePaye(id)+"-"+LocalDate.now()+".pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture aerienne non payée
    @GetMapping("/inventaireAerienNP/facture/{id}")
    public ResponseEntity<byte[]> factureAerienneNonPaye(@PathVariable("id") Long id) throws IOException, JRException {
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
        parameter.put("nbre_colis", commandeService.showOnecommande(id).getNbColis());
        parameter.put("taxe", produitAerienService.showMaxTaxeAerienne(id));
        parameter.put("poids", colisAerienService.poidsTotalColisAerien(id));
        parameter.put("montantTotal", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(id),commandeService.showOnecommande(id).getReduction()));
        parameter.put("transportTotal", colisAerienService.prixTransportColisAerien(id));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+commandeService.getIdentitePersonneNonPaye(id)+"-"+LocalDate.now()+".pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Facture douanière pour les valeurs marchandes des produits

    //Fonction pour générer la facture aerienne payée au Togo
    @GetMapping("/douaneAerien/facture/{id}")
    public ResponseEntity<byte[]> factureAerienneVM(@PathVariable("id") Long id) throws IOException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(id);
        Resource resource = resourceLoader.getResource("classpath:factureVMA.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", commandeService.showOnecommande(id).getUser().getPrenom());
        parameter.put("nbre_colis", commandeService.showOnecommande(id).getNbColis());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture maritime payé
    @GetMapping("/douaneMaritime/facture/{id}")
    public ResponseEntity<byte[]> factureMaritimeVM(@PathVariable("id") Long id) throws IOException, JRException {
        List<ProduitMaritime> listeProdMaritime = produitMaritimeService.findProduitColisMaritime(id);
        Resource resource = resourceLoader.getResource("classpath:factureVMM.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdMaritime);
        int petit = colisMaritimeService.nbreSelonCarton(id, "PC");
        int grand = colisMaritimeService.nbreSelonCarton(id, "GC");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", commandeService.showOnecommande(id).getUser().getPrenom());
        parameter.put("nbre_colis", commandeService.showOnecommande(id).getNbColis());
        parameter.put("nb_petit_carton", petit);
        parameter.put("nb_grand_carton", grand);
        if(petit!=0){
            parameter.put("montant_petit_carton", colisMaritimeService.montantPrixCarton(id, "PC"));
        }else{
            parameter.put("montant_petit_carton", 0.0);
        }
        if(grand!=0){
            parameter.put("montant_grand_carton", (double)colisMaritimeService.montantPrixCarton(id, "GC"));
        }else {
            parameter.put("montant_grand_carton", 0.0);
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

}
