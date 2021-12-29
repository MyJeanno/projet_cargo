package com.mola.cargo.service;

import com.mola.cargo.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;
}
