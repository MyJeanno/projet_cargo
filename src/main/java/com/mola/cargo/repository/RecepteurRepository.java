package com.mola.cargo.repository;

import com.mola.cargo.model.Recepteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RecepteurRepository extends JpaRepository<Recepteur, Long> {

    @Transactional
    @Modifying
    @Query("update Recepteur r SET r.solde = solde + :reste  WHERE r.id = :id")
    void updateSoldeClient(@Param("reste") double reste, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update Recepteur r SET r.solde = solde - :montant  WHERE r.id = :id")
    void updateSoldeClientEncaissement(@Param("montant") double montant, @Param("id") Long id);
}
