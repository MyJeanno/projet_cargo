package com.mola.cargo.service;

import com.mola.cargo.model.TarifAerien;
import com.mola.cargo.repository.TarifAerienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarifAerienService {

    @Autowired
    private TarifAerienRepository tarifAerienRepository;

    public void saveTarifAerien(TarifAerien tarifAerien){
        tarifAerienRepository.save(tarifAerien);
    }

    public List<TarifAerien> showAllTarifAerien(){
        return tarifAerienRepository.findAll();
    }

    public TarifAerien showOneTarifAerien(Long id){
        return tarifAerienRepository.findById(id).get();
    }

    public void deleteTarifAerien(Long id){
        tarifAerienRepository.deleteById(id);
    }

    public TarifAerien leTarifaerien(){
        return tarifAerienRepository.letarifAerien();
    }
}
