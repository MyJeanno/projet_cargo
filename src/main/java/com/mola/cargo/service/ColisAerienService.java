package com.mola.cargo.service;

import com.mola.cargo.model.Colis;
import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.ColisMaritime;
import com.mola.cargo.model.Commande;
import com.mola.cargo.repository.ColisAerienRepository;
import com.mola.cargo.repository.ColisMaritimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColisAerienService {

    @Autowired
    private ColisAerienRepository colisAerienRepository;

    public void saveColisAerien(ColisAerien colisAerien){
        colisAerienRepository.save(colisAerien);
    }

    public List<ColisAerien> showColisAerien(){
        return colisAerienRepository.findAll();
    }

    public List<ColisAerien> showColisAerienCommande(Long id){
        return colisAerienRepository.findColisAerienCommande(id);
    }

    public List<ColisAerien> findColisAerienCommandePin(String pin){
        return colisAerienRepository.findColisAerienCommandePin(pin);
    }

    public int nbreColisAerien(Long id){
        return colisAerienRepository.nbreColisAerien(id);
    }

    public ColisAerien showMaLastColisAerien(){
        return colisAerienRepository.showMaLastColisAerien();
    }

    public int nombreColisAerien(){
        return colisAerienRepository.nbreCommandeAerien();
    }

    public boolean testerAppartenance(List<ColisAerien> liste, String num){
        boolean appartient = false;
        for (ColisAerien c:liste){
            if (c.getNumeroColis().equals(num)){
                appartient = true;
            }else{
                appartient = false;
            }
        }
        return appartient;
    }

    public Colis showOneColisAerien(Long id){
        Optional<ColisAerien> optional = colisAerienRepository.findById(id);
        ColisAerien colisAerien = null;
        if(optional.isPresent()){
            colisAerien = optional.get();
        }else {
            throw new RuntimeException("Id introuvable");
        }
        return colisAerien;
    }

    public void deleteColisAerien(Long id){
        colisAerienRepository.deleteById(id);
    }
}
