package com.mola.cargo.service;

import com.mola.cargo.model.Codepostal;
import com.mola.cargo.repository.CodepostalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodepostalService {

    @Autowired
    private CodepostalRepository codepostalRepository;

    public void saveCodepostal(Iterable<Codepostal> codepostals){
        codepostalRepository.saveAll(codepostals);
    }

    public List<Codepostal> showCodepostal(){
        return codepostalRepository.findAll();
    }

    public Codepostal findEtatCodePostal(int code){
        return codepostalRepository.etatCodePostal(code);
    }

}
