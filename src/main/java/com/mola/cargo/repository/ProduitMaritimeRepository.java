package com.mola.cargo.repository;

import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.ProduitAerien;
import com.mola.cargo.model.ProduitMaritime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitMaritimeRepository extends JpaRepository<ProduitMaritime, Long> {

    @Query("select pm from ProduitMaritime pm where pm.colisMaritime.commandeid = ?1")
    List<ProduitMaritime> findProduitColisMaritime(Long id);

    @Query("select max(pm.tarif.taxeMaritime) from ProduitMaritime pm where pm.colisMaritime.commandeid = ?1")
    Double MaxTaxeCommandeMaritime(Long id);

    @Query("select SUM(pm.poids) FROM ProduitMaritime pm where pm.colisMaritimeid = ?1")
    Double sommePoidsColisMaritime(Long id);

    @Query("delete from ProduitMaritime pm where pm.colisMaritime.commandeid = :id")
    void supprimerProduitCommande(@Param("id") Long id);
}
