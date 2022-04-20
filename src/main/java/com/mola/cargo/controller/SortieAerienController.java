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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SortieAerienController {

    @Autowired
    private SortieAerienService sortieAerienService;
    @Autowired
    private SortieMaritimeService sortieMaritimeService;
    @Autowired
    private ColisAerienService colisAerienService;
    @Autowired
    private ConvoiService convoiService;
    @Autowired
    private ProduitAerienService produitAerienService;
    @Autowired
    private ReductionService tarifAerienService;
    @Autowired
    private CommandeService commandeService;

    @GetMapping("/sortieAeriens")
    public String afficherSortie(Model model){
        //model.addAttribute("sortieAeriens", sortieAerienService.showSortieAeriens());
        model.addAttribute("colisAeriens", colisAerienService.showColisAerienDepot(Constante.INITIAL));
        model.addAttribute("unConvoi", convoiService.showMaLastConvoiAerien());
        model.addAttribute("sortieAeriens", sortieAerienService.showSortieColisConvois(convoiService.showMaLastConvoiAerien().getId()));
        return "sortie/sortieAerien";
    }

    @GetMapping("sortieAerien/colis/{id}")
    public String afficherSortieAdmin(@PathVariable("id") Long id, Model model){
        model.addAttribute("sortieAeriens", sortieAerienService.showSortieColisConvois(id));
        model.addAttribute("unConvoi", convoiService.showOneConvoi(id));
        return "sortie/sortieAerienAdmin";
    }

    @GetMapping("/sortieAerien/nouveau")
    public String enreggistrerSortieAerien(){
        //List<SortieAerien> listeSortieAerien = new ArrayList<>();
        for(ColisAerien ca : colisAerienService.showColisAerienDepot(Constante.INITIAL)){
            SortieAerien sortieAerien = new SortieAerien();
            sortieAerien.setColisAerienid(ca.getId());
            sortieAerien.setConvoiid(convoiService.showMaLastConvoiAerien().getId());
            sortieAerienService.saveSortieAerien(sortieAerien);
            colisAerienService.updateStatutColisAerien(Constante.SORTIE, ca.getId());
        }
        return "redirect:/sortieAeriens";
    }

    @GetMapping("sortieAerien/formUpdate/{id}")
    public String afficherFormUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("UneSortieAerien", sortieAerienService.showOneSortieAerien(id));
        model.addAttribute("colisAeriens", colisAerienService.showColisAerien());
        return "sortie/formUpdateSortieAerien";
    }

    @PostMapping("/sortieAerien/update")
    public String updateSortieAerien(@ModelAttribute("SortieAerien") SortieAerien sortieAerien){
        sortieAerienService.saveSortieAerien(sortieAerien);
        return "redirect:/sortieAeriens";
    }

    @GetMapping("sortieAerien/delete")
    public String supprimer(Long id){
        colisAerienService.updateStatutColisAerien(Constante.INITIAL, sortieAerienService.showOneSortieAerien(id).getColisAerienid());
        sortieAerienService.deleteSortieAerien(id);
        return "redirect:/sortieAeriens";
    }

    @GetMapping("/convoiAerien/{id}")
    public String afficherColisParCommande(@PathVariable("id") Long id, Model model){
        if(convoiService.showOneConvoi(id).getIdentifiant().contains(Constante.PREFIX_AERIEN)){
            model.addAttribute("listeCommande", sortieAerienService.listeCommandeAerien(id));
            model.addAttribute("unConvoi", convoiService.showOneConvoi(id));
            return "sortie/commandeAerienConvoi";
        }else{
            model.addAttribute("listeCommande", sortieMaritimeService.listeCommandeMaritime(id));
            model.addAttribute("unConvoi", convoiService.showOneConvoi(id));
            return "sortie/commandeMaritimeConvoi";
        }

    }

    //Fonction pour générer la facture aerienne payée au Togo
    @GetMapping("/sortieAerien/paramfactureP/{id}")
    public ResponseEntity<byte[]> factureAerienne(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(id);
        File file = ResourceUtils.getFile("classpath:factureAeriennePaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", produitAerienService.getPrincipal());
        parameter.put("nbre_colis", colisAerienService.nbreColisAerien(id));
        parameter.put("taxe", produitAerienService.showMaxTaxeAerienne(id));
        parameter.put("poids", colisAerienService.poidsTotalColisAerien(id));
        parameter.put("montantTotal", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(id), commandeService.showOnecommande(id).getReduction()));
        parameter.put("transportTotal", colisAerienService.prixTransportColisAerien(id));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour générer la facture aerienne non payée
    @GetMapping("/sortieAerien/paramfactureNP/{id}")
    public ResponseEntity<byte[]> factureAerienneNonPaye(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(id);
        File file = ResourceUtils.getFile("classpath:factureAerienneNonPaye.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("user", produitAerienService.getPrincipal());
        parameter.put("nbre_colis", colisAerienService.nbreColisAerien(id));
        parameter.put("taxe", produitAerienService.showMaxTaxeAerienne(id));
        parameter.put("poids", colisAerienService.poidsTotalColisAerien(id));
        parameter.put("montantTotal", colisAerienService.appliquerReduction(colisAerienService.prixTotalColisAerien(id), commandeService.showOnecommande(id).getReduction()));
        parameter.put("transportTotal", colisAerienService.prixTransportColisAerien(id));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Fonction pour imprimer la liste des colis maritime envoyés
    @GetMapping("/sortieAerien/facture/{id}")
    public ResponseEntity<byte[]> factureMaritime(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<SortieAerien> listeSortieAerien = sortieAerienService.showSortieColisConvois(id);
        File file = ResourceUtils.getFile("classpath:listeArrivageAerien.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeSortieAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("nbre", listeSortieAerien.size());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Facture douanière pour les valeurs marchandes des produits

    //Fonction pour générer la facture aerienne douane
    @GetMapping("/sortieAerien/douane/{id}")
    public ResponseEntity<byte[]> factureAerienneVM(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
        List<ProduitAerien> listeProdAerien = produitAerienService.findProduitColisAerien(id);
        File file = ResourceUtils.getFile("classpath:factureVMA.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeProdAerien);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        parameter.put("nbre_colis", colisAerienService.nbreColisAerien(id));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

}
