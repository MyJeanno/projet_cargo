package com.mola.cargo.repository;

import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.Produit;
import com.mola.cargo.model.ProduitAerien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProduitAerienRepository extends JpaRepository<ProduitAerien, Long> {

    @Query("select pa from ProduitAerien pa where pa.colisAerien.commandeid = ?1")
    List<ProduitAerien> findProduitColisAerien(Long id);

    @Query("select pa from ProduitAerien pa where pa.colisAerien.statut = ?1")
    List<ProduitAerien> findProduitColisAerienDepot(String statut);

    @Query("select pa.tarif.categorieProduit.nomCategorie, SUM(pa.poids) as poids " +
            "from ProduitAerien pa " +
            "where pa.colisAerien.statut = ?1 " +
            "GROUP BY pa.tarif.categorieProduit.nomCategorie")
    List<String> PoidsParCategorieAlimentaire(String statut);

    //@Query("select pa from ProduitAerien pa where pa.colisAerienid = ?1")
    List<ProduitAerien> findByColisAerienid(Long id);

    @Query("select max(pa.tarif.prixKilo) from ProduitAerien pa where pa.colisAerienid = ?1")
    Double MaxPrixTarifColisAerien(Long id);

    @Query("select max(pa.tarif.taxeAerien) from ProduitAerien pa where pa.colisAerien.commandeid = ?1")
    Double MaxTaxeCommandeAerien(Long id);

    //@Query("select SUM(pa.prixProduit) FROM ProduitAerien pa where pa.colisAerien.commandeid = ?1")
   // Double sommePrixProduitAerien(Long id);

    @Query("select SUM(ca.emballage.prix) from ColisAerien ca where ca.commandeid = ?1 and ca.id in (select pa.colisAerienid from ProduitAerien pa)")
    Double fraisEmballage(Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from produit_aerien where colis_aerienid=:id", nativeQuery = true)
    void supprimerProduitCommande(@Param("id") Long id);

}
