package com.mola.cargo.controller;

import com.mola.cargo.model.Carton;
import com.mola.cargo.model.Pays;
import com.mola.cargo.service.CartonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CartonController {

    @Autowired
    private CartonService cartonService;

    @GetMapping("/cartons")
    public String afficherCarton(Model model){
        model.addAttribute("cartons", cartonService.showCarton());
        return "carton/carton";
    }

    @PostMapping("carton/nouveau")
    public String enregistrerCarton(@RequestParam String libelle,
                                    @RequestParam String code,
                                    @RequestParam String mesure,
                                    @RequestParam double prix,
                                    @RequestParam double poids){
        Carton carton = new Carton();
        carton.setLibelleCarton(libelle);
        carton.setCode(code);
        carton.setMesure(mesure);
        carton.setPrixCarton(prix);
        carton.setPoidAutorise(poids);
        carton.setQteStock(0);
        cartonService.saveCarton(carton);
        return "redirect:/cartons";
    }

    @GetMapping("carton/formUpdate/{id}")
    public String showFormEditCarton(@PathVariable("id") Long id, Model model){
        model.addAttribute("unCarton", cartonService.showOneCarton(id));
        return "carton/formUpdateCarton";
    }

    @PostMapping("carton/update")
    public String updateCarton(@ModelAttribute("carton") Carton carton){
        cartonService.saveCarton(carton);
        return "redirect:/cartons";
    }

    @GetMapping("/carton/delete")
    public String supprimerCarton(Long id){
        cartonService.deleteCarton(id);
        return "redirect:/cartons";
    }

}
