package com.mola.cargo.repository;

import com.mola.cargo.model.Inventaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InventaireRepository extends JpaRepository<Inventaire, Long> {

    @Transactional
    @Modifying
    @Query("update Inventaire i set i.status = :s where i.id = :id")
    void updateStatutInventaire(String s, Long id);

    @Query("select i from Inventaire i where i.status =?1 and i.commande.lieuPaiement =?2")
    List<Inventaire> findByStatusAndByLieu(String s, String lieu);

    @Query("select SUM(i.prixTotal) from Inventaire i where i.status = :s and i.commande.lieuPaiement = :lieu")
    Double sommeFactureNonEncaisse(String s, String lieu);
}
