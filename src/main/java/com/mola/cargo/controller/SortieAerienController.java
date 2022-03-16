package com.mola.cargo.controller;

import com.mola.cargo.model.SortieAerien;
import com.mola.cargo.service.ColisAerienService;
import com.mola.cargo.service.ConvoiService;
import com.mola.cargo.service.SortieAerienService;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SortieAerienController {

    @Autowired
    private SortieAerienService sortieAerienService;
    @Autowired
    private ColisAerienService colisAerienService;
    @Autowired
    private ConvoiService convoiService;

    @GetMapping("/sortieAeriens")
    public String afficherSortie(Model model){
        model.addAttribute("sortieAeriens", sortieAerienService.showSortieAeriens());
        model.addAttribute("colisAeriens", colisAerienService.showColisAerienDepot(Constante.INITIAL));
        model.addAttribute("unConvoi", convoiService.showMaLastConvoiAerien());
        return "sortie/sortieAerien";
    }

    @GetMapping("sortieAerien/colis/{id}")
    public String afficherSortieAdmin(@PathVariable("id") Long id, Model model){
        model.addAttribute("sortieAeriens", sortieAerienService.showSortieColisConvois(id));
        model.addAttribute("unConvoi", convoiService.showOneConvoi(id));
        return "sortie/sortieAerienAdmin";
    }

    @PostMapping("/sortieAerien/nouveau")
    public String enreggistrerSortieAerien(@RequestParam Long identifiant, @RequestParam Long colisAerienid){
        SortieAerien sortieAerien = new SortieAerien();
        sortieAerien.setColisAerienid(colisAerienid);
        sortieAerien.setConvoiid(identifiant);
        sortieAerienService.saveSortieAerien(sortieAerien);
        colisAerienService.updateStatutColisAerien(Constante.SORTIE, colisAerienid);
        return "redirect:/sortieAeriens";
    }

    @GetMapping("sortieAerien/formUpdate/{id}")
    public String afficherFormUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("UneSortieAerien", sortieAerienService.showOneSortieAerien(id));
        model.addAttribute("colisAeriens", colisAerienService.showColisAerien());
        return "sortie/formUpdateSortieAerien";
    }

    @PostMapping("/sortieAerien/update")
    public String updateSortieAerien(@ModelAttribute("SortieAerien") SortieAerien sortieAerien){
        sortieAerienService.saveSortieAerien(sortieAerien);
        return "redirect:/sortieAeriens";
    }

    @GetMapping("sortieAerien/delete")
    public String supprimer(Long id){
        sortieAerienService.deleteSortieAerien(id);
        colisAerienService.updateStatutColisAerien(Constante.INITIAL, id);
        return "redirect:/sortieAeriens";
    }

   /* @GetMapping("sortieAerien/colis/{id}")
    public String afficherColisAerienSortie(@PathVariable("id") Long id, Model model){
        model.addAttribute("sortieAeriens", sortieAerienService.showOneSortieAerien(id));

        return "sortie/colisSortieAerien";
    }*/
}
