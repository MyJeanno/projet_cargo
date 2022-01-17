package com.mola.cargo.controller;

import com.mola.cargo.model.Carton;
import com.mola.cargo.model.CartonAppro;
import com.mola.cargo.service.CartonApproService;
import com.mola.cargo.service.CartonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class CartonApproController {

    @Autowired
    private CartonApproService cartonApproService;
    @Autowired
    private CartonService cartonService;

    @GetMapping("/cartonAppros")
    public String afficherCartonAppro(Model model){
        model.addAttribute("cartonAppros", cartonApproService.showCartonAppro());
        model.addAttribute("cartons", cartonService.showCarton());
        return "carton/cartonAppro";
    }

    @PostMapping("/cartonAppro/nouveau")
    public String enregistrerCartonAppro(@RequestParam Long cartonid,
                                         @RequestParam String date,
                                         @RequestParam int qte) throws ParseException {
        CartonAppro cartonAppro = new CartonAppro();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d = df.parse(date);
        cartonAppro.setCartonid(cartonid);
        cartonAppro.setDateAppro(d);
        cartonAppro.setQteAppro(qte);
        cartonApproService.saveCartonAppro(cartonAppro);
        cartonService.updateAddStockCarton(cartonid, qte);
       return "redirect:/cartonAppros";
    }

    @GetMapping("/cartonAppro/update/{id}")
    public String showFormUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("unAppro", cartonApproService.findOneCartonAppro(id));
        model.addAttribute("cartons", cartonService.showCarton());
        return "carton/formUpdateAppro";
    }

    @PostMapping("/cartonAppro/update")
    public String updateCartonAppro(@ModelAttribute("cartonAppro") CartonAppro cartonAppro,
                                    @RequestParam String qtebdd,
                                    @RequestParam String cartonidbdd
                                    ) {
        int qteDiff = cartonAppro.getQteAppro()-Integer.parseInt(qtebdd);
        cartonApproService.saveCartonAppro(cartonAppro);
        if (cartonAppro.getCartonid()==Long.parseLong(cartonidbdd)){
            cartonService.updateAddStockCarton(cartonAppro.getCartonid(),qteDiff);
        }else{
            cartonService.updateMoinsStockCarton(Long.parseLong(cartonidbdd),Integer.parseInt(qtebdd));
            cartonService.updateAddStockCarton(cartonAppro.getCartonid(),cartonAppro.getQteAppro());
        }
        return "redirect:/cartonAppros";
    }

    @GetMapping("cartonAppro/delete")
    public String supprimerCartonAppro(Long id, Long idca, int qte){
        cartonApproService.deleteCartonAppro(id);
        cartonService.updateMoinsStockCarton(idca, qte);
        return "redirect:/cartonAppros";
    }
}
