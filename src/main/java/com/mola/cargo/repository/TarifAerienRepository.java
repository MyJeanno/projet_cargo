package com.mola.cargo.repository;

import com.mola.cargo.model.TarifAerien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TarifAerienRepository extends JpaRepository<TarifAerien, Long> {

    @Query(value = "select * from tarif_aerien limit 1", nativeQuery = true)
    TarifAerien letarifAerien();
}
