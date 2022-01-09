package com.mola.cargo.controller;

import com.mola.cargo.model.Pays;
import com.mola.cargo.service.PaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PaysController {

    @Autowired
    private PaysService paysService;

    @GetMapping("/pays")
    public String afficherPays(Model model){
      model.addAttribute("pays", paysService.showPays());
      return "pays/pays";
    }

    @PostMapping("pays/nouveau")
    public String enregistrerPays(@RequestParam String libelle, @RequestParam String livraison){
        Pays pays = new Pays();
        pays.setLibellePays(libelle);
        pays.setLivraison(livraison);
        paysService.savePays(pays);
        return "redirect:/pays";
    }

    @GetMapping("pays/formUpdate/{id}")
    public String showFormEditPays(@PathVariable("id") Long id, Model model){
       model.addAttribute("unPays", paysService.showOnePays(id));
       return "pays/formUpdatePays";
    }

    @PostMapping("pays/update")
    public String updatePays(@ModelAttribute("pays") Pays pays){
       paysService.savePays(pays);
       return "redirect:/pays";
    }

    @GetMapping("/pays/delete")
    public String supprimerPays(Long id){
        paysService.deletePays(id);
        return "redirect:/pays";
    }
}
