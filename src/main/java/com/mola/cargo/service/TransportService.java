package com.mola.cargo.service;

import com.mola.cargo.model.Transport;
import com.mola.cargo.repository.TransportRepository;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportService {

    @Autowired
    private TransportRepository transportRepository;

    public void saveTransport(Transport transport){
        transportRepository.save(transport);
    }

    public List<Transport> showAllTransports(){
        return transportRepository.findAll();
    }

    public Transport showOneTransport(Long id){
        return transportRepository.findById(id).get();
    }

    public void deleteTransport(Long id){
        transportRepository.deleteById(id);
    }

    public double calculerPrixTransportAllemangne(double poids){
        double prix=0;
        for (Transport t : transportRepository.findAll()){
            if(poids>=t.getInf() && poids<=t.getSup()){
                prix = t.getPrix();
            }
        }
        return prix;
    }
}
