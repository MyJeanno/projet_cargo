package com.mola.cargo.repository;

import com.mola.cargo.model.Annulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnulationRepository extends JpaRepository<Annulation, Integer> {
}
