package com.mola.cargo.controller;

import com.mola.cargo.model.Etat;
import com.mola.cargo.model.Tarif;
import com.mola.cargo.service.EtatService;
import com.mola.cargo.service.PaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EtatController {

    @Autowired
    private EtatService etatService;
    @Autowired
    private PaysService paysService;

    @GetMapping("/etats")
    public String afficherEtats(Model model){
        model.addAttribute("etats", etatService.showStates());
        model.addAttribute("pays", paysService.showPays());
        return "pays/etat";
    }

    @PostMapping("etat/nouveau")
    public String enregistrerEtat(@RequestParam Long code,
                                  @RequestParam String libelle,
                                  @RequestParam Long paysid){
        Etat etat = new Etat();
        etat.setCode(code);
        etat.setLibelleEtat(libelle);
        etat.setPaysid(paysid);
        etatService.saveEtat(etat);
        return "redirect:/etats";
    }

    @GetMapping("etat/formUpdate/{id}")
    public String afficherFormUpdate(@PathVariable("id") Long code, Model model){
        model.addAttribute("unEtat", etatService.showOneState(code));
        model.addAttribute("pays", paysService.showPays());
        return "pays/formUpdateEtat";
    }

    @PostMapping("/etat/update")
    public String updateEtat(@ModelAttribute("etat") Etat etat){
        etatService.saveEtat(etat);
        return "redirect:/etats";
    }

    @GetMapping("/etat/delete")
    public String supprimerEtat(Long id){
        etatService.deleteState(id);
        return "redirect:/etats";
    }
}
