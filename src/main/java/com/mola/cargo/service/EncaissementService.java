package com.mola.cargo.service;

import com.mola.cargo.model.Encaissement;
import com.mola.cargo.repository.EncaissementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EncaissementService {

    @Autowired
    private EncaissementRepository encaissementRepository;

    public void saveEncaissement(Encaissement encaissement){
        encaissementRepository.save(encaissement);
    }

    public List<Encaissement> showEncaissement(){
        return encaissementRepository.findAll();
    }

    public Encaissement showOneEncaissement(Long id){
        return encaissementRepository.findById(id).get();
    }

    public void deleteEncaissement(Long id){
        encaissementRepository.deleteById(id);
    }
}
