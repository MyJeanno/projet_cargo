package com.mola.cargo.service;

import com.mola.cargo.model.Convoi;
import com.mola.cargo.repository.ConvoiRepository;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        if(convoiRepository.showMaLastConvoi()==null){
            return new Convoi(1L, Constante.PREFIX_AERIEN+"_"+ LocalDate.now(), LocalDateTime.now());
        }
        return convoiRepository.showMaLastConvoi();
    }

    public Convoi showMaLastConvoiMaritime(){
        if(convoiRepository.showMaLastConvoiMaritime()==null){
            return new Convoi(1L, Constante.PREFIX_MARITIME+"_"+ LocalDate.now(), LocalDateTime.now());
        }
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
