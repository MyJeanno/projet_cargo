package com.mola.cargo.controller;

import com.mola.cargo.model.ProduitAerien;
import com.mola.cargo.model.ProduitMaritime;
import com.mola.cargo.service.*;
import com.mola.cargo.util.Constante;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileNotFoundException;
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

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/inventaires")
    public String afficherPaiement(Model model){
        double prixTotal = inventaireService.sommeFactureNonEncaisse(Constante.INVENTAIRE_NON_ENCAISSE,
                Constante.LIEU_TOGO);
        model.addAttribute("inventaires", inventaireService.showInventaireSelonStatut(Constante.INVENTAIRE_NON_ENCAISSE,
                Constante.LIEU_TOGO));
        model.addAttribute("totalSomme", String.format("% ,.2f",prixTotal));
        return "sortie/inventaire";
    }

    @GetMapping("/listeFactures/all")
    public String afficherFacrureTogo(Model model){
        model.addAttribute("inventaires", inventaireService.showInventaires());
        return "sortie/factures";
    }

    /*@GetMapping("/listeFactures/all")
    public String afficherFacrureAll(Model model){
        model.addAttribute("inventaires", inventaireService.findByTypeEnvoi(Constante.ENVOI_MARITIME));
        return "sortie/listeFactureMaritime";
    }*/

    @GetMapping("/inventaires/encaisser")
    public String afficherPaiementEncaisser(Model model){
        model.addAttribute("inventaires", inventaireService.showInventaireParStatut(Constante.INVENTAIRE_ENCAISSE));
        return "sortie/encaiser";
    }

    @GetMapping("/inventaires/non_paye")
    public String afficherFactureNonPayer(Model model){
        Double prixTotal = inventaireService.sommeFactureNonEncaisse(Constante.INVENTAIRE_NON_ENCAISSE,
                Constante.LIEU_ALL);
        model.addAttribute("inventaires", inventaireService.showInventaireSelonStatut(Constante.INVENTAIRE_NON_ENCAISSE,
                Constante.LIEU_ALL));
        model.addAttribute("totalSomme",String.format("% ,.2f",prixTotal));
        return "sortie/paiementAllemagne";
    }

    @GetMapping("inventaire/valider/{id}")
    public String updateInventaireEncaissement(@PathVariable("id") Long id){
       inventaireService.updateStatutInventaire(Constante.INVENTAIRE_ENCAISSE, id);
       return "redirect:/stat/inventaires";
    }

    @GetMapping("/annuler/encaissement/{id}")
    public String annulerEncaissement(@PathVariable("id") Long id){
        inventaireService.updateStatutInventaire(Constante.INVENTAIRE_NON_ENCAISSE, id);
        return "redirect:/stat/inventaires";
    }

    @GetMapping("/facture/non_paye/{id}")
    public String validerFactureNonPayer(@PathVariable("id") Long id){
        inventaireService.updateStatutInventaire(Constante.INVENTAIRE_ENCAISSE, id);
        return "redirect:/stat/inventaires/non_paye";
    }

    //Fonction pour générer la facture maritime payé
    @GetMapping("/inventaireMaritime/facture/{id}")
    public ResponseEntity<byte[]> factureMaritime(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<ProduitMaritime> listeProdMaritime = produitMaritimeService.findProduitColisMaritime(inventaireService.showOneInventaire(id).getCommandeid());
        File file = ResourceUtils.getFile("classpath:factureMaritimePaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdMaritime);
        int petit = colisMaritimeService.nbreSelonCarton(inventaireService.showOneInventaire(id).getCommandeid(), "PC");
        int grand = colisMaritimeService.nbreSelonCarton(inventaireService.showOneInventaire(id).getCommandeid(), "GC");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", produitMaritimeService.getPrincipal());
        parameter.put("nbre_colis", colisMaritimeService.nbreColisMaritime(inventaireService.showOneInventaire(id).getCommandeid()));
        parameter.put("taxe_maritime", produitMaritimeService.MaxTaxeCommandeMaritime(inventaireService.showOneInventaire(id).getCommandeid()));
        parameter.put("nb_petit_carton", petit);
        parameter.put("nb_grand_carton", grand);
        parameter.put("total_transport", colisMaritimeService.montantTotalTransport(inventaireService.showOneInventaire(id).getCommandeid()));
        if(petit!=0){
            parameter.put("montant_petit_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(inventaireService.showOneInventaire(id).getCommandeid(), "PC"), inventaireService.showOneInventaire(id).getCommande().getReduction()));
        }else{
            parameter.put("montant_petit_carton", 0.0);
        }
        if(grand!=0){
            parameter.put("montant_grand_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(inventaireService.showOneInventaire(id).getCommandeid(), "GC"), inventaireService.showOneInventaire(id).getCommande().getReduction()));
        }else {
            parameter.put("montant_grand_carton", 0.0);
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture maritime non payé
    @GetMapping("/inventaireMaritimeNP/facture/{id}")
    public ResponseEntity<byte[]> factureMaritimeNonPaye(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<ProduitMaritime> listeProdMaritime = produitMaritimeService.findProduitColisMaritime(inventaireService.showOneInventaire(id).getCommandeid());
        File file = ResourceUtils.getFile("classpath:factureMaritimeNonPaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdMaritime);
        int petit = colisMaritimeService.nbreSelonCarton(inventaireService.showOneInventaire(id).getCommandeid(), "PC");
        int grand = colisMaritimeService.nbreSelonCarton(inventaireService.showOneInventaire(id).getCommandeid(), "GC");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", produitMaritimeService.getPrincipal());
        parameter.put("nbre_colis", colisMaritimeService.nbreColisMaritime(inventaireService.showOneInventaire(id).getCommandeid()));
        parameter.put("taxe_maritime", produitMaritimeService.MaxTaxeCommandeMaritime(inventaireService.showOneInventaire(id).getId()));
        parameter.put("nb_petit_carton", petit);
        parameter.put("nb_grand_carton", grand);
        parameter.put("total_transport", colisMaritimeService.montantTotalTransport(inventaireService.showOneInventaire(id).getCommandeid()));
        if(petit!=0){
            parameter.put("montant_petit_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(inventaireService.showOneInventaire(id).getCommandeid(), "PC"), inventaireService.showOneInventaire(id).getCommande().getReduction()));
        }else{
            parameter.put("montant_petit_carton", 0.0);
        }
        if(grand!=0){
            parameter.put("montant_grand_carton", colisMaritimeService.appliquerReduction(colisMaritimeService.montantPrixCarton(inventaireService.showOneInventaire(id).getCommandeid(), "GC"), inventaireService.showOneInventaire(id).getCommande().getReduction()));
        }else {
            parameter.put("montant_grand_carton", 0.0);
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture aerienne payée au Togo
    @GetMapping("/inventaireAerien/facture/{id}")
    public ResponseEntity<byte[]> factureAerienne(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(inventaireService.showOneInventaire(id).getCommandeid());
        File file = ResourceUtils.getFile("classpath:factureAeriennePaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", produitMaritimeService.getPrincipal());
        parameter.put("nbre_colis", colisAerienService.nbreColisAerien(inventaireService.showOneInventaire(id).getCommandeid()));
        parameter.put("taxe", produitAerienService.showMaxTaxeAerienne(inventaireService.showOneInventaire(id).getCommandeid()));
        parameter.put("poids", colisAerienService.poidsTotalColisAerien(inventaireService.showOneInventaire(id).getCommandeid()));
        parameter.put("montantTotal", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(inventaireService.showOneInventaire(id).getCommandeid()),inventaireService.showOneInventaire(id).getCommande().getReduction()));
        parameter.put("transportTotal", colisAerienService.prixTransportColisAerien(inventaireService.showOneInventaire(id).getCommandeid()));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture aerienne non payée
    @GetMapping("/inventaireAerienNP/facture/{id}")
    public ResponseEntity<byte[]> factureAerienneNonPaye(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(inventaireService.showOneInventaire(id).getCommandeid());
        File file = ResourceUtils.getFile("classpath:factureAerienneNonPaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", produitMaritimeService.getPrincipal());
        parameter.put("nbre_colis", colisAerienService.nbreColisAerien(inventaireService.showOneInventaire(id).getCommandeid()));
        parameter.put("taxe", produitAerienService.showMaxTaxeAerienne(inventaireService.showOneInventaire(id).getCommandeid()));
        parameter.put("poids", colisAerienService.poidsTotalColisAerien(inventaireService.showOneInventaire(id).getCommandeid()));
        parameter.put("montantTotal", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(inventaireService.showOneInventaire(id).getCommandeid()),inventaireService.showOneInventaire(id).getCommande().getReduction()));
        parameter.put("transportTotal", colisAerienService.prixTransportColisAerien(inventaireService.showOneInventaire(id).getCommandeid()));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Facture douanière pour les valeurs marchandes des produits

    //Fonction pour générer la facture aerienne payée au Togo
    @GetMapping("/douaneAerien/facture/{id}")
    public ResponseEntity<byte[]> factureAerienneVM(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(inventaireService.showOneInventaire(id).getCommandeid());
        File file = ResourceUtils.getFile("classpath:factureVMA.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", produitMaritimeService.getPrincipal());
        parameter.put("nbre_colis", colisAerienService.nbreColisAerien(inventaireService.showOneInventaire(id).getCommandeid()));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture maritime payé
    @GetMapping("/douaneMaritime/facture/{id}")
    public ResponseEntity<byte[]> factureMaritimeVM(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<ProduitMaritime> listeProdMaritime = produitMaritimeService.findProduitColisMaritime(inventaireService.showOneInventaire(id).getCommandeid());
        File file = ResourceUtils.getFile("classpath:factureVMM.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdMaritime);
        int petit = colisMaritimeService.nbreSelonCarton(inventaireService.showOneInventaire(id).getCommandeid(), "PC");
        int grand = colisMaritimeService.nbreSelonCarton(inventaireService.showOneInventaire(id).getCommandeid(), "GC");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", produitMaritimeService.getPrincipal());
        parameter.put("nbre_colis", colisMaritimeService.nbreColisMaritime(inventaireService.showOneInventaire(id).getCommandeid()));
        parameter.put("nb_petit_carton", petit);
        parameter.put("nb_grand_carton", grand);
        if(petit!=0){
            parameter.put("montant_petit_carton", colisMaritimeService.montantPrixCarton(inventaireService.showOneInventaire(id).getCommandeid(), "PC"));
        }else{
            parameter.put("montant_petit_carton", 0.0);
        }
        if(grand!=0){
            parameter.put("montant_grand_carton", (double)colisMaritimeService.montantPrixCarton(inventaireService.showOneInventaire(id).getCommandeid(), "GC"));
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
