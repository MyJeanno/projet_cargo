package com.mola.cargo.controller;

import com.mola.cargo.model.Tarif;
import com.mola.cargo.service.TarifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TarifController {

    @Autowired
    private TarifService tarifService;

    @GetMapping("/tarifs")
    public String afficherTarif(Model model){
       model.addAttribute("tarifs", tarifService.showTarifs());
       return "tarif/tarif";
    }

    @PostMapping("tarif/nouveau")
    public String enregistrerTarif(@RequestParam String libelle, @RequestParam double prix){
        Tarif tarif = new Tarif();
        tarif.setLibelleTarif(libelle);
        tarif.setPrixkilo(prix);
        tarifService.saveTarif(tarif);
        return "redirect:/tarifs";
    }

    @GetMapping("tarif/formUpdate/{id}")
    public String afficherFormUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("unTarif", tarifService.showOneTarif(id));
        return "tarif/formUpdateTarif";
    }

    @PostMapping("/tarif/update")
    public String updateTarif(@ModelAttribute("tarif") Tarif tarif){
        tarifService.saveTarif(tarif);
        return "redirect:/tarifs";
    }

    @GetMapping("/tarif/delete")
    public String supprimerTarif(Long id){
        tarifService.deleteTarif(id);
        return "redirect:/tarifs";
    }
}
