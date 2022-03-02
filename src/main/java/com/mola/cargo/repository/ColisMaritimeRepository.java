package com.mola.cargo.repository;

import com.mola.cargo.model.Colis;
import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.ColisMaritime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColisMaritimeRepository extends JpaRepository<ColisMaritime, Long> {

    @Query("select cm from ColisMaritime cm where cm.commandeid = ?1")
    List<ColisMaritime> findColisMaritimeCommande(Long id);

    @Query(value = "select * from Colis_maritime ORDER BY id DESC LIMIT 1", nativeQuery = true)
    ColisMaritime showMaLastColisMaritime();

    @Query("select COUNT(cm) FROM  ColisMaritime cm where cm.commandeid = ?1")
    int nbreColisMaritime(Long id);

    @Query("select SUM(cm.prixColis) FROM ColisMaritime cm where cm.commandeid = ?1 and cm.carton.code = ?2")
    double montantPrixCarton(Long id, String code);

    @Query("select COUNT(cm) FROM ColisMaritime cm where cm.commandeid = ?1 and cm.carton.code = ?2")
    int nbreCarton(Long id, String code);

    @Query(value = "SELECT count(DISTINCT commandeid) FROM colis_maritime", nativeQuery = true)
    int nbreCommandeMaritime();

}
