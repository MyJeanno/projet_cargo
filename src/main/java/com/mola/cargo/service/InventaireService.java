package com.mola.cargo.service;

import com.mola.cargo.model.Inventaire;
import com.mola.cargo.repository.InventaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventaireService {

    @Autowired
    private InventaireRepository inventaireRepository;

    public void saveInventaire(Inventaire inventaire){
        inventaireRepository.save(inventaire);
    }

    public List<Inventaire> showInventaires(){
        return inventaireRepository.findAll();
    }

    public void updateStatutInventaire(String s, Long id){
        inventaireRepository.updateStatutInventaire(s, id);
    }

    public List<Inventaire> showInventaireSelonStatut(String s, String lieu){
        return inventaireRepository.findByStatusAndByLieu(s, lieu);
    }

    public Double sommeFactureNonEncaisse(String s, String lieu){
        if(inventaireRepository.sommeFactureNonEncaisse(s, lieu) == null){
            return 0.0;
        }else {
            return inventaireRepository.sommeFactureNonEncaisse(s, lieu);
        }
    }
}