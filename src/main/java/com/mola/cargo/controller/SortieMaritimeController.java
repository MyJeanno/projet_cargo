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

    @GetMapping("/sortieMaritimes")
    public String afficherSortie(Model model){
        model.addAttribute("sortieMaritimes", sortieMaritimeService.showSortieMaritimes());
        model.addAttribute("colisMaritimes", colisMaritimeService.showColisMaritimeDepot(Constante.INITIAL));
        model.addAttribute("unConvoi", convoiService.showMaLastConvoiMaritime());
        return "sortie/sortieMaritime";
    }

    @GetMapping("sortieMaritimes/colis/{id}")
    public String afficherSortieAdmin(@PathVariable("id") Long id, Model model){
        model.addAttribute("sortieMaritimes", sortieMaritimeService.showSortieColisMaritimeConvois(id));
        model.addAttribute("unConvoi", convoiService.showOneConvoi(id));
        return "sortie/sortieMaritimeAdmin";
    }

    @PostMapping("/sortieMaritime/nouveau")
    public String enregistrerSortieMaritime(@RequestParam Long colisMaritimeid, @RequestParam Long identifiant){
        SortieMaritime sortieMaritime = new SortieMaritime();
        sortieMaritime.setColisMaritimeid(colisMaritimeid);
        sortieMaritime.setConvoiid(identifiant);
        sortieMaritimeService.saveSortieMaritime(sortieMaritime);
        colisMaritimeService.updateStatutColisMaritime(Constante.SORTIE, colisMaritimeid);
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
        sortieMaritimeService.deleteSortieMaritime(id);
        colisMaritimeService.updateStatutColisMaritime(Constante.INITIAL, id);
        return "redirect:/sortieMaritimes";
    }

    //Fonction pour imprimer la liste des colis maritime envoyés
    @GetMapping("/sortieMaritime/facture/{id}")
    public ResponseEntity<byte[]> factureMaritime(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
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


}
