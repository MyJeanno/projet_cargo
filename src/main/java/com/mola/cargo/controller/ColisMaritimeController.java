package com.mola.cargo.controller;

import com.mola.cargo.model.CargoType;
import com.mola.cargo.model.Colis;
import com.mola.cargo.model.ColisMaritime;
import com.mola.cargo.model.FactureColisPDFExporter;
import com.mola.cargo.service.*;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ColisMaritimeController {

    @Autowired
    private ColisMaritimeService colisMaritimeService;
    @Autowired
    private CargoTypeService cargoTypeService;
    @Autowired
    private CommandeService commandeService;
    @Autowired
    private CartonService cartonService;
    @Autowired
    private PaysService paysService;

    /*
    //Liste des colis par voie maritime
    @GetMapping("/colisMaritime/listes")
    public String afficherListeColis(Model model){
        model.addAttribute("colis", colisMaritimeService.showColisMaritimeCommande(commandeService.showMaLastCommande().getId()));
        model.addAttribute("lastCommande", commandeService.showMaLastCommande());
        return "colis/listeColisMaritime";
    }

    //Renvoie le formulaire de saisie des colis par voie maritime
    @GetMapping("/colis/formColis")
    public String showFormColisMaritime(Model model){
        model.addAttribute("cartons", cartonService.showCarton());
        model.addAttribute("pays", paysService.showPays());
        model.addAttribute("lastCommande", commandeService.showMaLastCommande());
        return "colis/formAddColisMaritime";
    }

    //Enregistrement d'un colis maritime
    @PostMapping("/colis/nouveau")
    public String enregistrerColisMaritime(ColisMaritime colisMaritime){
        List<CargoType> listPrix = new ArrayList<>();
        listPrix = cargoTypeService.showCargoType();
        for (CargoType c : listPrix){
            if(colisMaritime.getCartonid() == c.getCartonid() && colisMaritime.getPaysid() == c.getPaysid()){
                colisMaritime.setPrixColis(c.getPrix());
            }
        }
        colisMaritimeService.saveColisMaritime(colisMaritime);
        return "redirect:/colisMaritime/listes";
    }

    //Formulaire de mise à jour d'un colis par voie maritime
    @GetMapping("/colis/formUpdate/maritime{id}")
    public String showFormUpdateColisMaritime(@PathVariable("id") Long id, Model model){
        model.addAttribute("unColis",colisMaritimeService.showOneColisMaritime(id));
        model.addAttribute("cartons", cartonService.showCarton());
        model.addAttribute("pays", paysService.showPays());
        return "colis/formUpdateColisMaritime";
    }

    //Mise à jour d'un colis par voie maritime
    @PostMapping("/colis/update/maritime")
    public String updateColisMaritime(@ModelAttribute("colis") ColisMaritime colisMaritime){
        List<CargoType> listPrix = new ArrayList<>();
        listPrix = cargoTypeService.showCargoType();
        for (CargoType c : listPrix){
            if(colisMaritime.getCartonid() == c.getCartonid() && colisMaritime.getPaysid() == c.getPaysid()){
                colisMaritime.setPrixColis(c.getPrix());
            }
        }
        colisMaritimeService.saveColisMaritime(colisMaritime);
        return "redirect:/colis/listes";
    }

    //Suppression d'un colis par voie maritime
    @GetMapping("colis/delete/maritime")
    public String supprimerColisMaritime(Long id){
        colisMaritimeService.deleteColisMaritime(id);
        return "redirect:/colis/listes";
    }

    //Fonction pour générer la facture maritime
    @GetMapping("/colis/facture")
    public ResponseEntity<byte[]> factureMaritime() throws FileNotFoundException, JRException {
        List<ColisMaritime> listeColisMaritime = colisMaritimeService.showColisMaritimeCommande(commandeService.showMaLastCommande().getId());
        File file = ResourceUtils.getFile("classpath:factureMaritime.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listeColisMaritime);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("Données colis", "Première source");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);
        byte[] donnees = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=facture.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(donnees);
    }

    //Ancienne version de la facture
    @GetMapping("/colis/pdf")
    public void exportFactureToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String dateActuelle = df.format(new Date());
        String headerkey = "Content-Disposition";
        String headerValue = "attachment, filename=facture_"+dateActuelle+".pdf";
        //response.setHeader(headerkey, headerValue);
        List<ColisMaritime> listeColis = colisMaritimeService.showColisMaritimeCommande(commandeService.showMaLastCommande().getId());
        FactureColisPDFExporter factureColisPDFExporter = new FactureColisPDFExporter(listeColis);
        factureColisPDFExporter.export(response,
                commandeService.showMaLastCommande().getDateEnvoi(),
                commandeService.showMaLastCommande().getPin(),
                commandeService.showMaLastCommande().getRecepteur().getPays().getLibellePays(),
                commandeService.showMaLastCommande().getEmetteur().getNomPersonne()+" "+commandeService.showMaLastCommande().getEmetteur().getPrenomPersonne(),
                commandeService.showMaLastCommande().getRecepteur().getNomPersonne()+" "+commandeService.showMaLastCommande().getRecepteur().getPrenomPersonne(),
                commandeService.showMaLastCommande().getRecepteur().getEtat().getLibelleEtat(),
                commandeService.showMaLastCommande().getRecepteur().getPays().getLibellePays(),
                commandeService.showMaLastCommande().getRecepteur().getVillePersonne(),
                commandeService.showMaLastCommande().getRecepteur().getContactPersonne()
        );
    }
*/

}
