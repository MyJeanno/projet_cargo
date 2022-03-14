package com.mola.cargo.controller;

import com.mola.cargo.service.ColisAerienService;
import com.mola.cargo.service.ColisMaritimeService;
import com.mola.cargo.service.CommandeService;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StatistiqueController {

    @Autowired
    private CommandeService commandeService;
    @Autowired
    private ColisMaritimeService colisMaritimeService;
    @Autowired
    private ColisAerienService colisAerienService;

    @GetMapping("/stat/depot")
    public String afficherListDepot(Model model){
        //model.addAttribute("commandes", commandeService.showCommandeEnDepot(Constante.INITIAL));
        return "statistique/statEnvoiDepot";
    }

    @GetMapping("colis/depot/{com}")
    public String afficherColisCommande(@PathVariable("com") String com, Model model){
        //System.out.println("Le code pin est : "+com);
        //System.exit(-1);
       model.addAttribute("UneCommande", commandeService.showCommandePin(com));
       if(commandeService.CommandeAppartenanceAerien(colisAerienService.showColisAerien(), com)){
           model.addAttribute("colisAerienCommandes", colisAerienService.findColisAerienCommandePin(com));
           return "statistique/colisAerienDepot";
       }else {
           model.addAttribute("colisMaritimeCommandes", colisMaritimeService.findColisMaritimeCommandePin(com));
           return "statistique/colisMaritimeDepot";
       }
    }
}
