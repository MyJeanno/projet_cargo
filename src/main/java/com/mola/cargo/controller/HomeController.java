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

    @GetMapping("/stat/envois")
    public String statEnvoiCommande(){
        return "statistique/statEnvoi";
    }

    @GetMapping("/pieChart")
    public String pieChart(Model model) {
        model.addAttribute("pass", 90);
        model.addAttribute("fail", 10);
        return "piechart";

    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("nbClient", emetteurService.nbreEmetteur());
        model.addAttribute("totalCommande", commandeService.totalCommande());
        model.addAttribute("nbCommandeAerien", colisAerienService.nombreColisAerien());
        model.addAttribute("nbCommandeMaritime", colisMaritimeService.nombreCommandeMaritime());

       /* Map<String, Integer> nombre_colis = new HashMap<>();
        nombre_colis.put("Janvier", 5);
        nombre_colis.put("Fevrier", 20);
        nombre_colis.put("Mars", 18);
        nombre_colis.put("Avril", 25);
        nombre_colis.put("Mai", 14);
        nombre_colis.put("Juin", 11);
        nombre_colis.put("Juillet", 7);
        nombre_colis.put("Août", 48);
        nombre_colis.put("Septembre", 3);
        nombre_colis.put("Octobre", 20);
        nombre_colis.put("Novembre", 4);
        nombre_colis.put("Décembre", 15);
        model.addAttribute("nombre_colis", nombre_colis);
       Map<String, Integer> surveyMap = new LinkedHashMap<>();
        surveyMap.put("Java", 40);
        surveyMap.put("Dev oops", 25);
        surveyMap.put("Python", 20);
        surveyMap.put(".Net", 15);
        model.addAttribute("surveyMap", surveyMap);
        return "home/home";

        //first, add the regional sales
        Integer northeastSales = 17089;
        Integer westSales = 10603;
        Integer midwestSales = 5223;
        Integer southSales = 10111;

        model.addAttribute("northeastSales", northeastSales);
        model.addAttribute("southSales", southSales);
        model.addAttribute("midwestSales", midwestSales);
        model.addAttribute("westSales", westSales);

        //now add sales by lure type
        List<Integer> inshoreSales = Arrays.asList(4074, 3455, 4112);
        List<Integer> nearshoreSales = Arrays.asList(3222, 3011, 3788);
        List<Integer> offshoreSales = Arrays.asList(7811, 7098, 6455);

        model.addAttribute("inshoreSales", inshoreSales);
        model.addAttribute("nearshoreSales", nearshoreSales);
        model.addAttribute("offshoreSales", offshoreSales);
        Map<String, Integer> data = new LinkedHashMap<>();
        data.put("Lomé", 20);
        data.put("Kara", 60);
        data.put("Atakpamé", 14);
        data.put("Sokodé", 45);
        data.put("Kpalimé", 15);
        model.addAttribute("keyset", data.keySet());
        model.addAttribute("values", data.values());*/
        return "home/home";
    }
}
