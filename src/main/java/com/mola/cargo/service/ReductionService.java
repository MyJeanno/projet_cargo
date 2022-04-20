package com.mola.cargo.service;

import com.mola.cargo.model.Reduction;
import com.mola.cargo.repository.ReductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReductionService {

    @Autowired
    private ReductionRepository reductionRepository;

    public void saveReduction(Reduction reduction){
        reductionRepository.save(reduction);
    }

    public List<Reduction> showAllReduction(){
        return reductionRepository.findAll();
    }

    public Reduction showOneReduction(Long id){
        return reductionRepository.findById(id).get();
    }

    public void deleteReduction(Long id){
        reductionRepository.deleteById(id);
    }

    public Reduction lareduction(){
        return reductionRepository.laReduction();
    }
}
