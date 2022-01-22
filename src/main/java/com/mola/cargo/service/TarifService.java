package com.mola.cargo.service;

import com.mola.cargo.model.Tarif;
import com.mola.cargo.repository.TarifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarifService {

    @Autowired
    private TarifRepository tarifRepository;

    public void saveTarif(Tarif tarif){
        tarifRepository.save(tarif);
    }

    public List<Tarif> showTarifs(){
        return tarifRepository.findAll();
    }

    public Tarif showOneTarif(Long id){
        Optional<Tarif> optional = tarifRepository.findById(id);
        Tarif tarif = null;
        if(optional.isPresent()){
            tarif = optional.get();
        }else {
            throw new RuntimeException("Id introuvable");
        }
        return tarif;
    }

    public void deleteTarif(Long id){
        tarifRepository.deleteById(id);
    }
}
