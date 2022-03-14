package com.mola.cargo.controller;

import com.mola.cargo.model.Convoi;
import com.mola.cargo.service.ConvoiService;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class ConvoiController {

    @Autowired
    private ConvoiService convoiService;

    @GetMapping("/convoiMaritimes")
    public String afficherConvoiMaritime(Model model){
        model.addAttribute("convoiMaritimes", convoiService.findConvoiMaritime());
        return "sortie/convoiMaritime";
    }

    @GetMapping("/convoiAeriens")
    public String afficherConvoiAerien(Model model){
        model.addAttribute("convoiAeriens", convoiService.findConvoiAerien());
        return "sortie/convoiAerien";
    }

    @PostMapping("/convoiMaritime/nouveau")
    public String enregistrerConvoiMaritime(){
        Convoi convoi = new Convoi();
        convoi.setIdentifiant(Constante.PREFIX_MARITIME+"_"+ LocalDate.now());
        convoi.setDateCreation(LocalDateTime.now());
        convoiService.saveConvoi(convoi);
        return "redirect:/convoiMaritimes";
    }

    @PostMapping("/convoiAerien/nouveau")
    public String enregistrerConvoiAerien(){
        Convoi convoi = new Convoi();
        convoi.setIdentifiant(Constante.PREFIX_AERIEN+"_"+ LocalDate.now());
        convoi.setDateCreation(LocalDateTime.now());
        convoiService.saveConvoi(convoi);
        return "redirect:/convoiAeriens";
    }

    @GetMapping("")
    public String supprimerConvoi(Long id){
        convoiService.deleteConvoi(id);
        return "redirect:/convois";
    }
}
