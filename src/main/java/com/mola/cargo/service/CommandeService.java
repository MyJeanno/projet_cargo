package com.mola.cargo.service;

import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.Commande;
import com.mola.cargo.model.User;
import com.mola.cargo.repository.CommandeRepository;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    public int genererNbre(int borneInf, int borneSup){
        Random random = new Random();
        return (borneInf+random.nextInt(borneSup-borneInf));
    }

    public Commande showOnecommande(Long id){
        return commandeRepository.findById(id).get();
    }

    public void updateTypeCommande(String type, Long id){
        commandeRepository.updateTypeCommande(type, id);
    }

    public void updatePaiementCommande(double total, double paye, Long id){
        commandeRepository.updatePaiementCommande(total, paye, id);
    }

    public void updateEtatCommande(String etat, Long id){
        commandeRepository.updateEtatCommande(etat, id);
    }

    public void updateNbColisCommande(int nb, Long id){
        commandeRepository.updateNbColisCommande(nb, id);
    }

    public List<Commande> commandeSelonEtat(String etat){
        return commandeRepository.findByEtatCommande(etat);
    }

    public Commande showCommandePin(String pin){
        return commandeRepository.findByPin(pin);
    }

    public boolean testerAppartenance(List<Commande> liste, String pin){
        boolean appartient = false;
        for (Commande c:liste){
            if (c.getPin().equals(pin)){
                appartient = true;
            }else{
                appartient = false;
            }
        }
        return appartient;
    }

    public boolean CommandeAppartenanceAerien(List<ColisAerien> liste, String pin){
        boolean appartient = false;
        for (ColisAerien c:liste){
            if (c.getCommande().getPin().equals(pin)){
                appartient = true;
            }else{
                appartient = false;
            }
        }
        return appartient;
    }

    public int totalCommande(){
      return commandeRepository.findAll().size();
    }

    public Commande showMaLastCommande(Long id){
        return commandeRepository.showMaLastCommande(id);
    }
    public void saveCommande(Commande commande){
        commandeRepository.save(commande);
    }
    public List<Commande> showCommande(){
        return commandeRepository.findAll();
    }

    public String getIdentitePersonnePaye(Long id){
        return showOnecommande(id).getEmetteur().getNomPersonne()+" "+showOnecommande(id).getEmetteur().getPrenomPersonne();
    }

    public String getIdentitePersonneNonPaye(Long id){
        return showOnecommande(id).getRecepteur().getNomPersonne()+" "+showOnecommande(id).getRecepteur().getPrenomPersonne();
    }

    public void supprimerCommande(Long id){
        commandeRepository.supprimerCommande(id);
    }

    public List<Commande> showCommandeInacheve(String etat, Long id){
        return commandeRepository.showCommandeInacheve(etat, id);
    }

    public List<Commande> commandeByStatusAndByLieu(String statut, String lieu, Date d1, Date d2){
        return commandeRepository.commandeByStatusAndByLieu(statut, lieu, d1, d2);
    }

    public List<Commande> commandeByStatusAndByLieu(String statut, String lieu){
        return commandeRepository.commandeByStatusAndByLieu(statut, lieu);
    }

    public Double sommeFactureNonEncaisseTogo(String s, String lieu){
        if(commandeRepository.sommeFactureNonEncaisseTogo(s, lieu) == null){
            return 0.0;
        }else {
            return commandeRepository.sommeFactureNonEncaisseTogo(s, lieu);
        }
    }

    public Double sommeFactureAllemagneNonEncaisse(String s, String lieu){
        if(commandeRepository.sommeFactureAllemagneNonEncaisse(s, lieu) == null){
            return 0.0;
        }else {
            return commandeRepository.sommeFactureAllemagneNonEncaisse(s, lieu);
        }
    }

    public Double sommeFactureAllemagneNonEncaisse(String s, String lieu, Date d1, Date d2){
        if(commandeRepository.sommeFactureAllemagneNonEncaisse(s, lieu, d1, d2) == null){
            return 0.0;
        }else {
            return commandeRepository.sommeFactureAllemagneNonEncaisse(s, lieu, d1, d2);
        }
    }

    public void updateStatutCommandeEncaisse(String statut, Long id){
        commandeRepository.updateStatutCommandeEncaisse(statut, id);
    }

    public List<Commande> commandeSelonStatut(String statut){
        return commandeRepository.findByStatutCommande(statut);
    }

}
