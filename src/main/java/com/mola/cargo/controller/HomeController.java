package com.mola.cargo.controller;

import com.mola.cargo.service.*;
import com.mola.cargo.util.Constante;
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
import java.util.*;

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
    @Autowired
    private SortieMaritimeService sortieMaritimeService;
    @Autowired
    private SortieAerienService sortieAerienService;

    @GetMapping("/stat/envois")
    public String statEnvoiCommande(){
        return "statistique/statEnvoi";
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("nbClient", emetteurService.nbreEmetteur());
        model.addAttribute("totalCommande", commandeService.totalCommande());
        model.addAttribute("nbCommandeAerien", colisAerienService.nombreColisAerien());
        model.addAttribute("nbCommandeMaritime", colisMaritimeService.nombreCommandeMaritime());
        //Envoi des paramètres des mois pour le graphique
        model.addAttribute("janvierA", sortieAerienService.colisAerienSelonMois(Constante.MOIS_JANVIER));
        model.addAttribute("janvierM", sortieMaritimeService.colisMaritimeSelonMois(Constante.MOIS_JANVIER));

        model.addAttribute("fevrierA", sortieAerienService.colisAerienSelonMois(Constante.MOIS_FEVRIER));
        model.addAttribute("fevrierM", sortieMaritimeService.colisMaritimeSelonMois(Constante.MOIS_FEVRIER));

        model.addAttribute("marsA", sortieAerienService.colisAerienSelonMois(Constante.MOIS_MARS));
        model.addAttribute("marsM", sortieMaritimeService.colisMaritimeSelonMois(Constante.MOIS_MARS));

        model.addAttribute("avrilA", sortieAerienService.colisAerienSelonMois(Constante.MOIS_AVRIL));
        model.addAttribute("avrilM", sortieMaritimeService.colisMaritimeSelonMois(Constante.MOIS_AVRIL));

        model.addAttribute("maiA", sortieAerienService.colisAerienSelonMois(Constante.MOIS_MAI));
        model.addAttribute("maiM", sortieMaritimeService.colisMaritimeSelonMois(Constante.MOIS_MAI));

        model.addAttribute("juinA", sortieAerienService.colisAerienSelonMois(Constante.MOIS_JUIN));
        model.addAttribute("juinM", sortieMaritimeService.colisMaritimeSelonMois(Constante.MOIS_JUIN));

        model.addAttribute("juilletA", sortieAerienService.colisAerienSelonMois(Constante.MOIS_JUILLET));
        model.addAttribute("juilletM", sortieMaritimeService.colisMaritimeSelonMois(Constante.MOIS_JUILLET));

        model.addAttribute("aoutA", sortieAerienService.colisAerienSelonMois(Constante.MOIS_AOUT));
        model.addAttribute("aoutM", sortieMaritimeService.colisMaritimeSelonMois(Constante.MOIS_AOUT));

        model.addAttribute("septembreA", sortieAerienService.colisAerienSelonMois(Constante.MOIS_SEPTEMBRE));
        model.addAttribute("septembreM", sortieMaritimeService.colisMaritimeSelonMois(Constante.MOIS_SEPTEMBRE));

        model.addAttribute("octobreA", sortieAerienService.colisAerienSelonMois(Constante.MOIS_OCTOBRE));
        model.addAttribute("octobreM", sortieMaritimeService.colisMaritimeSelonMois(Constante.MOIS_OCTOBRE));

        model.addAttribute("novembreA", sortieAerienService.colisAerienSelonMois(Constante.MOIS_NOVEMBRE));
        model.addAttribute("novembreM", sortieMaritimeService.colisMaritimeSelonMois(Constante.MOIS_NOVEMBRE));

        model.addAttribute("decembreA", sortieAerienService.colisAerienSelonMois(Constante.MOIS_DECEMBRE));
        model.addAttribute("decembreM", sortieMaritimeService.colisMaritimeSelonMois(Constante.MOIS_DECEMBRE));

        return "home/home";
    }
}
