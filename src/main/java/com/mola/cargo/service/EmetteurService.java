package com.mola.cargo.service;

import com.mola.cargo.model.Emetteur;
import com.mola.cargo.model.Personne;
import com.mola.cargo.model.Piece;
import com.mola.cargo.repository.EmetteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmetteurService {

    @Autowired
    private EmetteurRepository emetteurRepository;

    public String numeroClient(Emetteur emetteur, String chaine){
        return emetteur.getINITIAL_ENTREPRISE()+""+chaine.substring(0,3)+emetteurRepository.count();
    }

    public void saveEmetteur(Emetteur emetteur){
        emetteurRepository.save(emetteur);
    }

    public List<Emetteur> showEmetteur(){
        return emetteurRepository.findAll();
    }

    public Emetteur showOneEmetteur(Long id){
        Optional<Emetteur> optional = emetteurRepository.findById(id);
        Emetteur emetteur = null;
        if(optional.isPresent()){
            emetteur = optional.get();
        }else{
            throw new RuntimeException("Id introuvable");
        }
        return emetteur;
    }

    public void deleteEmetteur(Long id){
        emetteurRepository.deleteById(id);
    }
}
