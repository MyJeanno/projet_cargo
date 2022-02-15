package com.mola.cargo.service;

import com.mola.cargo.model.Colis;
import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.ColisMaritime;
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

    public List<ColisAerien> showColis(){
        return colisAerienRepository.findAll();
    }

    public List<ColisAerien> showColisAerienCommande(Long id){
        return colisAerienRepository.findColisAerienCommande(id);
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
