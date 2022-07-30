package com.mola.cargo.service;

import com.mola.cargo.model.SortieAerien;
import com.mola.cargo.model.SortieMaritime;
import com.mola.cargo.repository.SortieAerienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public int nbreTotalColisAerien(){
        return sortieAerienRepository.findAll().size();
    }

    public int colisAerienSelonMois(int mois){
        List<SortieAerien> listeMois = new ArrayList<>();
        for (SortieAerien sa : sortieAerienRepository.findAll()){
            if(sa.getConvoi().getDateCreation().getMonthValue()==mois){
                listeMois.add(sa);
            }
        }
        return listeMois.size();
    }

    public List<SortieAerien> sortieAerienByCommande(){
        return sortieAerienRepository.sortieAerienByCommande();
    }

    public boolean existeDeja(List<SortieAerien> liste, Long id){
        boolean existe = false;
        for (SortieAerien sa : liste){
            if(sa.getColisAerien().getCommande().getId()==id){
                existe = true;
            }else {
                existe = false;
            }
        }
        return existe;
    }

    public List<SortieAerien> listeCommandeAerien(Long id){
        List<SortieAerien> liste = new ArrayList<>();
        for (SortieAerien sa : sortieAerienRepository.listeCommande(id)){
            if (!existeDeja(liste, sa.getColisAerien().getCommande().getId())){
               liste.add(sa);
            }
        }
        return liste;
    }

    public Double poidsTotalColisAerienLot(Long id){
        if(sortieAerienRepository.poidsTotalColisAerienLot(id)==null){
            return 0.0;
        }else{
            return sortieAerienRepository.poidsTotalColisAerienLot(id);
        }
    }

    public List<String> PoidsParCategorieAlimentaire(Long id){
        List<String> listeDefault = new ArrayList<>();
        listeDefault = sortieAerienRepository.PoidsParCategorieAlimentaire(id);
        if(listeDefault.isEmpty()){
            listeDefault.add("0");
        }
        return listeDefault;
    }
}
