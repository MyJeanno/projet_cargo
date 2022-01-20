package com.mola.cargo.service;

import com.mola.cargo.model.Carton;
import com.mola.cargo.model.Pays;
import com.mola.cargo.repository.CartonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartonService {

    @Autowired
    private CartonRepository cartonRepository;

    public List<Carton> showCarton(){
        return cartonRepository.findAll();
    }

    public void updateAddStockCarton(Long id, int qte){
        cartonRepository.updateCartonAddQteStock(id, qte);
    }

    public void updateMoinsStockCarton(Long id, int qte){
        cartonRepository.updateCartonRemoveQteStock(id, qte);
    }

    public Carton findCartonById(Long id){
        return cartonRepository.findQteStockById(id);
    }

    public void saveCarton(Carton carton){
        cartonRepository.save(carton);
    }

    public Carton showOneCarton(Long id){
        Optional<Carton> optional = cartonRepository.findById(id);
        Carton carton = null;
        if(optional.isPresent()){
            carton = optional.get();
        }else{
            throw new RuntimeException("Id introuvable");
        }
        return carton;
    }

    public void deleteCarton(Long id){
        cartonRepository.deleteById(id);
    }
}
