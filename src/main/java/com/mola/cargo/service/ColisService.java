package com.mola.cargo.service;

import com.mola.cargo.model.Colis;
import com.mola.cargo.repository.ColisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColisService {

    @Autowired
    private ColisRepository colisRepository;

    public void saveColis(Colis colis){
        colisRepository.save(colis);
    }
}
