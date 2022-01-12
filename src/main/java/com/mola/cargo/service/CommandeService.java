package com.mola.cargo.service;

import com.mola.cargo.model.Commande;
import com.mola.cargo.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    public void saveCommande(Commande commande){
        commandeRepository.save(commande);
    }
    public List<Commande> showCommande(){
        return commandeRepository.findAll();
    }
}
