package com.mola.cargo.repository;

import com.mola.cargo.model.Emetteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmetteurRepository extends JpaRepository<Emetteur, Long> {

}
