package com.mola.cargo.service;


import com.mola.cargo.model.CargoType;
import com.mola.cargo.repository.CargoTypeRepository;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CargoTypeService {

    @Autowired
    private CargoTypeRepository cargoTypeRepository;

    public List<CargoType> showCargoType(){
        return  cargoTypeRepository.findAll();
    }

    public void saveCargoType(CargoType cargoType){
        cargoTypeRepository.save(cargoType);
    }

    public CargoType oneCargoType(Long id){
        Optional<CargoType> optional = cargoTypeRepository.findById(id);
        CargoType cargoType = null;
        if(optional.isPresent()){
            cargoType = optional.get();
        }else{
            throw new RuntimeException("id introuvable");
        }
        return cargoType;
    }

    public void deleteCargoType(Long id){
        cargoTypeRepository.deleteById(id);
    }
}
