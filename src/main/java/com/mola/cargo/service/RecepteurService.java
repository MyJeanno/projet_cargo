package com.mola.cargo.service;

import com.mola.cargo.model.Emetteur;
import com.mola.cargo.model.Recepteur;
import com.mola.cargo.repository.EmetteurRepository;
import com.mola.cargo.repository.RecepteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecepteurService {

    @Autowired
    private RecepteurRepository recepteurRepository;

    public void saveRecepteur(Recepteur recepteur){
        recepteurRepository.save(recepteur);
    }

    public List<Recepteur> showRecepteur(){
        return recepteurRepository.findAll();
    }

    public Recepteur showOneRecepteur(Long id){
        Optional<Recepteur> optional = recepteurRepository.findById(id);
        Recepteur recepteur = null;
        if(optional.isPresent()){
            recepteur = optional.get();
        }else{
            throw new RuntimeException("Id introuvable");
        }
        return recepteur;
    }

    public void deleteRecepteur(Long id){
        recepteurRepository.deleteById(id);
    }


}
