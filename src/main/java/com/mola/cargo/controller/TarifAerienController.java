package com.mola.cargo.controller;

import com.mola.cargo.model.TarifAerien;
import com.mola.cargo.service.TarifAerienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TarifAerienController {

    @Autowired
    private TarifAerienService tarifAerienService;

    @GetMapping("/tarifAerien/param")
    public String afficherTarif(Model model){
        model.addAttribute("tarifs", tarifAerienService.showAllTarifAerien());
        return "tarif/tarifParam";
    }

    @PostMapping("/tarifAerien/save")
    public String enregistrerTarif(@RequestParam double prix, @RequestParam double taxe){
        TarifAerien tarifAerien = new TarifAerien();
        tarifAerien.setPrix(prix);
        tarifAerien.setTaxe(taxe);
        tarifAerienService.saveTarifAerien(tarifAerien);
       return "redirect:/tarifAerien/param";
    }

    @GetMapping("/tarifAerien/formUpdate/{id}")
    public String afficherFormUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("unTarifAerien", tarifAerienService.showOneTarifAerien(id));
        return "tarif/updateTarifAerien";
    }

    @PostMapping("/tarifAerien/update")
    public String updateTarifAerien(@ModelAttribute("tarifAerien") TarifAerien tarifAerien){
        tarifAerienService.saveTarifAerien(tarifAerien);
        return "redirect:/tarifAerien/param";
    }

    @GetMapping("/tarifAerien/delete/")
    public String supprimerTarifAerien(Long id){
        tarifAerienService.deleteTarifAerien(id);
        return "redirect:/tarifAerien/param";
    }
}
