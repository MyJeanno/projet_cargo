package com.mola.cargo.controller;

import com.mola.cargo.model.Paiement;
import com.mola.cargo.model.Piece;
import com.mola.cargo.service.PaiementService;
import com.mola.cargo.service.PieceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/param")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    @GetMapping("/paiements")
    public String listerPaiement(Model model){
        model.addAttribute("paiements", paiementService.showPaiement());
        return "paiement/paiement";
    }

    @PostMapping("paiement/nouveau")
    public String enregistrerPaiement(@RequestParam String mode){
        Paiement paiement = new Paiement();
        paiement.setModePaiement(mode);
        paiementService.savePaiement(paiement);
        return "redirect:/param/paiements";
    }

    @GetMapping("/paiement/formUpdate/{id}")
    public String showFormUpdatePaiement(@PathVariable("id") Long id, Model model){
        model.addAttribute("unPaiement", paiementService.showOnePaiement(id));
        return "paiement/formUpdate";
    }

    @PostMapping("/paiement/update")
    public String updatePaiement(@ModelAttribute("paiement") Paiement paiement){
        paiementService.savePaiement(paiement);
        return "redirect:/param/paiements";
    }

    @GetMapping("paiement/delete/")
    public String supprimerPaiement(Long id){
        paiementService.deletePaiement(id);
        return "redirect:/param/paiements";
    }
}
