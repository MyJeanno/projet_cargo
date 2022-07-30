package com.mola.cargo.repository;

import com.mola.cargo.model.Colis;
import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.ColisMaritime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ColisMaritimeRepository extends JpaRepository<ColisMaritime, Long> {

    @Query("select cm from ColisMaritime cm where cm.commandeid = ?1")
    List<ColisMaritime> findColisMaritimeCommande(Long id);

    @Query("select cm from ColisMaritime cm where cm.commande.pin = ?1")
    List<ColisMaritime> findColisMaritimeCommandePin(String pin);

    @Query(value = "select * from colis_maritime WHERE commandeid = :id ORDER BY id DESC LIMIT 1", nativeQuery = true)
    ColisMaritime showMaLastColisMaritime(@Param("id") Long id);

    @Query(value = "select * from colis_maritime where commandeid=:id LIMIT 1", nativeQuery = true)
    ColisMaritime showMaColisMaritimeByCommandeId(@Param("id") Long id);

    @Query("select COUNT(cm) FROM  ColisMaritime cm where cm.commandeid = ?1 and cm.id in (select p.colisMaritimeid from ProduitMaritime p)")
    int nbreColisMaritime(Long id);

    @Query("select SUM(cm.prixColis) FROM ColisMaritime cm where cm.commandeid = ?1 and cm.carton.code = ?2 and cm.id in (select p.colisMaritimeid from ProduitMaritime p)")
    Double montantPrixCarton(Long id, String code);

    @Query("select COUNT(cm) FROM ColisMaritime cm where cm.commandeid = ?1 and cm.carton.code = ?2 and cm.id in (select p.colisMaritimeid from ProduitMaritime p)")
    int nbreCarton(Long id, String code);

    @Query(value = "SELECT count(DISTINCT commandeid) FROM colis_maritime", nativeQuery = true)
    int nbreCommandeMaritime();

    @Query("select SUM(cm.prixColis) FROM ColisMaritime cm where cm.commandeid = ?1 AND cm.id in (select p.colisMaritimeid from ProduitMaritime p)")
    Double montantTotalPrixCarton(Long id);

    @Query("select SUM(cm.transportAllemagne) FROM ColisMaritime cm where cm.commandeid = ?1")
    Double montantTotalTransport(Long id);

    @Query("select SUM(cm.poids) FROM ColisMaritime cm where cm.statut = ?1")
    Double poidsTotalMaritimeDepot(String etat);

    @Transactional
    @Modifying
    @Query("update ColisMaritime cm SET cm.statut = :statut WHERE cm.id = :id")
    void updateStatutColisMaritime(@Param("statut") String statut, @Param("id") Long id);

    List<ColisMaritime> findByStatut(String statut);

    @Transactional
    @Modifying
    @Query("update ColisMaritime cm SET cm.poids = :poids, cm.transportAllemagne = :trans where cm.id = :id")
    void updatePoidsTransportColisMaritime(@Param("poids") double poids, @Param("trans") double trans, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update ColisMaritime cm SET cm.poids = :poids where cm.id = :id")
    void updatePoidsColisMaritime(@Param("poids") double poids, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from colis_maritime where commandeid=:id", nativeQuery = true)
    void supprimerColisCommande(@Param("id") Long id);


}
