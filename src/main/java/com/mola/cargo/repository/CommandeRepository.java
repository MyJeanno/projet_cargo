package com.mola.cargo.repository;

import com.mola.cargo.model.Commande;
import com.mola.cargo.model.Inventaire;
import com.mola.cargo.model.ProduitAerien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
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
    @Query("update Commande c SET c.nbColis = :nb WHERE c.id = :id")
    void updateNbColisCommande(@Param("nb") int nb, @Param("id") Long id);

    @Query("select c from Commande c where c.statutCommande =?1 and c.lieuPaiement =?2")
    List<Commande> commandeByStatusAndByLieu(String s, String lieu);

    @Query("select c from Commande c where c.statutCommande =?1 and c.lieuPaiement =?2 and DATE(c.dateEnvoi) BETWEEN ?3 AND ?4")
    List<Commande> commandeByStatusAndByLieu(String s, String lieu, Date d1, Date d2);

    @Query("select SUM(c.montantPaye) from Commande c where c.statutCommande = :s and c.lieuPaiement = :lieu")
    Double sommeFactureNonEncaisseTogo(String s, String lieu);

    @Query("select SUM(c.montantTotal) from Commande c where c.statutCommande = :s and c.lieuPaiement = :lieu")
    Double sommeFactureAllemagneNonEncaisse(String s, String lieu);

    @Query("select SUM(c.montantTotal) from Commande c where c.statutCommande = :s and c.lieuPaiement = :lieu and DATE(c.dateEnvoi) BETWEEN :d1 AND :d2")
    Double sommeFactureAllemagneNonEncaisse(String s, String lieu, Date d1, Date d2);

    @Transactional
    @Modifying
    @Query("update Commande c set c.statutCommande = :s where c.id = :id")
    void updateStatutCommandeEncaisse(String s, Long id);

    List<Commande> findByStatutCommande(String status);

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
