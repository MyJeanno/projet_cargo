package com.mola.cargo.service;

import com.mola.cargo.model.ProduitAerien;
import com.mola.cargo.model.ProduitMaritime;
import com.mola.cargo.repository.ProduitMaritimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitMaritimeService {

    @Autowired
    private ProduitMaritimeRepository produitMaritimeRepository;
    @Autowired
    private ReductionService tarifAerienService;

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

    public Double MaxTaxeCommandeMaritime(Long id){
        if(produitMaritimeRepository.MaxTaxeCommandeMaritime(id)==null){
            return 0.0;
        }
        return produitMaritimeRepository.MaxTaxeCommandeMaritime(id);
    }

    public Double sommePoidsColisMaritime(Long id){
        if(produitMaritimeRepository.sommePoidsColisMaritime(id) == null){
            return 0.;
        }
        return produitMaritimeRepository.sommePoidsColisMaritime(id);
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

    /*public double taxe(List<ProduitMaritime> listeMaritime) {
        double taxe = 0;
        for (ProduitMaritime p : listeMaritime) {
            if (p.getTarif().getTaxe().equals(Constante.TAXE_OUI)) {
                taxe = tarifAerienService.leTarifaerien().getTaxe();
                break;
            }
        }
        return taxe;
    }*/

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

    public boolean appartenanceProduitMaritime(List<ProduitMaritime> liste, Long id) {
        boolean app = false;
        for (ProduitMaritime pm : liste) {
            if (pm.getColisMaritime().getCommandeid() == id) {
                app = true;
                break;
            }
        }
        return app;
    }

    public void supprimerProduitCommande(Long id){
        produitMaritimeRepository.supprimerProduitCommande(id);
    }

}
