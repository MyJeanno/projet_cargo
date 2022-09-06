package com.mola.cargo.service;

import com.mola.cargo.model.CommandeCarton;
import com.mola.cargo.repository.CommandeCartonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeCartonService {

    @Autowired
    private CommandeCartonRepository commandeCartonRepository;

    public void saveCartonCommande(CommandeCarton commandeCarton){
        commandeCartonRepository.save(commandeCarton);
    }

    public List<CommandeCarton> showAllCommandeCarton(){
        return commandeCartonRepository.findAll();
    }

    public CommandeCarton showOneCommandeCarton(Long id){
        return commandeCartonRepository.findById(id).get();
    }

    public void deleteCommandeCarton(Long id){
        commandeCartonRepository.deleteById(id);
    }

    public List<CommandeCarton> listerCommandeCartonEncours(){
        return commandeCartonRepository.commandeVente();
    }

    public CommandeCarton showLastCommandeCarton(){
        return commandeCartonRepository.lastCommandeCarton();
    }

    public void updateCommandeCartonAppro(Long id, int qte){
        commandeCartonRepository.updateCommandeCartonAppro(id, qte);
    }

    public void updateCommandeCartonVente(Long id, int qte){
        commandeCartonRepository.updateCommandeCartonVente(id, qte);
    }


}
