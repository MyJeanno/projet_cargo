package com.mola.cargo.repository;

import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.ColisMaritime;
import com.mola.cargo.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ColisAerienRepository extends JpaRepository<ColisAerien, Long> {

    @Query("select ca from ColisAerien ca where ca.commandeid = ?1")
    List<ColisAerien> findColisAerienCommande(Long id);

    @Query("select ca from ColisAerien ca where ca.commande.pin = ?1")
    List<ColisAerien> findColisAerienCommandePin(String pin);

    @Query(value = "select * from Colis_aerien ORDER BY id DESC LIMIT 1", nativeQuery = true)
    ColisAerien showMaLastColisAerien();

    @Query(value = "SELECT count(DISTINCT commandeid) FROM colis_aerien", nativeQuery = true)
    int nbreCommandeAerien();

    @Query("select COUNT(ca) FROM  ColisAerien ca where ca.commandeid = ?1 and ca.id in (select pa.colisAerienid from ProduitAerien pa)")
    int nbreColisAerien(Long id);

    @Transactional
    @Modifying
    @Query("update ColisAerien ca SET ca.statut = :statut WHERE ca.id = :id")
    void updateStatutColisAerien(@Param("statut") String statut, @Param("id") Long id);

    List<ColisAerien> findByStatut(String statut);

    @Query("select SUM(ca.poids) FROM ColisAerien ca where ca.commandeid = ?1")
    Double poidsTotalColisAerien(Long id);

    @Transactional
    @Modifying
    @Query("update ColisAerien ca SET ca.poids = :p WHERE ca.id = :id")
    void updatePoidsColisAerien(@Param("p") double p, @Param("id") Long id);

}
