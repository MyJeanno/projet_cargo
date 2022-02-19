package com.mola.cargo.service;

import com.mola.cargo.model.Commande;
import com.mola.cargo.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    public int genererNbre(int borneInf, int borneSup){
        Random random = new Random();
        return (borneInf+random.nextInt(borneSup-borneInf));
    }

    public boolean testerAppartenance(List<Commande> liste, String pin){
        boolean appartient = false;
        for (Commande c:liste){
            if (c.getPin().equals(pin)){
                appartient = true;
            }else{
                appartient = false;
            }
        }
        return appartient;
    }

    public Commande showMaLastCommande(){
        return commandeRepository.showMaLastCommande();
    }
    public void saveCommande(Commande commande){
        commandeRepository.save(commande);
    }
    public List<Commande> showCommande(){
        return commandeRepository.findAll();
    }
}
