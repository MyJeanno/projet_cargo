package com.mola.cargo.controller;

import com.mola.cargo.model.Emballage;
import com.mola.cargo.service.EmballageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmballageController {

    @Autowired
    private EmballageService emballageService;

    @GetMapping("/emballages")
    public String listerEmballage(Model model){
        model.addAttribute("emballages", emballageService.showEmballages());
        return "colis/listeEmballage";
    }

    @PostMapping("/emballage/nouveau")
    public String enregistrerEmballage(@RequestParam String taille, @RequestParam double prix){
        Emballage emballage = new Emballage();
        emballage.setTaille(taille);
        emballage.setPrix(prix);
        emballageService.saveEmballage(emballage);
        return "redirect:/emballages";
    }

    @GetMapping("/emballage/formUpdate/{id}")
    public String showFormUpdate(@PathVariable("id") Long id, Model model){
      model.addAttribute("unEmballage", emballageService.showOneEmballage(id));
      return "colis/formUpdateEmballage";
    }

    @PostMapping("/emballage/update")
    public String updateEmballage(@ModelAttribute("Emballage") Emballage emballage){
        emballageService.saveEmballage(emballage);
        return "redirect:/emballages";
    }

    @GetMapping("/emballage/delete")
    public String suprimerEmballage(Long id){
        emballageService.deleteEmballage(id);
        return "redirect:/emballages";
    }


}
