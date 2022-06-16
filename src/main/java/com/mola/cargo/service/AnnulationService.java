package com.mola.cargo.service;

import com.mola.cargo.model.Annulation;
import com.mola.cargo.repository.AnnulationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnulationService {

    @Autowired
    private AnnulationRepository annulationRepository;

    public void saveAnnulation(Annulation annulation){
        annulationRepository.save(annulation);
    }

    public List<Annulation> showAllAnnulation(){
        return annulationRepository.findAll();
    }

}
