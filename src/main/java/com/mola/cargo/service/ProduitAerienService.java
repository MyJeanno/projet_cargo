package com.mola.cargo.service;

import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.ColisMaritime;
import com.mola.cargo.model.ProduitAerien;
import com.mola.cargo.repository.ProduitAerienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProduitAerienService {
    @Autowired
    private ProduitAerienRepository produitAerienRepository;
    @Autowired
    private ReductionService tarifAerienService;

    public List<ProduitAerien> showProduitsAerien(){
        return produitAerienRepository.findAll();
    }

    public void saveProduitAerien(ProduitAerien produitAerien){
        produitAerienRepository.save(produitAerien);
    }

   /* public Double sommePrixProduitAerien(Long id){
        if(produitAerienRepository.sommePrixProduitAerien(id) == null){
            return 0.0;
        }else{
            return produitAerienRepository.sommePrixProduitAerien(id);
        }
    }*/

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

    public List<ProduitAerien> showProduitColisAerien(Long id){
        return produitAerienRepository.findByColisAerienid(id);
    }

    public Double showMaxPrixProduit(Long id){
        if(produitAerienRepository.MaxPrixTarifColisAerien(id)==null){
            return 0.0;
        }
        return produitAerienRepository.MaxPrixTarifColisAerien(id);
    }

    public Double showMaxTaxeAerienne(Long id){
        if (produitAerienRepository.MaxTaxeCommandeAerien(id)==null){
            return 0.0;
        }
        return produitAerienRepository.MaxTaxeCommandeAerien(id);
    }

    public void deleteProduitAerien(Long id){
        produitAerienRepository.deleteById(id);
    }

    public boolean estentier(double n){
        if(n==(int)n)
            return true;
        return false;
    }

   /*public double taxe(List<ProduitAerien> listeAerien) {
        double taxe = 0;
        for (ProduitAerien p : listeAerien) {
            if (p.getTarif().getTaxe().equals(Constante.TAXE_OUI)) {
                taxe = tarifAerienService.leTarifaerien().getTaxe();
                break;
            }
        }
        return taxe;
    }*/

    public Double fraisEmballage(Long id){
        if(produitAerienRepository.fraisEmballage(id) == null){
            return 0.0;
        }else {
            return produitAerienRepository.fraisEmballage(id);
        }
    }

    public String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

   /* public double fraisEmballage(List<ColisAerien> listeca){
        double montant=0;
        for (ColisAerien ca:listeca){
            montant = montant + ca.getEmballage().getPrix();
        }
        return montant;
    }*/

    public boolean appartenanceProduitAerien(List<ProduitAerien> liste, Long id){
        boolean app = false;
        for(ProduitAerien pa : liste){
            if(pa.getColisAerien().getCommandeid() == id){
                app = true;
                break;
            }
        }
        return app;
    }

    public void supprimerProduitCommande(List<ColisAerien> liste_aerien){
        for(ColisAerien ca:liste_aerien){
            produitAerienRepository.supprimerProduitCommande(ca.getId());
        }
    }
    public List<ProduitAerien> findProduitColisAerienDepot(String etat){
        return produitAerienRepository.findProduitColisAerienDepot(etat);
    }

    public List<String> PoidsParCategorieAlimentaire(String statut){
        List<String> listeDefault = new ArrayList<>();
        listeDefault = produitAerienRepository.PoidsParCategorieAlimentaire(statut);
        if(listeDefault.isEmpty()){
            listeDefault.add("0");
        }
        return listeDefault;
    }
}
