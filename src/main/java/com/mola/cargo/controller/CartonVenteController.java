package com.mola.cargo.controller;

import com.mola.cargo.model.CartonVente;
import com.mola.cargo.model.VueCommandeStat;
import com.mola.cargo.service.CartonService;
import com.mola.cargo.service.CartonVenteService;
import com.mola.cargo.service.CommandeCartonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
public class CartonVenteController {

    @Autowired
    private CartonVenteService cartonVenteService;
    @Autowired
    private CartonService cartonService;
    @Autowired
    private CommandeCartonService commandeCartonService;

    @GetMapping("/cartonVentes")
    public String afficherCartonVente(Model model){
        model.addAttribute("cartonVentes", cartonVenteService.showCartonVente());
        model.addAttribute("cartons", cartonService.showCarton());
        model.addAttribute("listeCommandeCartonVente", commandeCartonService.listerCommandeCartonEncours());
        return "carton/cartonVente";
    }

    @GetMapping("/cartonVente/error")
    public String showErrorvente(){
        return "carton/cartonVenteError";
    }

    @PostMapping("/cartonVente/nouveau")
    public String enregistrerCartonVente(@RequestParam Long cartonid,
                                         @RequestParam Long comid,
                                         @RequestParam int qte) throws ParseException {
        CartonVente cartonVente = new CartonVente();
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cartonVente.setCartonid(cartonid);
        //cartonVente.setDateVente(df.parse(date));
        cartonVente.setDateVente(LocalDateTime.now());
        cartonVente.setQteVente(qte);
        cartonVente.setCommandeCartonid(comid);
        if(qte <= cartonService.findCartonById(cartonid).getQteStock()){
            cartonVenteService.savecartonVente(cartonVente);
            cartonService.updateMoinsStockCarton(cartonid, qte);
            commandeCartonService.updateCommandeCartonVente(comid, qte);
            return "redirect:/cartonVentes";
        }else {
            return "redirect:/cartonVente/error";
        }

    }

    @GetMapping("/cartonVente/update/{id}")
    public String showFormUpdate(@PathVariable("id") Long id, Model model){
       model.addAttribute("uneVente", cartonVenteService.showOneCartonVente(id));
        model.addAttribute("cartons", cartonService.showCarton());
        return "carton/formUpdateVente";
    }

    @PostMapping("/cartonVente/update")
    public String updatevente(@ModelAttribute("cartonVente") CartonVente cartonVente,
                              @RequestParam String qtebdd,
                              @RequestParam String cartonidbdd){
        int qteDiff = cartonVente.getQteVente()-Integer.parseInt(qtebdd);
        if(cartonVente.getId()==Long.parseLong(cartonidbdd)){
            if(cartonVente.getQteVente()<=cartonService.findCartonById(Long.parseLong(cartonidbdd)).getQteStock()){
                cartonVenteService.savecartonVente(cartonVente);
                cartonService.updateMoinsStockCarton(cartonVente.getCartonid(), qteDiff);
                return "redirect:/cartonVentes";
            }else{
                return "redirect:/cartonVente/error";
            }
        }else
            if(cartonVente.getQteVente()<=cartonService.findCartonById(cartonVente.getCartonid()).getQteStock()) {
            cartonVenteService.savecartonVente(cartonVente);
            cartonService.updateAddStockCarton(Long.parseLong(cartonidbdd),Integer.parseInt(qtebdd));
            cartonService.updateMoinsStockCarton(cartonVente.getCartonid(),cartonVente.getQteVente());
            return "redirect:/cartonVentes";
            }else{
                return "redirect:/cartonVente/error";
            }
        }

    @GetMapping("cartonVente/delete")
    public String supprimerVente(Long id, Long idca, int qte){
        cartonVenteService.deleteCartonVente(id);
        cartonService.updateAddStockCarton(idca, qte);
        return "redirect:/cartonVentes";
    }

    @GetMapping("/commande/form/periode")
    public String etatPeriode(@RequestParam("d1") String d1, @RequestParam("d2") String d2, Model model) throws ParseException {
        List<VueCommandeStat> listeCommande = new ArrayList<>();
        model.addAttribute("lesVentes", cartonVenteService.etatventeCarton(LocalDate.parse(d1), LocalDate.parse(d2)));
        model.addAttribute("date1", DateFormat.getDateInstance(DateFormat.FULL, new Locale("fr","FR")).format(new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.parse(d1).toString())));
        model.addAttribute("date2", DateFormat.getDateInstance(DateFormat.FULL, new Locale("fr","FR")).format(new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.parse(d2).toString())));
        //model.addAttribute("date2", LocalDate.parse(d2));
        for(String s : cartonVenteService.etatventeCarton(LocalDate.parse(d1), LocalDate.parse(d2))){
            VueCommandeStat vc = new VueCommandeStat();
            vc.setLibelleCom(Arrays.asList(s.split(",")).get(0));
            vc.setQteCom(Integer.parseInt(Arrays.asList(s.split(",")).get(1)));
            vc.setQteVendue(Integer.parseInt(Arrays.asList(s.split(",")).get(2)));
            vc.setQteRestante(Integer.parseInt(Arrays.asList(s.split(",")).get(3)));
            vc.setMontantCom(Double.parseDouble(Arrays.asList(s.split(",")).get(4)));
            vc.setTotalVendu(Double.parseDouble(Arrays.asList(s.split(",")).get(5)));
            vc.setBenefice(Double.parseDouble(Arrays.asList(s.split(",")).get(6)));
            listeCommande.add(vc);
        }
        model.addAttribute("lesVentes", listeCommande);
        return "carton/statVente";
    }

    @GetMapping("/carton/vente/stat")
    public String afficherEtatVente(Model model){
        List<VueCommandeStat> listeCommande = new ArrayList<>();
        model.addAttribute("lesVentes", cartonVenteService.etatventeCarton(null, null));
        for(String s : cartonVenteService.etatventeCarton(null, null)){
            //System.out.println("******************************LISTE DES VALEURS : "+ Arrays.asList(s.split(",")).get(0));
            VueCommandeStat vc = new VueCommandeStat();
            vc.setLibelleCom(Arrays.asList(s.split(",")).get(0));
            vc.setQteCom(Integer.parseInt(Arrays.asList(s.split(",")).get(1)));
            vc.setQteVendue(Integer.parseInt(Arrays.asList(s.split(",")).get(2)));
            vc.setQteRestante(Integer.parseInt(Arrays.asList(s.split(",")).get(3)));
            vc.setMontantCom(Double.parseDouble(Arrays.asList(s.split(",")).get(4)));
            vc.setTotalVendu(Double.parseDouble(Arrays.asList(s.split(",")).get(5)));
            vc.setBenefice(Double.parseDouble(Arrays.asList(s.split(",")).get(6)));
            listeCommande.add(vc);
        }
        model.addAttribute("lesVentes", listeCommande);
        //System.out.println("**************************** LISTE = "+listeCommande);
        return "carton/statVente";
    }
}
