package com.mola.cargo.controller;

import com.mola.cargo.model.CartonVente;
import com.mola.cargo.service.CartonService;
import com.mola.cargo.service.CartonVenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
public class CartonVenteController {

    @Autowired
    private CartonVenteService cartonVenteService;
    @Autowired
    private CartonService cartonService;

    @GetMapping("/cartonVentes")
    public String afficherCartonVente(Model model){
        model.addAttribute("cartonVentes", cartonVenteService.showCartonVente());
        model.addAttribute("cartons", cartonService.showCarton());
        //model.addAttribute("unCarton", cartonService.findCartonById(cartonid).getQteStock());
        return "carton/cartonVente";
    }

    @GetMapping("/cartonVente/error")
    public String showErrorvente(){
        return "carton/cartonVenteError";
    }

    @PostMapping("/cartonVente/nouveau")
    public String enregistrerCartonVente(@RequestParam Long cartonid,
                                         @RequestParam String date,
                                         @RequestParam int qte) throws ParseException {
        CartonVente cartonVente = new CartonVente();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cartonVente.setCartonid(cartonid);
        cartonVente.setDateVente(df.parse(date));
        cartonVente.setQteVente(qte);
        if(qte <= cartonService.findCartonById(cartonid).getQteStock()){
            cartonVenteService.savecartonVente(cartonVente);
            cartonService.updateMoinsStockCarton(cartonid, qte);
            return "redirect:/cartonVentes";
        }else {
            return "redirect:/cartonVente/error";
        }

    }

    @GetMapping("/cartonVente/update/{id}")
    public String showFormUpdate(@PathVariable("id") Long id, Model model){
       model.addAttribute("uneVente", cartonVenteService.showOneCartonVente(id));
        model.addAttribute("cartons", cartonService.showCarton());
        return "carton/formUpdateVente";
    }

    @PostMapping("/cartonVente/update")
    public String updatevente(@ModelAttribute("cartonVente") CartonVente cartonVente,
                              @RequestParam String qtebdd,
                              @RequestParam String cartonidbdd){
        int qteDiff = cartonVente.getQteVente()-Integer.parseInt(qtebdd);
        if(cartonVente.getId()==Long.parseLong(cartonidbdd)){
            if(cartonVente.getQteVente()<=cartonService.findCartonById(Long.parseLong(cartonidbdd)).getQteStock()){
                cartonVenteService.savecartonVente(cartonVente);
                cartonService.updateMoinsStockCarton(cartonVente.getCartonid(), qteDiff);
                return "redirect:/cartonVentes";
            }else{
                return "redirect:/cartonVente/error";
            }
        }else
            if(cartonVente.getQteVente()<=cartonService.findCartonById(cartonVente.getCartonid()).getQteStock()) {
            cartonVenteService.savecartonVente(cartonVente);
            cartonService.updateAddStockCarton(Long.parseLong(cartonidbdd),Integer.parseInt(qtebdd));
            cartonService.updateMoinsStockCarton(cartonVente.getCartonid(),cartonVente.getQteVente());
            return "redirect:/cartonVentes";
            }else{
                return "redirect:/cartonVente/error";
            }
        }


    @GetMapping("cartonVente/delete")
    public String supprimerVente(Long id, Long idca, int qte){
        cartonVenteService.deleteCartonVente(id);
        cartonService.updateAddStockCarton(idca, qte);
        return "redirect:/cartonVentes";
    }
}
