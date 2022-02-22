package com.mola.cargo.repository;

import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.ProduitAerien;
import com.mola.cargo.model.ProduitMaritime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitMaritimeRepository extends JpaRepository<ProduitMaritime, Long> {

    @Query("select pm from ProduitMaritime pm where pm.colisMaritime.commandeid = ?1")
    List<ProduitMaritime> findProduitColisMaritime(Long id);
}
