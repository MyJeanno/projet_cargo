package com.mola.cargo.repository;

import com.mola.cargo.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    @Query(value = "select * from commande ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Commande showMaLastCommande();

    //List<Commande> findByStatut(String statut);

    Commande findByPin(String pin);
}
