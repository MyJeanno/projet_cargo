package com.mola.cargo.repository;

import com.mola.cargo.model.SortieAerien;
import com.mola.cargo.model.SortieMaritime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SortieMaritimeRepository extends JpaRepository<SortieMaritime, Long> {
    List<SortieMaritime> findByConvoiid(Long id);

    @Query("select DISTINCT(sm) from SortieMaritime sm where sm.convoiid = :id")
    List<SortieMaritime> listeCommande(Long id);

    @Query("select SUM(sm.colisMaritime.poids) FROM SortieMaritime sm where sm.convoiid = ?1")
    Double poidsTotalColisMaritimeLot(Long id);

    @Query("select pm.tarif.categorieProduit.nomCategorie, SUM(pm.poids) as poids " +
            "from ProduitMaritime pm " +
            "where pm.colisMaritimeid in (select sm.colisMaritimeid from SortieMaritime sm where sm.convoiid = ?1 )" +
            "GROUP BY pm.tarif.categorieProduit.nomCategorie")
    List<String> PoidsParCategorieAlimentaire(Long id);
}
