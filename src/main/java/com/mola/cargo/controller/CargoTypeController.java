package com.mola.cargo.controller;

import com.mola.cargo.model.CargoType;
import com.mola.cargo.model.Piece;
import com.mola.cargo.service.CargoTypeService;
import com.mola.cargo.service.CartonService;
import com.mola.cargo.service.PaysService;
import com.mola.cargo.service.PieceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CargoTypeController {

    @Autowired
    private CargoTypeService cargoTypeService;
    @Autowired
    private CartonService cartonService;
    @Autowired
    private PaysService paysService;

    @GetMapping("/cargoTypes")
    public String listerCargoType(Model model){
        model.addAttribute("cargoTypes", cargoTypeService.showCargoType());
        model.addAttribute("cartons", cartonService.showCarton());
        model.addAttribute("pays", paysService.showPays());
        return "cargoType/cargoType";
    }

    @PostMapping("cargoType/nouveau")
    public String enregistrerCargoType(@RequestParam Long cartonid,
                                       @RequestParam Long paysid,
                                       @RequestParam double prix){
        CargoType cargoType = new CargoType();
        cargoType.setCartonid(cartonid);
        cargoType.setPaysid(paysid);
        cargoType.setPrix(prix);
        cargoTypeService.saveCargoType(cargoType);
        return "redirect:/cargoTypes";
    }

    @GetMapping("cargoType/formUpdate/{id}")
    public String showFormUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("unCargo", cargoTypeService.oneCargoType(id));
        model.addAttribute("cartons", cartonService.showCarton());
        model.addAttribute("pays", paysService.showPays());
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
