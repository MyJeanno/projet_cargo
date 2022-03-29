package com.mola.cargo.service;

import com.mola.cargo.model.SortieAerien;
import com.mola.cargo.model.SortieMaritime;
import com.mola.cargo.repository.SortieAerienRepository;
import com.mola.cargo.repository.SortieMaritimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SortieMaritimeService {

    @Autowired
    private SortieMaritimeRepository sortieMaritimeRepository;

    public void saveSortieMaritime(SortieMaritime sortieMaritime){
        sortieMaritimeRepository.save(sortieMaritime);
    }

    public List<SortieMaritime> showSortieMaritimes(){
        return sortieMaritimeRepository.findAll();
    }

    public SortieMaritime showOneSortieMaritime(Long id){
        Optional<SortieMaritime> optional = sortieMaritimeRepository.findById(id);
        SortieMaritime sortieMaritime = null;
        if(optional.isPresent()){
            sortieMaritime = optional.get();
        }else {
            throw new RuntimeException("Id introuvable");
        }
        return sortieMaritime;
    }

    public void deleteSortieMaritime(Long id){
        sortieMaritimeRepository.deleteById(id);
    }

    public List<SortieMaritime> showSortieColisMaritimeConvois(Long id){
        return sortieMaritimeRepository.findByConvoiid(id);
    }

    public int colisMaritimeSelonMois(int mois){
        List<SortieMaritime> listeMois = new ArrayList<>();
        for (SortieMaritime sm : sortieMaritimeRepository.findAll()){
            if(sm.getConvoi().getDateCreation().getMonthValue()==mois){
                listeMois.add(sm);
            }
        }
        return listeMois.size();
    }
}
