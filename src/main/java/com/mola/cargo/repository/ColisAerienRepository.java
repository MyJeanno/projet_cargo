package com.mola.cargo.repository;

import com.mola.cargo.model.ColisAerien;
import com.mola.cargo.model.ColisMaritime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColisAerienRepository extends JpaRepository<ColisAerien, Long> {

    @Query("select ca from ColisAerien ca where ca.commandeid = ?1")
    List<ColisAerien> findColisAerienCommande(Long id);
}
