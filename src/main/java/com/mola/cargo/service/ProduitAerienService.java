package com.mola.cargo.service;

import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.Produit;
import com.mola.cargo.model.ProduitAerien;
import com.mola.cargo.repository.ProduitAerienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProduitAerienService {
    @Autowired
    private ProduitAerienRepository produitAerienRepository;

    public List<ProduitAerien> showProduitsAerien(){
        return produitAerienRepository.findAll();
    }

    public void saveProduitAerien(ProduitAerien produitAerien){
        produitAerienRepository.save(produitAerien);
    }

    public ProduitAerien showOneProduitAerien(Long id){
        Optional<ProduitAerien> optional = produitAerienRepository.findById(id);
        ProduitAerien produitAerien = null;
        if(optional.isPresent()){
            produitAerien = optional.get();
        }else {
            throw new RuntimeException("Id introuvable");
        }
        return produitAerien;
    }

    public List<ProduitAerien> findProduitColisAerien(Long id){
        return produitAerienRepository.findProduitColisAerien(id);
    }

    public int nbreColisAerien(Long id){
        return produitAerienRepository.nbreColisAerien(id);
    }

    public void deleteProduitAerien(Long id){
        produitAerienRepository.deleteById(id);
    }

    public boolean estentier(double n){
        if(n==(int)n)
            return true;
        return false;
    }

    public double taxe(List<ProduitAerien> listeAerien) {
        double taxe = 0;
        for (ProduitAerien p : listeAerien) {
            if (p.getTarif().getTaxe() != 0) {
                taxe = p.getTarif().getTaxe();
                break;
            }
        }
        return taxe;
    }

    public double fraisEmballage(List<ColisAerien> listeca){
        double montant=0;
        for (ColisAerien ca:listeca){
            montant = montant + ca.getEmballage().getPrix();
        }
        return montant;
    }
}
