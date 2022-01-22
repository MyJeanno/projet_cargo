package com.mola.cargo.repository;

import com.mola.cargo.model.Tarif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarifRepository extends JpaRepository<Tarif, Long> {

}
