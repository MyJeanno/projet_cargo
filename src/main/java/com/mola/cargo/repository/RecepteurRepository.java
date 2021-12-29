package com.mola.cargo.repository;

import com.mola.cargo.model.Recepteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecepteurRepository extends JpaRepository<Recepteur, Long> {
}
