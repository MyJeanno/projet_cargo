package com.mola.cargo.service;

import com.mola.cargo.model.Colis;
import com.mola.cargo.model.ColisMaritime;
import com.mola.cargo.repository.ColisMaritimeRepository;
import com.mola.cargo.repository.ColisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColisMaritimeService {

    @Autowired
    private ColisMaritimeRepository colisMaritimeRepository;

    public void saveColisMaritime(ColisMaritime colisMaritime){
        colisMaritimeRepository.save(colisMaritime);
    }

    public List<ColisMaritime> showColisMaritime(){
        return colisMaritimeRepository.findAll();
    }

    public List<ColisMaritime> showColisMaritimeCommande(Long id){
        return colisMaritimeRepository.findColisMaritimeCommande(id);
    }

    public Colis showOneColisMaritime(Long id){
        Optional<ColisMaritime> optional = colisMaritimeRepository.findById(id);
        ColisMaritime colisMaritime = null;
        if(optional.isPresent()){
            colisMaritime = optional.get();
        }else {
            throw new RuntimeException("Id introuvable");
        }
        return colisMaritime;
    }

    public void deleteColisMaritime(Long id){
        colisMaritimeRepository.deleteById(id);
    }
}
