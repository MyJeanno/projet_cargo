package com.mola.cargo.repository;

import com.mola.cargo.model.Emetteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EmetteurRepository extends JpaRepository<Emetteur, Long> {
    @Query(value = "SELECT * from emetteur WHERE userid = :id ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public Emetteur showMaLastEmetteur(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update Emetteur e SET e.etatPersonne = :etat WHERE e.id = :id")
    void updateInfoEmetteur(@Param("etat") String etat, @Param("id") Long id);
}
