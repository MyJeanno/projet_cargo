package com.mola.cargo.controller;

import com.mola.cargo.model.*;
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
import org.springframework.web.bind.annotation.*;

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

    //Formulaire colis maritime
    @GetMapping("/colisMaritime/form")
    public String afficherFormColisMaritime(Model model){
        model.addAttribute("lastCommande", commandeService.showMaLastCommande());
        model.addAttribute("cartons", cartonService.showCarton());
        model.addAttribute("pays", paysService.showPays());
        return "colis/formColisMaritime";
    }

    //Fonction pour enregistrer un colis maritime
    @PostMapping("/colisMaritime/nouveau")
    public String enregistrerColisMaritime(ColisMaritime colisMaritime){
        List<ColisMaritime> listeMaritime = new ArrayList<>();
        Commande commande = new Commande();
        listeMaritime = colisMaritimeService.showColisMaritime();
        String numero = commande.getPREFIX_COLIS_MARITIME()+""+commandeService.genererNbre(commande.getNBRE_INITIAL(), commande.getNBRE_FINAL());
        while(colisMaritimeService.testerAppartenance(listeMaritime, numero)){
            numero = commande.getPREFIX_COLIS_MARITIME()+""+commandeService.genererNbre(commande.getNBRE_INITIAL(), commande.getNBRE_FINAL());
        }
        colisMaritime.setNumeroColis(numero);
        colisMaritime.setCommandeid(commandeService.showMaLastCommande().getId());

        List<CargoType> listPrix = new ArrayList<>();
        listPrix = cargoTypeService.showCargoType();
        for (CargoType c : listPrix){
            if(colisMaritime.getCartonid() == c.getCartonid() && colisMaritime.getPaysid() == c.getPaysid()){
                colisMaritime.setPrixColis(c.getPrix());
            }
        }
        colisMaritimeService.saveColisMaritime(colisMaritime);
        return "redirect:/colisMaritime/produits";
    }

    //Liste des colis par voie maritime
    @GetMapping("/colisMaritime/listes")
    public String afficherListeColis(Model model){
        model.addAttribute("colisMaritime", colisMaritimeService.showColisMaritimeCommande(commandeService.showMaLastCommande().getId()));
        model.addAttribute("lastCommande", commandeService.showMaLastCommande());
        return "colis/listeColisMaritime";
    }

    //Formulaire de mise à jour d'un colis par voie maritime
    @GetMapping("/colisMaritime/formUpdate/{id}")
    public String showFormUpdateColisMaritime(@PathVariable("id") Long id, Model model){
        model.addAttribute("unColisMaritime",colisMaritimeService.showOneColisMaritime(id));
        model.addAttribute("cartons", cartonService.showCarton());
        model.addAttribute("pays", paysService.showPays());
        return "colis/formUpdateColisMaritime";
    }

    //Mise à jour d'un colis par voie maritime
    @PostMapping("/colisMaritime/update")
    public String updateColisMaritime(@ModelAttribute("colisMaritime") ColisMaritime colisMaritime){
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

   //Suppression d'un colis par voie maritime
    @GetMapping("/colisMaritime/delete")
    public String supprimerColisMaritime(Long id){
        colisMaritimeService.deleteColisMaritime(id);
        return "redirect:/colisMaritime/listes";
    }

    //Formulaire de choix d'une commande
    @GetMapping("/choixMaritime/commande")
    public String showFormChoixCommande(){
        return "colis/choixCommandeMaritime";
    }

    @GetMapping("/colisMaritime/commande")
    public String showColisMaritimeCommande(@RequestParam String numCom, Model model){
        Long id = commandeService.showCommandePin(numCom).getId();
        //System.out.println("L'id de la commande est : "+id);
        model.addAttribute("colisMaritime", colisMaritimeService.showColisMaritimeCommande(id));
        model.addAttribute("lastCommande", commandeService.showCommandePin(numCom));
        return "colis/listeColisMaritime";
    }

    /*//Fonction pour générer la facture maritime
    @GetMapping("/colis/facture")
    public ResponseEntity<byte[]> factureMaritime() throws FileNotFoundException, JRException {
        List<ColisMaritime> listeColisMaritime = colisMaritimeService.showColisMaritimeCommande(commandeService.showMaLastCommande().getId());
        File file = ResourceUtils.getFile("classpath:factureMaritimePaye.jrxml");
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
