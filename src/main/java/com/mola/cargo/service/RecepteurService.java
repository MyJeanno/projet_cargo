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

    public String numeroClient(Recepteur recepteur, String chaine){
        return recepteur.getINITIAL_ENTREPRISE()+""+chaine.substring(0,3)+recepteurRepository.count();
    }

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

    public void updateSoldeClient(double solde, Long id){
        recepteurRepository.updateSoldeClient(solde, id);
    }

    public void updateSoldeClientEncaissement(double montant, Long id){
        recepteurRepository.updateSoldeClientEncaissement(montant, id);
    }

    public List<Recepteur> clientSelonEtat(Long id){
        return recepteurRepository.findByEtatid(id);
    }

    public List<Recepteur> clientSelonPays(Long id){
        return recepteurRepository.findByPaysid(id);
    }

    public Recepteur showMonDernierRecepteur(Long id){
        return recepteurRepository.showMaLastRecepteur(id);
    }

    public void updateInfoRecepteur(String etat, Long id){
        recepteurRepository.updateInfoRecepteur(etat, id);
    }

    public boolean testerAppartenance(List<Recepteur> liste, String pin){
        boolean appartient = false;
        for (Recepteur r:liste){
            if (r.getNumeroPersonne().equals(pin)){
                appartient = true;
            }else{
                appartient = false;
            }
        }
        return appartient;
    }
}
