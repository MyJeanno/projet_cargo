package com.mola.cargo.controller;

import com.mola.cargo.service.AnnulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stat")
public class AnnulationController {

    @Autowired
    private AnnulationService annulationService;

    @GetMapping("/inventaire/annulation/liste")
    public String afficherAnnulation(Model model){
        model.addAttribute("lesAnnulations", annulationService.showAllAnnulation());
        return "inventaire/listeAnnulation";
    }
}
