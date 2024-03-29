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
    @Autowired
    TransportService transportService;

    //Formulaire colis maritime
    @GetMapping("/colisMaritime/form")
    public String afficherFormColisMaritime(Model model){
        model.addAttribute("lastCommande", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()));
        model.addAttribute("cartons", cartonService.showCarton());
        model.addAttribute("pays", paysService.findPaysTarif());
        commandeService.updateTypeCommande(Constante.ENVOI_MARITIME, commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        return "colis/formColisMaritime";
    }

    @GetMapping("/colisMaritime/formReprise/{pin}")
    public String afficherFormColisMaritimeReprise(@PathVariable("pin") String pin, Model model){
        Commande commande = commandeService.showCommandePin(pin);
        model.addAttribute("lastCommande", commande);
        model.addAttribute("cartons", cartonService.showCarton());
        model.addAttribute("pays", paysService.findPaysTarif());
        commandeService.updateTypeCommande(Constante.ENVOI_MARITIME, commande.getId());
        return "colis/formColisMaritimeReprise";
    }

    @GetMapping("/colisMaritime/depot")
    public String afficherColisMaritimeDepot(Model model){
        model.addAttribute("colisMaritimes", colisMaritimeService.showColisMaritimeDepot(Constante.INITIAL));
        model.addAttribute("quantite", colisMaritimeService.showColisMaritimeDepot(Constante.INITIAL).size());
        return "colis/listeColisMaritimeDepot";
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
        colisMaritime.setStatut(Constante.INITIAL);
        colisMaritime.setCommandeid(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());

        List<CargoType> listPrix = new ArrayList<>();
        listPrix = cargoTypeService.showCargoType();
        for (CargoType c : listPrix){
            if(colisMaritime.getCartonid() == c.getCartonid() && colisMaritime.getPaysid() == c.getPaysid()){
                colisMaritime.setPrixColis(c.getPrix());
            }
        }
        if(!commandeService.showOnecommande(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()).getEtatCommande().equals(Constante.STATUT_PRODUIT_CREE)){
            commandeService.updateEtatCommande(Constante.STATUT_COLIS_CREE, commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        }
        colisMaritimeService.saveColisMaritime(colisMaritime);
        return "redirect:/colisMaritime/produits";
    }

    @PostMapping("/colisMaritime/nouveauReprise")
    public String enregistrerColisMaritimeReprise(@RequestParam("pin") String pin, ColisMaritime colisMaritime){
        List<ColisMaritime> listeMaritime = new ArrayList<>();
        Commande commande = new Commande();
        listeMaritime = colisMaritimeService.showColisMaritime();
        String numero = commande.getPREFIX_COLIS_MARITIME()+""+commandeService.genererNbre(commande.getNBRE_INITIAL(), commande.getNBRE_FINAL());
        while(colisMaritimeService.testerAppartenance(listeMaritime, numero)){
            numero = commande.getPREFIX_COLIS_MARITIME()+""+commandeService.genererNbre(commande.getNBRE_INITIAL(), commande.getNBRE_FINAL());
        }
        colisMaritime.setNumeroColis(numero);
        colisMaritime.setStatut(Constante.INITIAL);
        colisMaritime.setCommandeid(commandeService.showCommandePin(pin).getId());

        List<CargoType> listPrix = new ArrayList<>();
        listPrix = cargoTypeService.showCargoType();
        for (CargoType c : listPrix){
            if(colisMaritime.getCartonid() == c.getCartonid() && colisMaritime.getPaysid() == c.getPaysid()){
                colisMaritime.setPrixColis(c.getPrix());
            }
        }
        if(!commandeService.showOnecommande(commandeService.showCommandePin(pin).getId()).getEtatCommande().equals(Constante.STATUT_PRODUIT_CREE)){
            commandeService.updateEtatCommande(Constante.STATUT_COLIS_CREE, commandeService.showCommandePin(pin).getId());
        }
        colisMaritimeService.saveColisMaritime(colisMaritime);
        return "redirect:/commande/colisbis/?pin="+pin+"&num="+colisMaritime.getId();
    }

    //Liste des colis par voie maritime
    @GetMapping("/colisMaritime/listes")
    public String afficherListeColis(Model model){
        model.addAttribute("colisMaritime", colisMaritimeService.showColisMaritimeCommande(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        model.addAttribute("lastCommande", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()));
        return "colis/listeColisMaritime";
    }

    //Pour ajouter les poids des colis après pesé
    @GetMapping("/colisMaritime/ajouterPoids")
    public String afficherColisMaritime(Model model){
        model.addAttribute("lesColis", colisMaritimeService.showColisMaritimeCommande(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        model.addAttribute("lastCommande", commandeService.showMaLastCommande(Constante.showUserConnecte().getId()));
        model.addAttribute("lastColisAerien", colisMaritimeService.showMaLastColisMaritime(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId()));
        return "colis/poidsColisMaritime";
    }

    @GetMapping("/colisMaritime/ajouterPoidsBis/{pin}")
    public String afficherColisMaritimeBis(@PathVariable("pin") String pin, Model model){
        Commande commande = commandeService.showCommandePin(pin);
        model.addAttribute("lesColis", colisMaritimeService.showColisMaritimeCommande(commande.getId()));
        model.addAttribute("lastCommande", commande);
        model.addAttribute("lastColisAerien", colisMaritimeService.showMaLastColisMaritime(commande.getId()));
        return "colis/poidsColisMaritimeReprise";
    }

    @PostMapping("/colisMaritime/updatePoids")
    public String ajouterPoids(@RequestParam List<Double> poids){
        List<ColisMaritime> ListeColisMaritime = colisMaritimeService.showColisMaritimeCommande(commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        commandeService.updateNbColisCommande(ListeColisMaritime.size(), commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        //commandeService.updateEtatCommande(Constante.STATUT_COMMANDE_ACHEVE, commandeService.showMaLastCommande(Constante.showUserConnecte().getId()).getId());
        int i =0;
        for (ColisMaritime cm:ListeColisMaritime){
           /* if(cm.getCommande().getTransport().equals("Oui")){
                colisMaritimeService.updatePoidsTransportColisMaritime(colisMaritimeService.arrondirPoids(poids.get(i)),
                        transportService.calculerPrixTransportAllemangne(colisMaritimeService.arrondirPoids(poids.get(i))),cm.getId());
            }else{*/
            colisMaritimeService.updatePoidsColisMaritime(colisMaritimeService.arrondirPoids(poids.get(i)), cm.getId());
           // }
            i++;
        }
        return "redirect:/envoiMaritime/detail";
    }

    @PostMapping("/colisMaritime/updatePoidsReprise")
    public String ajouterPoidsReprise(@RequestParam List<Double> poids, @RequestParam("pin") String pin){
        Commande commande = commandeService.showCommandePin(pin);
        List<ColisMaritime> ListeColisMaritime = colisMaritimeService.showColisMaritimeCommande(commande.getId());
        commandeService.updateNbColisCommande(ListeColisMaritime.size(), commande.getId());
        //commandeService.updateEtatCommande(Constante.STATUT_COMMANDE_ACHEVE, commande.getId());
        int i =0;
        for (ColisMaritime cm:ListeColisMaritime){
            /*if(cm.getCommande().getTransport().equals("Oui")){
                colisMaritimeService.updatePoidsTransportColisMaritime(colisMaritimeService.arrondirPoids(poids.get(i)),
                        transportService.calculerPrixTransportAllemangne(colisMaritimeService.arrondirPoids(poids.get(i))),cm.getId());
            }else{*/
                colisMaritimeService.updatePoidsColisMaritime(colisMaritimeService.arrondirPoids(poids.get(i)), cm.getId());
           // }
            i++;
        }
        return "redirect:/envoiMaritime/detailReprise/?pin="+pin;

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
