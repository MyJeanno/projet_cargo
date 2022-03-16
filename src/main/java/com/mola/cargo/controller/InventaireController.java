package com.mola.cargo.controller;

import com.mola.cargo.service.InventaireService;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stat")
public class InventaireController {

    @Autowired
    private InventaireService inventaireService;

    @GetMapping("/inventaires")
    public String afficherPaiement(Model model){
        double prixTotal = inventaireService.sommeFactureNonEncaisse(Constante.INVENTAIRE_NON_ENCAISSE,
                Constante.LIEU_PAIEMENT_TOGO);
        model.addAttribute("inventaires", inventaireService.showInventaireSelonStatut(Constante.INVENTAIRE_NON_ENCAISSE,
                Constante.LIEU_PAIEMENT_TOGO));
        model.addAttribute("totalSomme", String.format("% ,.2f",prixTotal));
        return "sortie/inventaire";
    }

    @GetMapping("/inventaires/encaisser")
    public String afficherPaiementEncaisser(Model model){
        model.addAttribute("inventaires", inventaireService.showInventaireSelonStatut(Constante.INVENTAIRE_ENCAISSE,
                Constante.LIEU_PAIEMENT_TOGO));
        return "sortie/encaiser";
    }

    @GetMapping("/inventaires/non_paye")
    public String afficherFactureNonPayer(Model model){
        Double prixTotal = inventaireService.sommeFactureNonEncaisse(Constante.INVENTAIRE_NON_ENCAISSE,
                Constante.LIEU_PAIEMENT_ALL);
        model.addAttribute("inventaires", inventaireService.showInventaireSelonStatut(Constante.INVENTAIRE_NON_ENCAISSE,
                Constante.LIEU_PAIEMENT_ALL));
        model.addAttribute("totalSomme",String.format("% ,.2f",prixTotal));
        return "sortie/paiementAllemagne";
    }

    @GetMapping("inventaire/valider/{id}")
    public String updateInventaireEncaissement(@PathVariable("id") Long id){
       inventaireService.updateStatutInventaire(Constante.INVENTAIRE_ENCAISSE, id);
       return "redirect:/stat/inventaires";
    }

    @GetMapping("/annuler/encaissement/{id}")
    public String annulerEncaissement(@PathVariable("id") Long id){
        inventaireService.updateStatutInventaire(Constante.INVENTAIRE_NON_ENCAISSE, id);
        return "redirect:/stat/inventaires";
    }

    @GetMapping("/facture/non_paye/{id}")
    public String validerFactureNonPayer(@PathVariable("id") Long id){
        inventaireService.updateStatutInventaire(Constante.INVENTAIRE_REGLE, id);
        return "redirect:/stat/inventaires/non_paye";
    }

}
