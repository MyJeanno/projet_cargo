package com.mola.cargo.controller;

import com.mola.cargo.model.Augmentation;
import com.mola.cargo.model.Pays;
import com.mola.cargo.service.AugmentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AugmentationController {
    @Autowired
    private AugmentationService augmentationService;

    @GetMapping("/augmentations")
    public String afficherAugmentation(Model model){
        model.addAttribute("augmentations", augmentationService.showAugmentation());
        return "augmentation/augmentation";
    }

    @PostMapping("augmentation/nouveau")
    public String enregistrerAugmentation(@RequestParam double pourcent){
        Augmentation augmentation = new Augmentation();
        augmentation.setPourcentage(pourcent);
        augmentationService.saveAugmentation(augmentation);
        return "redirect:/augmentations";
    }

    @GetMapping("augmentation/formUpdate/{id}")
    public String showFormEditAugmentation(@PathVariable("id") Long id, Model model){
        model.addAttribute("uneAugmentation", augmentationService.showOneAugmentation(id));
        return "augmentation/formUpdateAugmentation";
    }

    @PostMapping("augmentation/update")
    public String updateAugmentation(@ModelAttribute("augmentation") Augmentation augmentation){
        augmentationService.saveAugmentation(augmentation);
        return "redirect:/augmentations";
    }

    @GetMapping("/augmentation/delete")
    public String supprimerAugmentation(Long id){
        augmentationService.deleteAugmentation(id);
        return "redirect:/augmentations";
    }
}
