package com.mola.cargo.controller;

import com.mola.cargo.model.Convoi;
import com.mola.cargo.model.SortieAerien;
import com.mola.cargo.model.SortieMaritime;
import com.mola.cargo.service.*;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class SortieMaritimeController {

    @Autowired
    private SortieMaritimeService sortieMaritimeService;
    @Autowired
    private ColisMaritimeService colisMaritimeService;
    @Autowired
    private ConvoiService convoiService;

    @GetMapping("/sortieMaritimes")
    public String afficherSortie(Model model){
        model.addAttribute("sortieMaritimes", sortieMaritimeService.showSortieMaritimes());
        model.addAttribute("colisMaritimes", colisMaritimeService.showColisMaritimeDepot(Constante.INITIAL));
        model.addAttribute("unConvoi", convoiService.showMaLastConvoiMaritime());
        return "sortie/sortieMaritime";
    }

    @GetMapping("sortieMaritimes/colis/{id}")
    public String afficherSortieAdmin(@PathVariable("id") Long id, Model model){
        model.addAttribute("sortieMaritimes", sortieMaritimeService.showSortieColisMaritimeConvois(id));
        model.addAttribute("unConvoi", convoiService.showOneConvoi(id));
        return "sortie/sortieMaritimeAdmin";
    }

    @PostMapping("/sortieMaritime/nouveau")
    public String enregistrerSortieMaritime(@RequestParam Long colisMaritimeid, @RequestParam Long identifiant){
        SortieMaritime sortieMaritime = new SortieMaritime();
        sortieMaritime.setColisMaritimeid(colisMaritimeid);
        sortieMaritime.setConvoiid(identifiant);
        sortieMaritimeService.saveSortieMaritime(sortieMaritime);
        colisMaritimeService.updateStatutColisMaritime(Constante.SORTIE, colisMaritimeid);
        return "redirect:/sortieMaritimes";
    }

    @GetMapping("sortieMaritime/formUpdate/{id}")
    public String afficherFormUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("UneSortieMaritime", sortieMaritimeService.showOneSortieMaritime(id));
        model.addAttribute("colisMaritimes", colisMaritimeService.showColisMaritime());
        return "sortie/formUpdateSortieMaritime";
    }

    @PostMapping("/sortieMaritime/update")
    public String updateSortieMaritime(@ModelAttribute("SortieMaritime") SortieMaritime sortieMaritime){
        sortieMaritimeService.saveSortieMaritime(sortieMaritime);
        return "redirect:/sortieMaritimes";
    }

    @GetMapping("sortieMaritime/delete")
    public String supprimer(Long id){
        sortieMaritimeService.deleteSortieMaritime(id);
        colisMaritimeService.updateStatutColisMaritime(Constante.INITIAL, id);
        return "redirect:/sortieMaritimes";
    }
}
