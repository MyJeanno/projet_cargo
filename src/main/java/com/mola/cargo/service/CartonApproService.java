package com.mola.cargo.service;

import com.mola.cargo.model.CartonAppro;
import com.mola.cargo.repository.CartonApproRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartonApproService {

    @Autowired
    private CartonApproRepository cartonApproRepository;

    public void saveCartonAppro(CartonAppro cartonAppro){
        cartonApproRepository.save(cartonAppro);
    }

    public List<CartonAppro> showCartonAppro(){
        return cartonApproRepository.findAll();
    }

    public CartonAppro findOneCartonAppro(Long id){
        Optional<CartonAppro> optional = cartonApproRepository.findById(id);
        CartonAppro cartonAppro = null;
        if(optional.isPresent()){
            cartonAppro = optional.get();
        }else {
            throw new RuntimeException("id introuvable");
        }
        return cartonAppro;
    }

    public void deleteCartonAppro(Long id){
        cartonApproRepository.deleteById(id);
    }
}
