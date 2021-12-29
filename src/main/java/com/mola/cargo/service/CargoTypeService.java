package com.mola.cargo.service;


import com.mola.cargo.repository.CargoTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CargoTypeService {

    @Autowired
    private CargoTypeRepository cargoTypeRepository;
}
