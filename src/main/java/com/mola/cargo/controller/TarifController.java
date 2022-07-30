package com.mola.cargo.controller;

import com.mola.cargo.model.CategorieProduit;
import com.mola.cargo.model.Tarif;
import com.mola.cargo.service.CategorieProduitService;
import com.mola.cargo.service.TarifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("param")
public class TarifController {

    @Autowired
    private TarifService tarifService;
    @Autowired
    private CategorieProduitService categorieProduitService;

    @GetMapping("/tarifs")
    public String afficherTarif(Model model){
       model.addAttribute("tarifs", tarifService.showTarifs());
        model.addAttribute("lesCategories", categorieProduitService.showAllCategorieProduit());
       return "tarif/tarif";
    }

    @PostMapping("tarif/nouveau")
    public String enregistrerTarif(@RequestParam String libelle,
                                   @RequestParam double prix,
                                   @RequestParam double taxeA,
                                   @RequestParam double taxeM,
                                   @RequestParam Long categorieId
                                   ){
        Tarif tarif = new Tarif();
        tarif.setLibelleTarif(libelle);
        tarif.setPrixKilo(prix);
        tarif.setTaxeAerien(taxeA);
        tarif.setTaxeMaritime(taxeM);
        tarif.setCategorieId(categorieId);
        tarifService.saveTarif(tarif);
        return "redirect:/param/tarifs";
    }

    @GetMapping("tarif/formUpdate/{id}")
    public String afficherFormUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("unTarif", tarifService.showOneTarif(id));
        model.addAttribute("lesCategories", categorieProduitService.showAllCategorieProduit());
        return "tarif/formUpdateTarif";
    }

    @PostMapping("/tarif/update")
    public String updateTarif(@ModelAttribute("tarif") Tarif tarif){
        tarifService.saveTarif(tarif);
        return "redirect:/param/tarifs";
    }

    @GetMapping("/tarif/delete")
    public String supprimerTarif(Long id){
        tarifService.deleteTarif(id);
        return "redirect:/param/tarifs";
    }
}
