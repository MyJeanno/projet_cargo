package com.mola.cargo.service;

import com.mola.cargo.model.Colis;
import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.ColisMaritime;
import com.mola.cargo.model.ProduitMaritime;
import com.mola.cargo.repository.ColisMaritimeRepository;
import com.mola.cargo.repository.ColisRepository;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
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

    public List<ColisMaritime> findColisMaritimeCommandePin(String pin){
        return colisMaritimeRepository.findColisMaritimeCommandePin(pin);
    }

    public Double montantTotalPrixCarton(Long id){
        if(colisMaritimeRepository.montantTotalPrixCarton(id) == null){
            return 0.0;
        }
        else {
            return colisMaritimeRepository.montantTotalPrixCarton(id);
        }
    }

    public void updatePoidsTransportColisMaritime(double poids, double trans, Long id){
        colisMaritimeRepository.updatePoidsTransportColisMaritime(poids, trans, id);
    }

    public int nombreCommandeMaritime(){
        return colisMaritimeRepository.nbreCommandeMaritime();
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

    public boolean testerAppartenance(List<ColisMaritime> liste, String num){
        boolean appartient = false;
        for (ColisMaritime c:liste){
            if (c.getNumeroColis().equals(num)){
                appartient = true;
            }else{
                appartient = false;
            }
        }
        return appartient;
    }

    public ColisMaritime showMaLastColisMaritime(){
        return colisMaritimeRepository.showMaLastColisMaritime();
    }

    public int nbreColisMaritime(Long id){
        return colisMaritimeRepository.nbreColisMaritime(id);
    }

    public Double montantPrixCarton(Long id, String code){
        if(colisMaritimeRepository.montantPrixCarton(id, code) == null){
            return 0.0;
        }
        return colisMaritimeRepository.montantPrixCarton(id, code);
    }

    public double appliquerReduction(double montant, double reduction){
        return montant-montant*reduction/100;
    }

    public int nbreSelonCarton(Long id, String code){
        return colisMaritimeRepository.nbreCarton(id,code);
    }

    public void updateStatutColisMaritime(String statut, Long id){
        colisMaritimeRepository.updateStatutColisMaritime(statut,id);
    }

    public List<ColisMaritime> showColisMaritimeDepot(String statut){
        return colisMaritimeRepository.findByStatut(statut);
    }

    public void updatePoidsColisMaritime(double poids, Long id){
        colisMaritimeRepository.updatePoidsColisMaritime(poids, id);
    }

    public double arrondirPoids(double d){
        if(d-(int)d<=0.5){
            return (int)d + 0.5;
        }else{
            return (int)d + 1;
        }
    }

    public Double montantTotalTransport(Long id){
        if(colisMaritimeRepository.montantTotalTransport(id) == null){
            return 0.0;
        }else {
            return colisMaritimeRepository.montantTotalTransport(id);
        }
    }

    public boolean appartenanceColisMaritime(List<ColisMaritime> liste, Long id){
        boolean app = false;
        for(ColisMaritime cm : liste){
            if(cm.getCommandeid() == id){
                app = true;
                break;
            }
        }
        return app;
    }

    public void supprimerColisCommande(Long id){
        colisMaritimeRepository.supprimerColisCommande(id);
    }

}
