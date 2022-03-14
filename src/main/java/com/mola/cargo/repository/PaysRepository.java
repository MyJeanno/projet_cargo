package com.mola.cargo.repository;

import com.mola.cargo.model.Pays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaysRepository extends JpaRepository<Pays, Long> {

    @Query(value = "select * from pays where libelle_pays LIKE '%Allemagne'", nativeQuery = true)
    List<Pays> findPaysTarif();
}
