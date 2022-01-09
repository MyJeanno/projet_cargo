package com.mola.cargo.service;

import com.mola.cargo.model.Augmentation;
import com.mola.cargo.model.Pays;
import com.mola.cargo.repository.AugmentationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AugmentationService {

    @Autowired
    private AugmentationRepository augmentationRepository;

    public List<Augmentation> showAugmentation(){
        return augmentationRepository.findAll();
    }

    public void saveAugmentation(Augmentation augmentation){
        augmentationRepository.save(augmentation);
    }

    public Augmentation showOneAugmentation(Long id){
        Optional<Augmentation> optional = augmentationRepository.findById(id);
        Augmentation augmentation = null;
        if(optional.isPresent()){
            augmentation = optional.get();
        }else{
            throw new RuntimeException("Id introuvable");
        }
        return augmentation;
    };

    public void deleteAugmentation(Long id){
        augmentationRepository.deleteById(id);
    }
}
