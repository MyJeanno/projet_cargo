package com.mola.cargo.controller;

import com.mola.cargo.model.CargoType;
import com.mola.cargo.model.Piece;
import com.mola.cargo.service.CargoTypeService;
import com.mola.cargo.service.PieceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CargoTypeController {

    @Autowired
    private CargoTypeService cargoTypeService;

    @GetMapping("/cargoTypes")
    public String listerCargoType(Model model){
        model.addAttribute("cargoTypes", cargoTypeService.showCargoType());
        return "cargoType/cargoType";
    }

    @PostMapping("cargoType/nouveau")
    public String enregistrerCargoType(@RequestParam String pays,
                                       @RequestParam String typeCarton,
                                       @RequestParam double prix){
        CargoType cargoType = new CargoType();
        cargoType.setRegion(pays);
        cargoType.setLibelleType(typeCarton);
        cargoType.setPrix(prix);
        cargoTypeService.saveCargoType(cargoType);
        return "redirect:/cargoTypes";
    }

    @GetMapping("cargoType/formUpdate/{id}")
    public String showFormUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("unCargo", cargoTypeService.oneCargoType(id));
        return "cargoType/formUpdate";
    }

    @PostMapping("cargoType/update")
    public String updateCargo(@ModelAttribute("cargoType") CargoType cargoType){
        cargoTypeService.saveCargoType(cargoType);
        return "redirect:/cargoTypes";
    }

    @GetMapping("cargoType/delete/")
    public String supprimerCargoType(Long id){
        cargoTypeService.deleteCargoType(id);
        return "redirect:/cargoTypes";
    }


}
