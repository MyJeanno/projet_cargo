package com.mola.cargo.controller;

import com.mola.cargo.model.Reduction;
import com.mola.cargo.service.ReductionService;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReductionController {

    @Autowired
    private ReductionService reductionService;

    @GetMapping("/reduction/param")
    public String afficherReduction(Model model){
        model.addAttribute("reductions", reductionService.showAllReduction());
        return "tarif/reduction";
    }

    @PostMapping("/reduction/save")
    public String enregistrerTarif(@RequestParam double taux){
        Reduction reduction = new Reduction();
        reduction.setTaux(taux);
        reduction.setActivation(Constante.TAXE_NON);
        reductionService.saveReduction(reduction);
       return "redirect:/reduction/param";
    }

    @GetMapping("/reduction/formUpdate/{id}")
    public String afficherFormUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("uneReduction", reductionService.showOneReduction(id));
        return "tarif/updateReduction";
    }

    @PostMapping("/reduction/update")
    public String updateTarifAerien(@ModelAttribute("tarifAerien") Reduction reduction){
        reductionService.saveReduction(reduction);
        return "redirect:/reduction/param";
    }

    @GetMapping("/reduction/delete/")
    public String supprimerTarifAerien(Long id){
        reductionService.deleteReduction(id);
        return "redirect:/reduction/param";
    }
}
