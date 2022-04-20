package com.mola.cargo.controller;

import com.mola.cargo.model.Transport;
import com.mola.cargo.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/param")
public class TransportController {

    @Autowired
    private TransportService transportService;

    @GetMapping("/transports")
    public String afficherTransport(Model model){
        model.addAttribute("transports", transportService.showAllTransports());
        return "transport/transport";
    }

    @PostMapping("transport/nouveau")
    public String enregistrer(@RequestParam double inf, @RequestParam double sup, @RequestParam double prix){
        Transport transport = new Transport();
        transport.setInf(inf);
        transport.setSup(sup);
        transport.setPrix(prix);
        transportService.saveTransport(transport);
        return "redirect:/param/transports";
    }

    @GetMapping("/transport/formUpdate/{id}")
    public String formUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("unTransport", transportService.showOneTransport(id));
        return "transport/formUpdate";
    }

    @PostMapping("/transport/update")
    public String update(@ModelAttribute("transport") Transport transport){
        transportService.saveTransport(transport);
        return "redirect:/param/transports";
    }

    @GetMapping("/transport/delete/")
    public String supprimer(Long id){
        transportService.deleteTransport(id);
        return "redirect:/param/transports";
    }
}
