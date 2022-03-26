package com.mola.cargo.controller;

import com.mola.cargo.model.SortieAerien;
import com.mola.cargo.model.SortieMaritime;
import com.mola.cargo.service.ColisAerienService;
import com.mola.cargo.service.ConvoiService;
import com.mola.cargo.service.SortieAerienService;
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
    private ColisAerienService colisAerienService;
    @Autowired
    private ConvoiService convoiService;

    @GetMapping("/sortieAeriens")
    public String afficherSortie(Model model){
        model.addAttribute("sortieAeriens", sortieAerienService.showSortieAeriens());
        model.addAttribute("colisAeriens", colisAerienService.showColisAerienDepot(Constante.INITIAL));
        model.addAttribute("unConvoi", convoiService.showMaLastConvoiAerien());
        return "sortie/sortieAerien";
    }

    @GetMapping("sortieAerien/colis/{id}")
    public String afficherSortieAdmin(@PathVariable("id") Long id, Model model){
        model.addAttribute("sortieAeriens", sortieAerienService.showSortieColisConvois(id));
        model.addAttribute("unConvoi", convoiService.showOneConvoi(id));
        return "sortie/sortieAerienAdmin";
    }

    @PostMapping("/sortieAerien/nouveau")
    public String enreggistrerSortieAerien(@RequestParam Long identifiant, @RequestParam Long colisAerienid){
        SortieAerien sortieAerien = new SortieAerien();
        sortieAerien.setColisAerienid(colisAerienid);
        sortieAerien.setConvoiid(identifiant);
        sortieAerienService.saveSortieAerien(sortieAerien);
        colisAerienService.updateStatutColisAerien(Constante.SORTIE, colisAerienid);
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
        sortieAerienService.deleteSortieAerien(id);
        colisAerienService.updateStatutColisAerien(Constante.INITIAL, id);
        return "redirect:/sortieAeriens";
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

   /* @GetMapping("sortieAerien/colis/{id}")
    public String afficherColisAerienSortie(@PathVariable("id") Long id, Model model){
        model.addAttribute("sortieAeriens", sortieAerienService.showOneSortieAerien(id));

        return "sortie/colisSortieAerien";
    }*/
}
