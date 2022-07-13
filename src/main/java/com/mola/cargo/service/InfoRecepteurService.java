package com.mola.cargo.service;

import com.mola.cargo.model.InfoRecepteur;
import com.mola.cargo.repository.InfoRecepteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoRecepteurService {

    @Autowired
    private InfoRecepteurRepository infoRecepteurRepository;

    public void saveInfoRecepteur(InfoRecepteur infoRecepteur){
        infoRecepteurRepository.save(infoRecepteur);
    }

    public List<InfoRecepteur> showAllInfoRecepteur(){
        return infoRecepteurRepository.findAll();
    }
}
