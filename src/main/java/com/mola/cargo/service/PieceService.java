package com.mola.cargo.service;

import com.mola.cargo.model.Piece;
import com.mola.cargo.repository.PieceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PieceService {

    @Autowired
    private PieceRepository pieceRepository;

    public void savePiece(Piece piece){
        pieceRepository.save(piece);
    }

    public List<Piece> showPiece(){
        return pieceRepository.findAll();
    }

    public Piece showOnePiece(Long id){
        Optional<Piece> optional = pieceRepository.findById(id);
        Piece piece = null;
        if(optional.isPresent()){
            piece = optional.get();
        }else{
            throw new RuntimeException("Id introuvable");
        }
        return piece;
    }

    public void deletePiece(Long id){
        pieceRepository.deleteById(id);
    }


}
