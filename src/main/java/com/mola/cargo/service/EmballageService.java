package com.mola.cargo.service;

import com.mola.cargo.model.Emballage;
import com.mola.cargo.repository.EmballageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmballageService {

    @Autowired
    private EmballageRepository emballageRepository;

    public List<Emballage> showEmballages(){
        return emballageRepository.findAll();
    }

    public void saveEmballage(Emballage emballage){
        emballageRepository.save(emballage);
    }

    public Emballage showOneEmballage(Long id){
        Optional<Emballage> optional = emballageRepository.findById(id);
        Emballage emballage = null;
        if(optional.isPresent()){
            emballage = optional.get();
        }else{
            throw new RuntimeException("Id introuvable");
        }
        return emballage;
    }

    public void deleteEmballage(Long id){
        emballageRepository.deleteById(id);
    }
}
