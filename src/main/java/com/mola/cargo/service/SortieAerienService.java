package com.mola.cargo.service;

import com.mola.cargo.model.SortieAerien;
import com.mola.cargo.repository.SortieAerienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SortieAerienService {

    @Autowired
    private SortieAerienRepository sortieAerienRepository;

    public void saveSortieAerien(SortieAerien sortieAerien){
        sortieAerienRepository.save(sortieAerien);
    }

    public List<SortieAerien> showSortieAeriens(){
        return sortieAerienRepository.findAll();
    }

    public SortieAerien showOneSortieAerien(Long id){
        Optional<SortieAerien> optional = sortieAerienRepository.findById(id);
        SortieAerien sortieAerien = null;
        if(optional.isPresent()){
            sortieAerien = optional.get();
        }else {
            throw new RuntimeException("Id introuvable");
        }
        return sortieAerien;
    }

    public void deleteSortieAerien(Long id){
        sortieAerienRepository.deleteById(id);
    }

    public List<SortieAerien> showSortieColisConvois(Long id){
        return sortieAerienRepository.findByConvoiid(id);
    }
}
