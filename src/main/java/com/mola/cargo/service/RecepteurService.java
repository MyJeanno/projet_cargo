package com.mola.cargo.service;

import com.mola.cargo.repository.RecepteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecepteurService {

    @Autowired
    private RecepteurRepository recepteurRepository;
}
