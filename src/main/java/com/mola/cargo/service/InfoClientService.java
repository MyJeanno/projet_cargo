package com.mola.cargo.service;

import com.mola.cargo.model.InfoClient;
import com.mola.cargo.repository.InfoClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoClientService {
    @Autowired
    private InfoClientRepository infoClientRepository;

    public void saveInfoClient(InfoClient infoClient){
        infoClientRepository.save(infoClient);
    }

    public List<InfoClient> showAllInfoClients(){
        return infoClientRepository.findAll();
    }
}
