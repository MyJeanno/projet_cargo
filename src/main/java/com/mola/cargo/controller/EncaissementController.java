package com.mola.cargo.controller;

import com.mola.cargo.model.Encaissement;
import com.mola.cargo.service.EncaissementService;
import com.mola.cargo.service.InventaireService;
import com.mola.cargo.service.RecepteurService;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/stat")
public class EncaissementController {

    @Autowired
    private EncaissementService encaissementService;
    @Autowired
    private RecepteurService recepteurService;
    @Autowired
    private InventaireService inventaireService;

    @GetMapping("/encaissement/liste")
    public String afficherEncaissement(Model model){
        model.addAttribute("listeEncaissement", encaissementService.showEncaissement());
        return "inventaire/ListeEncaissement";
    }

    @GetMapping("/form/encaissement/{id}")
    public String afficherFormEncaissement(@PathVariable("id") Long id, Model model){
        model.addAttribute("unInventaire", inventaireService.showOneInventaire(id));
        model.addAttribute("reste", String.format("% ,.2f", inventaireService.showOneInventaire(id).getCommande().getRecepteur().getSolde()));
        return "inventaire/formEncaissement";
    }

    @PostMapping("/encaissement/facture")
    public String validerFactureNonPayer(@RequestParam double montant, @RequestParam Long id){
        Encaissement encaissement = new Encaissement();
        encaissement.setDateEncaissement(LocalDate.now());
        encaissement.setCommande_id(inventaireService.showOneInventaire(id).getCommande().getId());
        encaissement.setMontant(montant);
        encaissementService.saveEncaissement(encaissement);
        if(montant==inventaireService.showOneInventaire(id).getCommande().getRecepteur().getSolde()){
            inventaireService.updateStatutInventaire(Constante.INVENTAIRE_ENCAISSE, id);
        }
        recepteurService.updateSoldeClientEncaissement(montant, inventaireService.showOneInventaire(id).getCommande().getRecepteur().getId());
        return "redirect:/stat/inventaires/non_paye";
    }

    @GetMapping("/encaissement/formUpdate/{id}")
    public String showFormUpdate(@PathVariable("id") Long id, Model model){
       model.addAttribute("unEncaissement", encaissementService.showOneEncaissement(id));
       return "inventaire/formUpdateEncaissement";
    }

    @PostMapping("/encaissement/update")
    public String updateEncaissement(@ModelAttribute("encaissement") Encaissement encaissement, @RequestParam String mont){
        double montantDiff = encaissement.getMontant()-Double.parseDouble(mont);
        encaissementService.saveEncaissement(encaissement);
        recepteurService.updateSoldeClientEncaissement(montantDiff, encaissementService.showOneEncaissement(encaissement.getId()).getCommande().getRecepteur().getId());
        return "redirect:/stat/encaissement/liste";
    }

    @GetMapping("/encaissement/delete")
    public String supprimerEncaissement(Long id){
        //System.out.println("********************************"+encaissementService.showOneEncaissement(id).getMontant());
        recepteurService.updateSoldeClient(encaissementService.showOneEncaissement(id).getMontant(),
                encaissementService.showOneEncaissement(id).getCommande().getRecepteur().getId());
        encaissementService.deleteEncaissement(id);
        return "redirect:/stat/encaissement/liste";
    }


}
