package com.mola.cargo.repository;

import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.Convoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConvoiRepository extends JpaRepository<Convoi, Long> {

    @Query(value = "SELECT * FROM convoi i WHERE i.identifiant LIKE 'MFM%' AND i.id NOT IN (SELECT convoiid FROM sortie_maritime)", nativeQuery = true)
    List<Convoi> findConvoiMaritime();

    @Query(value = "SELECT * FROM convoi i WHERE i.identifiant LIKE 'MFA%' AND i.id NOT IN (SELECT convoiid FROM sortie_aerien)", nativeQuery = true)
    List<Convoi> findConvoiAerien();

    @Query(value = "select * from convoi WHERE identifiant LIKE 'MFA%' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Convoi showMaLastConvoi();

    @Query(value = "select * from convoi WHERE identifiant LIKE 'MFM%' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Convoi showMaLastConvoiMaritime();

    @Query(value = "SELECT * FROM convoi order by date_creation desc", nativeQuery = true)
    List<Convoi> findToutConvoi();

    @Query(value = "SELECT * FROM convoi i WHERE i.identifiant LIKE 'MFM%'", nativeQuery = true)
    List<Convoi> findTouConvoiMaritime();

    @Query(value = "SELECT * FROM convoi i WHERE i.identifiant LIKE 'MFA%'", nativeQuery = true)
    List<Convoi> findToutConvoiAerien();

    //Convoi findByStatut(String statut);
}
