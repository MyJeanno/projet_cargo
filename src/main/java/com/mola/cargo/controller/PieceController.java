package com.mola.cargo.controller;

import com.mola.cargo.model.Piece;
import com.mola.cargo.service.PieceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/param")
public class PieceController {

    @Autowired
    private PieceService pieceService;

    @GetMapping("/pieces")
    public String listerPiece(Model model){
        model.addAttribute("pieces", pieceService.showPiece());
        return "piece/piece";
    }

    @PostMapping("piece/nouveau")
    public String enregistrerPiece(@RequestParam String lib){
       Piece piece = new Piece();
       piece.setLibellePiece(lib);
       pieceService.savePiece(piece);
       return "redirect:/param/pieces";
    }

    @GetMapping("piece/formUpdate/{id}")
    public String showFormUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("unePiece", pieceService.showOnePiece(id));
        return "piece/pieceFormUpdate";
    }

    @PostMapping("piece/update")
    public String updatePiece(@ModelAttribute("piece") Piece piece){
        pieceService.savePiece(piece);
        return "redirect:/param/pieces";
    }

    @GetMapping("/piece/delete/")
    public String supprimerPiece(Long id){
        pieceService.deletePiece(id);
        return "redirect:/param/pieces";
    }
}
