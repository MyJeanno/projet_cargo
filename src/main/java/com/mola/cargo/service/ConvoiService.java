package com.mola.cargo.service;

import com.mola.cargo.model.Convoi;
import com.mola.cargo.repository.ConvoiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConvoiService {

    @Autowired
    private ConvoiRepository convoiRepository;

    public void saveConvoi(Convoi convoi){
        convoiRepository.save(convoi);
    }

    public List<Convoi> showConvois(){
        return convoiRepository.findAll();
    }

    public List<Convoi> findConvoiMaritime(){
        return convoiRepository.findConvoiMaritime();
    }

    public List<Convoi> findConvoiAerien(){
        return convoiRepository.findConvoiAerien();
    }

    public Convoi showMaLastConvoiAerien(){
        return convoiRepository.showMaLastConvoi();
    }

    public Convoi showMaLastConvoiMaritime(){
        return convoiRepository.showMaLastConvoiMaritime();
    }

    public Convoi showOneConvoi(Long id){
        Optional<Convoi> optional = convoiRepository.findById(id);
        Convoi convoi = null;
        if(optional.isPresent()){
            convoi = optional.get();
        }else {
            throw new RuntimeException("Id introuvable");
        }
        return convoi;
    }

    public List<Convoi> findToutConvoi(){
        return convoiRepository.findToutConvoi();
    }

    public List<Convoi> findTouConvoiMaritime(){
        return convoiRepository.findTouConvoiMaritime();
    }

    public List<Convoi> findToutConvoiAerien(){
        return convoiRepository.findToutConvoiAerien();
    }

    public void deleteConvoi(Long id){
        convoiRepository.deleteById(id);
    }
}
