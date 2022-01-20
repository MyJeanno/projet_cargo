package com.mola.cargo.service;

import com.mola.cargo.model.CartonVente;
import com.mola.cargo.repository.CartonVenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartonVenteService {

    @Autowired
    private CartonVenteRepository cartonVenteRepository;

    public List<CartonVente> showCartonVente(){
        return cartonVenteRepository.findAll();
    }

    public void savecartonVente(CartonVente cartonVente){
        cartonVenteRepository.save(cartonVente);
    }

    public CartonVente showOneCartonVente(Long id){
        Optional<CartonVente> optional = cartonVenteRepository.findById(id);
        CartonVente cartonVente = null;
        if(optional.isPresent()){
            cartonVente = optional.get();
        }else{
            throw new RuntimeException("Id introuvable");
        }
        return cartonVente;
    }

    public void deleteCartonVente(long id){
        cartonVenteRepository.deleteById(id);
    }
}
