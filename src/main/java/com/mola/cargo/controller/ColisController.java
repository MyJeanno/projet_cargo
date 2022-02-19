package com.mola.cargo.controller;

import com.mola.cargo.model.CargoType;
import com.mola.cargo.model.Colis;
import com.mola.cargo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ColisController {
/*
    @Autowired
    private ColisService colisService;
    @Autowired
    private CartonService cartonService;
    @Autowired
    private PaysService paysService;
    @Autowired
    private CommandeService commandeService;
    @Autowired
    private CargoTypeService cargoTypeService;

    @PostMapping("/colis/nouveau")
    public String enregistrerColis(Colis colis){
        List<CargoType> listPrix = new ArrayList<>();
        listPrix = cargoTypeService.showCargoType();
        for (CargoType c : listPrix){
            if(colis.getCartonid() == c.getCartonid() && colis.getPaysid() == c.getPaysid()){
               colis.setPrixColis(c.getPrix());
            }
        }
        colisService.saveColis(colis);
        return "redirect:/colis/formColis";
    }

    //Renvoie le formulaire de saisie des colis
    @GetMapping("/colis/formColis")
    public String showFormColis(Model model){
        model.addAttribute("cartons", cartonService.showCarton());
        model.addAttribute("pays", paysService.showPays());
        model.addAttribute("lastCommande", commandeService.showMaLastCommande());
        return "colis/formAddColis";
    }*/

}
