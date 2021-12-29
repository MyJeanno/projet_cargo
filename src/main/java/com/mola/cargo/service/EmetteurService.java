package com.mola.cargo.service;

import com.mola.cargo.repository.EmetteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmetteurService {

    @Autowired
    private EmetteurRepository emetteurRepository;
}
