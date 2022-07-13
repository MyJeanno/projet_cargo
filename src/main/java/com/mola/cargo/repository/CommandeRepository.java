package com.mola.cargo.repository;

import com.mola.cargo.model.Commande;
import com.mola.cargo.model.ProduitAerien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    @Query(value = "select * from commande WHERE userid = :id ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Commande showMaLastCommande(@Param("id") Long id);

    @Query("select c from Commande c where c.etatCommande != :etat and userid = :id")
    List<Commande> showCommandeInacheve(@Param("etat") String etat, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update Commande c SET c.typeEnvoi = :type WHERE c.id = :id")
    void updateTypeCommande(@Param("type") String type, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update Commande c SET c.etatCommande = :etat WHERE c.id = :id")
    void updateEtatCommande(@Param("etat") String etat, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update Commande c SET c.montantTotal = :total, c.montantPaye = :paye WHERE c.id = :id")
    void updatePaiementCommande(@Param("total") double total, @Param("paye") double paye, @Param("id") Long id);

    Commande findByPin(String pin);
    List<Commande> findByEtatCommande(String etat);
    @Transactional
    @Modifying
    @Query(value = "delete from commande where id=:idc", nativeQuery = true)
    void supprimerCommande(@Param("idc") Long idc);
}
