package com.mola.cargo.service;

import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.ColisMaritime;
import com.mola.cargo.model.ProduitAerien;
import com.mola.cargo.model.ProduitMaritime;
import com.mola.cargo.repository.ProduitMaritimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitMaritimeService {

    @Autowired
    private ProduitMaritimeRepository produitMaritimeRepository;

    public List<ProduitMaritime> showProduitsMaritime(){
        return produitMaritimeRepository.findAll();
    }

    public void saveProduitMaritime(ProduitMaritime produitMaritime){
        produitMaritimeRepository.save(produitMaritime);
    }

    public ProduitMaritime showOneProduitMaritime(Long id){
        Optional<ProduitMaritime> optional = produitMaritimeRepository.findById(id);
        ProduitMaritime produitMaritime = null;
        if(optional.isPresent()){
           produitMaritime = optional.get();
        }else{
            throw new RuntimeException("Id introuvable");
        }
        return produitMaritime;
    }

    public void deleteProduitMaritime(Long id){
        produitMaritimeRepository.deleteById(id);
    }

    public List<ProduitMaritime> findProduitColisMaritime(Long id){
        return produitMaritimeRepository.findProduitColisMaritime(id);
    }

    public int nbreColisMaritimeBis(List<ProduitMaritime> listeMaritime){
        int n = 0;
        for (ProduitMaritime p:listeMaritime){
            if(p.getColisMaritime().getCarton().getCode() == "PC"){
                n++;
            }
        }
        return n;
    }

    public double taxe(List<ProduitMaritime> listeMaritime) {
        double taxe = 0;
        for (ProduitMaritime p : listeMaritime) {
            if (p.getTarif().getTaxeMaritime() != 0) {
                taxe = p.getTarif().getTaxeMaritime();
                break;
            }
        }
        return taxe;
    }

}
