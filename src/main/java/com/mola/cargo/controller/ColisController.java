package com.mola.cargo.controller;

import com.mola.cargo.model.Colis;
import com.mola.cargo.service.ColisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ColisController {

    @Autowired
    private ColisService colisService;

    @PostMapping("/colis/nouveau")
    public String enregistrerColis(Colis colis){
        colisService.saveColis(colis);
        return "colis/formColis";
    }

}
