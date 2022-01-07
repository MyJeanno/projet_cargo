package com.mola.cargo.service;

import com.mola.cargo.model.Paiement;
import com.mola.cargo.model.Piece;
import com.mola.cargo.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    public void savePaiement(Paiement paiement){
        paiementRepository.save(paiement);
    }

    public List<Paiement> showPaiement(){
        return paiementRepository.findAll();
    }

    public Paiement showOnePaiement(Long id){
        Optional<Paiement> optional = paiementRepository.findById(id);
        Paiement paiement = null;
        if(optional.isPresent()){
            paiement = optional.get();
        }else{
            throw new RuntimeException("Id introuvable");
        }
        return paiement;
    }

    public void deletePaiement(Long id){
        paiementRepository.deleteById(id);
    }
}
