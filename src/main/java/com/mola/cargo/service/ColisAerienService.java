package com.mola.cargo.service;

import com.mola.cargo.model.*;
import com.mola.cargo.repository.ColisAerienRepository;
import com.mola.cargo.repository.ColisMaritimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColisAerienService {

    @Autowired
    private ColisAerienRepository colisAerienRepository;
    @Autowired
    private ProduitAerienService produitAerienService;

    public void saveColisAerien(ColisAerien colisAerien){
        colisAerienRepository.save(colisAerien);
    }

    public List<ColisAerien> showColisAerien(){
        return colisAerienRepository.findAll();
    }

    public List<ColisAerien> showColisAerienCommande(Long id){
        return colisAerienRepository.findColisAerienCommande(id);
    }

    public List<ColisAerien> findColisAerienCommandePin(String pin){
        return colisAerienRepository.findColisAerienCommandePin(pin);
    }

    public void updatePoidsColisAerien(double poids, double prixKilo, double prixTotal, Long id){
        colisAerienRepository.updatePoidsColisAerien(poids, prixKilo, prixTotal, id);
    }

    public void updateToutColisAerien(double poids, double prixKilo, double prixTotal, double trans, Long id){
        colisAerienRepository.updateToutColisAerien(poids, prixKilo, prixTotal, trans, id);
    }

    public int nbreColisAerien(Long id){
        return colisAerienRepository.nbreColisAerien(id);
    }

    public ColisAerien showMaLastColisAerien(Long id){
        return colisAerienRepository.showMaLastColisAerien(id);
    }

   /* public ColisAerien showMaLastColisAerienCommande(Long id){
        return colisAerienRepository.showMaLastColisAerienCommande(id);
    }*/

    public int nombreColisAerien(){
        return colisAerienRepository.nbreCommandeAerien();
    }

    public Double poidsTotalColisAerien(Long id){
        if(colisAerienRepository.poidsTotalColisAerien(id)==null){
            return 0.0;
        }else{
            return colisAerienRepository.poidsTotalColisAerien(id);
        }
    }

    public Double poidsTotalColisAerienDepot(String statut){
        if(colisAerienRepository.poidsTotalColisAerienDepot(statut)==null){
            return 0.0;
        }else{
            return colisAerienRepository.poidsTotalColisAerienDepot(statut);
        }
    }

    public Double prixTotalColisAerien(Long id){
        if(colisAerienRepository.prixTotalColisAerien(id)==null){
            return 0.0;
        }else{
            return colisAerienRepository.prixTotalColisAerien(id);
        }
    }

    public double appliquerReduction(double montant, double reduction){
        return montant-montant*reduction/100;
    }

    public double arrondirPoids(double d){
        if(d-(int)d == 0.0){
            return d;
        }else if(d-(int)d<=0.5){
            return (int)d + 0.5;
        }else{
            return (int)d + 1;
        }
    }

    public Double prixTransportColisAerien(Long id){
        if(colisAerienRepository.prixTransportColisAerien(id)==null){
            return 0.0;
        }else{
            return colisAerienRepository.prixTransportColisAerien(id);
        }
    }



    public boolean testerAppartenance(List<ColisAerien> liste, String num){
        boolean appartient = false;
        for (ColisAerien c:liste){
            if (c.getNumeroColis().equals(num)){
                appartient = true;
            }else{
                appartient = false;
            }
        }
        return appartient;
    }

    public Colis showOneColisAerien(Long id){
        Optional<ColisAerien> optional = colisAerienRepository.findById(id);
        ColisAerien colisAerien = null;
        if(optional.isPresent()){
            colisAerien = optional.get();
        }else {
            throw new RuntimeException("Id introuvable");
        }
        return colisAerien;
    }

    public void deleteColisAerien(Long id){
        colisAerienRepository.deleteById(id);
    }

    public void updateStatutColisAerien(String statut, Long id){
        colisAerienRepository.updateStatutColisAerien(statut, id);
    }

    public List<ColisAerien> showColisAerienDepot(String statut){
        return colisAerienRepository.findByStatut(statut);
    }

    public boolean appartenanceColisAerien(List<ColisAerien> liste, Long id){
        boolean app = false;
        for(ColisAerien ca : liste){
            if(ca.getCommandeid() == id){
                app = true;
                break;
            }
        }
        return app;
    }

    public void supprimerColisCommande(Long id){
        colisAerienRepository.supprimerColisCommande(id);
    }

    public ColisAerien showColisAerienCommande(String num){
        return colisAerienRepository.showColisAerienCommande(num);
    }
}
