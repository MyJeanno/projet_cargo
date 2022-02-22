package com.mola.cargo.repository;

import com.mola.cargo.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    @Query(value = "select * from commande ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public Commande showMaLastCommande();

    Commande findCommandeByPin(String pin);
}
