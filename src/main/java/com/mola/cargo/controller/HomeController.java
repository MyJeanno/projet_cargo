package com.mola.cargo.controller;

import com.mola.cargo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private EmetteurService emetteurService;
    @Autowired
    private CommandeService commandeService;
    @Autowired
    private ColisAerienService colisAerienService;
    @Autowired
    private ColisMaritimeService colisMaritimeService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("nbClient", emetteurService.nbreEmetteur());
        model.addAttribute("totalCommande", commandeService.totalCommande());
        model.addAttribute("nbCommandeAerien", colisAerienService.nombreColisAerien());
        model.addAttribute("nbCommandeMaritime", colisMaritimeService.nombreCommandeMaritime());
        return "home/home";
    }
}
