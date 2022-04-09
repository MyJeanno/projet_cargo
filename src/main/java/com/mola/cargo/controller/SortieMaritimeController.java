package com.mola.cargo.controller;

import com.mola.cargo.model.*;
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
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SortieMaritimeController {

    @Autowired
    private SortieMaritimeService sortieMaritimeService;
    @Autowired
    private ColisMaritimeService colisMaritimeService;
    @Autowired
    private ConvoiService convoiService;
    @Autowired
    private CommandeService commandeService;
    @Autowired
    private ProduitMaritimeService produitMaritimeService;

    @GetMapping("/sortieMaritimes")
    public String afficherSortie(Model model){
        //model.addAttribute("sortieMaritimes", sortieMaritimeService.showSortieMaritimes());
        model.addAttribute("colisMaritimes", colisMaritimeService.showColisMaritimeDepot(Constante.INITIAL));
        model.addAttribute("unConvoi", convoiService.showMaLastConvoiMaritime());
        model.addAttribute("sortieMaritimes", sortieMaritimeService.showSortieColisMaritimeConvois(convoiService.showMaLastConvoiMaritime().getId()));
        return "sortie/sortieMaritime";
    }

    @GetMapping("sortieMaritimes/colis/{id}")
    public String afficherSortieAdmin(@PathVariable("id") Long id, Model model){
        model.addAttribute("sortieMaritimes", sortieMaritimeService.showSortieColisMaritimeConvois(id));
        model.addAttribute("unConvoi", convoiService.showOneConvoi(id));
        return "sortie/sortieMaritimeAdmin";
    }

    @GetMapping("/sortieMaritime/nouveau")
    public String enregistrerSortieMaritime(){
        for (ColisMaritime cm:colisMaritimeService.showColisMaritimeDepot(Constante.INITIAL)){
            SortieMaritime sortieMaritime = new SortieMaritime();
            sortieMaritime.setColisMaritimeid(cm.getId());
            sortieMaritime.setConvoiid(convoiService.showMaLastConvoiMaritime().getId());
            sortieMaritimeService.saveSortieMaritime(sortieMaritime);
            colisMaritimeService.updateStatutColisMaritime(Constante.SORTIE, cm.getId());
        }
        return "redirect:/sortieMaritimes";
    }

    @GetMapping("sortieMaritime/formUpdate/{id}")
    public String afficherFormUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("UneSortieMaritime", sortieMaritimeService.showOneSortieMaritime(id));
        model.addAttribute("colisMaritimes", colisMaritimeService.showColisMaritime());
        return "sortie/formUpdateSortieMaritime";
    }

    @PostMapping("/sortieMaritime/update")
    public String updateSortieMaritime(@ModelAttribute("SortieMaritime") SortieMaritime sortieMaritime){
        sortieMaritimeService.saveSortieMaritime(sortieMaritime);
        return "redirect:/sortieMaritimes";
    }

    @GetMapping("sortieMaritime/delete")
    public String supprimer(Long id){
        colisMaritimeService.updateStatutColisMaritime(Constante.INITIAL, sortieMaritimeService.showOneSortieMaritime(id).getColisMaritimeid());
        sortieMaritimeService.deleteSortieMaritime(id);
        return "redirect:/sortieMaritimes";
    }

    @GetMapping("/sortieMaritime/paramfacture/{id}")
    public String choisirFacture(@PathVariable("id") Long id){
        if(commandeService.showOnecommande(id).getLieuPaiement().equals(Constante.LIEU_TOGO)){
            return "redirect:/inventaireMaritime/facture/{id}";
        }
        return "redirect:/inventaireMaritimeNP/facture/{id}";
    }

    //Fonction pour générer la facture maritime payé
    @GetMapping("/sortieMaritime/paramfactureP/{id}")
    public ResponseEntity<byte[]> factureMaritime(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<ProduitMaritime> listeProdMaritime = produitMaritimeService.findProduitColisMaritime(id);
        File file = ResourceUtils.getFile("classpath:factureMaritimePaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdMaritime);
        int petit = colisMaritimeService.nbreSelonCarton(id, "PC");
        int grand = colisMaritimeService.nbreSelonCarton(id, "GC");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", produitMaritimeService.getPrincipal());
        parameter.put("nbre_colis", colisMaritimeService.nbreColisMaritime(id));
        parameter.put("taxe_maritime", produitMaritimeService.taxe(listeProdMaritime));
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

    //Fonction pour générer la facture maritime non payé
    @GetMapping("/sortieMaritime/paramfactureNP/{id}")
    public ResponseEntity<byte[]> factureMaritimeNonPaye(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<ProduitMaritime> listeProdMaritime = produitMaritimeService.findProduitColisMaritime(id);
        File file = ResourceUtils.getFile("classpath:factureMaritimeNonPaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdMaritime);
        int petit = colisMaritimeService.nbreSelonCarton(id, "PC");
        int grand = colisMaritimeService.nbreSelonCarton(id, "GC");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", produitMaritimeService.getPrincipal());
        parameter.put("nbre_colis", colisMaritimeService.nbreColisMaritime(id));
        parameter.put("taxe_maritime", produitMaritimeService.taxe(listeProdMaritime));
        parameter.put("nb_petit_carton", petit);
        parameter.put("nb_grand_carton", grand);
        if(petit!=0){
            parameter.put("montant_petit_carton", colisMaritimeService.montantPrixCarton(id, "PC"));
        }else{
            parameter.put("montant_petit_carton", 0.0);
        }
        if(grand!=0){
            parameter.put("montant_grand_carton", colisMaritimeService.montantPrixCarton(id, "GC"));
        }else {
            parameter.put("montant_grand_carton", 0.0);
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour imprimer la liste des colis maritime envoyés
    @GetMapping("/sortieMaritime/facture/{id}")
    public ResponseEntity<byte[]> factureMaritimeDouane(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<SortieMaritime> listeSortieMaritime = sortieMaritimeService.showSortieColisMaritimeConvois(id);
        File file = ResourceUtils.getFile("classpath:listeArrivageMaritime.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeSortieMaritime);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("nbre", listeSortieMaritime.size());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture maritime payé
    @GetMapping("/sortieMaritime/douane/{id}")
    public ResponseEntity<byte[]> factureMaritimeVM(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<ProduitMaritime> listeProdMaritime = produitMaritimeService.findProduitColisMaritime(id);
        File file = ResourceUtils.getFile("classpath:factureVMM.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdMaritime);
        int petit = colisMaritimeService.nbreSelonCarton(id, "PC");
        int grand = colisMaritimeService.nbreSelonCarton(id, "GC");
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("nbre_colis", colisMaritimeService.nbreColisMaritime(id));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }


}
