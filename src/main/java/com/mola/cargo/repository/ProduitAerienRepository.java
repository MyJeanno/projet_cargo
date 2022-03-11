package com.mola.cargo.repository;

import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.Produit;
import com.mola.cargo.model.ProduitAerien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitAerienRepository extends JpaRepository<ProduitAerien, Long> {

    @Query("select pa from ProduitAerien pa where pa.colisAerien.commandeid = ?1")
    List<ProduitAerien> findProduitColisAerien(Long id);

    @Query("select SUM(pa.prixProduit) FROM  ProduitAerien pa where pa.colisAerien.commandeid = ?1")
    double sommePrixProduitAerien(Long id);

}
