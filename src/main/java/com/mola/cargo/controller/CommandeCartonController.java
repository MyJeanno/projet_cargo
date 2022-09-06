package com.mola.cargo.controller;

import com.mola.cargo.model.CommandeCarton;
import com.mola.cargo.service.CartonService;
import com.mola.cargo.service.CommandeCartonService;
import com.mola.cargo.util.Constante;
import net.sf.jasperreports.components.table.fill.ConstantBuiltinExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
public class CommandeCartonController {

    @Autowired
    private CommandeCartonService commandeCartonService;
    @Autowired
    private CartonService cartonService;

    @GetMapping("/commande/cartons")
    public String afficherCommandeCarton(Model model){
        model.addAttribute("lesCommandes", commandeCartonService.showAllCommandeCarton());
        return "carton/commandeCarton";
    }

    @GetMapping("/commande/cartons/encours")
    public String afficherCommandeCartonENcours(Model model){
        model.addAttribute("listeCommandeCartonVente", commandeCartonService.listerCommandeCartonEncours());
        return "carton/commandeCartonEncours";
    }

    @GetMapping("/commande/carton/formvente/{id}")
    public String afficherFormVente(@PathVariable("id") Long id, Model model){
        model.addAttribute("laCommande", commandeCartonService.showOneCommandeCarton(id));
        model.addAttribute("cartons", cartonService.showCarton());
        return "carton/formVenteCarton";
    }

    @PostMapping("/commande/carton/nouveau")
    public String enregistrerCommande(CommandeCarton commandeCarton){
      commandeCarton.setDateCommande(LocalDateTime.now());
      commandeCarton.setQtera(commandeCarton.getQuantiteCommande());
      commandeCarton.setQterv(commandeCarton.getQuantiteCommande());
      commandeCarton.setLibelleCommande(Constante.MF_COMMANDE_CARTON+"_"+(commandeCartonService.showAllCommandeCarton().size()+1)+"_"+LocalDate.now());
      commandeCartonService.saveCartonCommande(commandeCarton);
      return "redirect:/commande/cartons";
    }

    @GetMapping("/commande/carton/formUpdate/{id}")
    public String showFormUpdateCommandeCarton(@PathVariable("id") Long id, Model model){
        model.addAttribute("uneCommande", commandeCartonService.showOneCommandeCarton(id));
        return "carton/formUpdateCommandeCarton";
    }

    @PostMapping("/commande/carton/update")
    public String updateCommandecarton(@ModelAttribute("commandeCarton") CommandeCarton commandeCarton){
        commandeCartonService.saveCartonCommande(commandeCarton);
        return "redirect:/commande/cartons";
    }

}
