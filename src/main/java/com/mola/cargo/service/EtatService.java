package com.mola.cargo.service;

import com.mola.cargo.model.Etat;
import com.mola.cargo.repository.EtatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EtatService {

    @Autowired
    private EtatRepository etatRepository;

    public void saveEtat(Etat etat){
        etatRepository.save(etat);
    }

    public List<Etat> showStates(){
        return etatRepository.findAll();
    }

    public Etat showOneState(Long id){
        Optional<Etat> optional = etatRepository.findById(id);
        Etat etat = null;
        if(optional.isPresent()){
            etat = optional.get();
        }else{
            throw new RuntimeException("id introuvable");
        }
        return etat;
    }

    public void deleteState(Long id){
        etatRepository.deleteById(id);
    }
}
