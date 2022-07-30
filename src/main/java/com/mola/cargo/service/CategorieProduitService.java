package com.mola.cargo.service;

import com.mola.cargo.model.CategorieProduit;
import com.mola.cargo.repository.CategorieProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieProduitService {

    @Autowired
    private CategorieProduitRepository categorieProduitRepository;

    public void saveCategorieProduit(CategorieProduit categorieProduit){
        categorieProduitRepository.save(categorieProduit);
    }

    public List<CategorieProduit> showAllCategorieProduit(){
        return categorieProduitRepository.findAll();
    }

    public CategorieProduit showOneCategorieProduit(Long id){
        return categorieProduitRepository.findById(id).get();
    }

    public void deleteCategorieProduit(Long id){
        categorieProduitRepository.deleteById(id);
    }
}
