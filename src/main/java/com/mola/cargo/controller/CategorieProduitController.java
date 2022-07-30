package com.mola.cargo.controller;

import com.mola.cargo.model.CategorieProduit;
import com.mola.cargo.service.CategorieProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/param")
public class CategorieProduitController {

    @Autowired
    private CategorieProduitService categorieProduitService;

    @GetMapping("/produit/categories")
    public String listerCategorie(Model model){
        model.addAttribute("categories", categorieProduitService.showAllCategorieProduit());
        return "produit/listeCategories";
    }

    @PostMapping("/categorie/nouveau")
    public String enregistrerCategorie(@RequestParam String categorie){
        CategorieProduit categorieProduit = new CategorieProduit();
        categorieProduit.setNomCategorie(categorie);
        categorieProduitService.saveCategorieProduit(categorieProduit);
        return "redirect:/param/produit/categories";
    }

    @GetMapping("/categorie/formUpdate/{id}")
    public String showFormUpdateCategorie(@PathVariable("id") Long id, Model model){
        model.addAttribute("uneCategorie", categorieProduitService.showOneCategorieProduit(id));
        return "produit/formUpdateCategorie";
    }

    @PostMapping("/update/categorie")
    public String updateCategorieProduit(@ModelAttribute("categorieProduit") CategorieProduit categorieProduit ){
        categorieProduitService.saveCategorieProduit(categorieProduit);
        return "redirect:/param/produit/categories";
    }

    @GetMapping("/categorie/delete")
    public String supprimerCategorieProduit(Long id){
        categorieProduitService.deleteCategorieProduit(id);
        return "redirect:/param/produit/categories";
    }
}
