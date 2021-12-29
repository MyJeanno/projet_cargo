package com.mola.cargo.service;

import com.mola.cargo.repository.PieceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PieceService {

    @Autowired
    private PieceRepository pieceRepository;
}
