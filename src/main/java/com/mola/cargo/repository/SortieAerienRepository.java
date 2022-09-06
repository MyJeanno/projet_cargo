package com.mola.cargo.repository;

import com.mola.cargo.model.SortieAerien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SortieAerienRepository extends JpaRepository<SortieAerien, Long> {

    List<SortieAerien> findByConvoiid(Long id);

    @Query("select sa, COUNT(sa) from SortieAerien sa group by sa")
    List<SortieAerien> sortieAerienByCommande();

    @Query("select DISTINCT(sa) from SortieAerien sa where sa.convoiid = :id")
    List<SortieAerien> listeCommande(Long id);

    @Query("select ROUND(SUM(sa.colisAerien.poids),2) FROM SortieAerien sa where sa.convoiid = ?1")
    Double poidsTotalColisAerienLot(Long id);

    @Query("select pa.tarif.categorieProduit.nomCategorie, ROUND(SUM(pa.poids),2) " +
            "from ProduitAerien pa " +
            "where pa.colisAerienid in (select sa.colisAerienid from SortieAerien sa where sa.convoiid = ?1 )" +
            "GROUP BY pa.tarif.categorieProduit.nomCategorie")
    List<String> PoidsParCategorieAlimentaire(Long id);



}
