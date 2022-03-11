package com.mola.cargo.service;

import com.mola.cargo.model.Pays;
import com.mola.cargo.repository.PaysRepository;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaysService {

    @Autowired
    private PaysRepository paysRepository;

    public List<Pays> showPays(){
        return paysRepository.findAll();
    }

    public List<Pays> findPaysTarif(){
        return paysRepository.findPaysTarif();
    }

    public void savePays(Pays pays){
        paysRepository.save(pays);
    }

    public Pays showOnePays(Long id){
        Optional<Pays> optional = paysRepository.findById(id);
        Pays pays = null;
        if(optional.isPresent()){
            pays = optional.get();
        }else{
            throw new RuntimeException("Id introuvable");
        }
        return pays;
    };

    public void deletePays(Long id){
        paysRepository.deleteById(id);
    }


}
