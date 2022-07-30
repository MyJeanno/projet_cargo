package com.mola.cargo.service;

import com.mola.cargo.model.Commande;
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

    public boolean testerAppartenance(List<Emetteur> liste, String pin){
        boolean appartient = false;
        for (Emetteur e:liste){
            if (e.getNumeroPersonne().equals(pin)){
                appartient = true;
            }else{
                appartient = false;
            }
        }
        return appartient;
    }

    public void saveEmetteur(Emetteur emetteur){
        emetteurRepository.save(emetteur);
    }

    public List<Emetteur> showEmetteur(){
        return emetteurRepository.findAll();
    }

    public int nbreEmetteur(){
        return emetteurRepository.findAll().size();
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

    public Emetteur showMonDernierEmetteur(Long id){
        return emetteurRepository.showMaLastEmetteur(id);
    }

    public void updateInfoEmetteur(String etat, Long id){
        emetteurRepository.updateInfoEmetteur(etat, id);
    }
}
